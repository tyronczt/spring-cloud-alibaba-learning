### Spring Cloud Alibaba 学习

> 学习视频（B站）：https://www.bilibili.com/video/BV1Mt4y1i7JW

#### 学习计划

1. **Nacos 服务注册与发现**
3. **Ribbon 负载均衡**
4. **Sentinel 流量控制**
5. **RockerMQ 消息的生产和消费**
6. **Gateway 路由映射和限流**
7. **Seata 分布式事务**

#### 零、项目初始化搭建

创建父工程，作为项目的大环境，微服务的各个组件作为子项目，继承父项目。

> 首先确保 JDK、Maven、IDEA 安装正常.
>
> Maven 推荐配置文件 [maven的settings.xml文件](https://blog.csdn.net/tian330726/article/details/112549171)

父项目根据 Spring Initializr 自动创建项目，并添加  `spring-cloud-dependencies` 和 `spring-cloud-alibaba-dependencies` 的依赖，注意踩坑点：**pom 中的那几个依赖先放到springboot自带的 dependencies 中，然后再添加到 DependentDependencyManagement 中**，否则会导致依赖下载不下来的问题。

具体pom.xml文件，参看代码： https://github.com/tyronczt/spring-cloud-alibaba-learning/blob/master/pom.xml

### 一、Nacos 服务注册与发现

> 官网安装包下载地址：https://github.com/alibaba/nacos/releases/tag/1.2.1
>
> 免费下载地址：https://download.csdn.net/download/tian330726/14150203

此处使用 Windows 环境，解压zip文件，进入bin目录，双击 `startup.cmd` ，会弹出命令框启动nacos程序，如果弹出后立即消失，**记得检查下 JAVA_HOME 是否配置！**

此时访问地址：http://localhost:8848/nacos

![nacos-init](https://raw.githubusercontent.com/tyronczt/spring-cloud-alibaba-learning/master/picture/nacos-init.png)

##### 创建provider项目

继承父项目，并加入`nacos-discovery` 的依赖

```xml
<parent>
    <groupId>com.tyron</groupId>
    <artifactId>springcloudalibabademo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</parent>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```

配置application.yml

```yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
  application:
    name: provider
server:
  port: 1111/2222/3333
```

启动项目时，Edit Configurations，将 `Allow parallel run` 的选项勾上，最终：

![provider](https://raw.githubusercontent.com/tyronczt/spring-cloud-alibaba-learning/master/picture/provider%E9%9B%86%E7%BE%A4.png)

##### 创建consumer项目

pom.xml文件同 provider 项目类似

新建 ConsumerController，对服务注册情况进行查看

```java
@RestController
public class ConsumerController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/instances")
    private List<ServiceInstance> instances(){
        return this.discoveryClient.getInstances("provider");
    }

}
```

启动后浏览器查看

![consumer-instances](https://raw.githubusercontent.com/tyronczt/spring-cloud-alibaba-learning/master/picture/consumer-instances.png)

#### 通过 RestTemplate 远程调用 Provider 的服务

**provider 提供服务**

```java
//SPEL
@Value("${server.port}")
private String port;

@GetMapping("/index")
public String index() {
	return port;
}
```

consumer 配置  RestTemplate

```java
@Configuration
public class ConsumerConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

ConsumerController 随机调用 provider 服务

```java
@Autowired
private RestTemplate restTemplate;

@GetMapping("/index")
public String index() {
    List<ServiceInstance> provider = discoveryClient.getInstances("provider");
    int index = ThreadLocalRandom.current().nextInt(provider.size());
    String url = provider.get(index).getUri() + "/index";
    return "consumer随机远程调用provier：" + this.restTemplate.getForObject(url, String.class);
}
```



## 二、Ribbon负载均衡

简介：

Spring Cloud Ribbon 是一个基于 HTTP 和 TCP 的客户端负载均衡工具，它基于 Netflix Ribbon 实现。通过 Spring Cloud 的封装，可以让我们轻松地将面向服务的 REST 模版请求自动转换成客户端负载均衡的服务调用。



### 整合ribbon

由于在consumer的pom中已经引入 `spring-cloud-starter-alibaba-nacos-discovery` , 它已经引入ribbon：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    <version>2.2.2.RELEASE</version>
    <scope>compile</scope>
</dependency>
```

所以只需要在 restTemplate 的 bean 中添加 `@LoadBalanced` 注解，即可以使用ribbon

```java
@Configuration
public class ConsumerConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
```

ConsumerController 调用接口，默认采用 **轮询** 方式

```java
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/index")
    public String index() {
        return "consumer远程调用provier：" + this.restTemplate.getForObject("http://provider/index", String.class);
    }
}
```

设置调用方式为 **随机**【只需在yml配置文件中添加已经定好的规则即可】：

```yml
provider:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

设置调用方式为 **Nacos 权重**

```java
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件
    }

    @Override
    public Server choose(Object o) {
        ILoadBalancer loadBalancer = this.getLoadBalancer();
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) loadBalancer;
        //获取要请求的微服务名称
        String name = baseLoadBalancer.getName();
        //获取服务发现的相关API
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        try {
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("选择的实例是port={},instance={}",instance.getPort(),instance);
            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
```





