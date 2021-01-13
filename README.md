## Spring Cloud Alibaba 学习

> 学习视频（B站）：https://www.bilibili.com/video/BV1Mt4y1i7JW

#### 学习计划

1. **Nacos 服务注册**
2. **Nacos 服务发现**
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

#### 一、Nacos 服务注册

> 官网安装包下载地址：https://github.com/alibaba/nacos/releases/tag/1.2.1
>
> 免费下载地址：https://download.csdn.net/download/tian330726/14150203

此处使用 Windows 环境，解压zip文件，进入bin目录，双击 `startup.cmd` ，会弹出命令框启动nacos程序，如果弹出后立即消失，**记得检查下 JAVA_HOME 是否配置！**

此时访问地址：http://localhost:8848/nacos

![nacos-init](https://raw.githubusercontent.com/tyronczt/spring-cloud-alibaba-learning/master/picture/nacos-init.png)

