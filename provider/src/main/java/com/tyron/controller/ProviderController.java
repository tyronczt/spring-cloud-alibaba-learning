package com.tyron.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.tyron.entity.Order;
import com.tyron.service.ProviderService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description: 服务发现控制器
 * @Author: tyron
 * @Date: Created in 2021/1/18
 */
@RestController
public class ProviderController {

    //SPEL
    @Value("${server.port}")
    private String port;

    @GetMapping("/index")
    public String index() {
        return port;
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }

    @Autowired
    private ProviderService providerService;

    @GetMapping("/provider1")
    public String provider1() {
        return providerService.provider();
    }

    @GetMapping("/provider2")
    public String provider2() {
        return providerService.provider();
    }

    @GetMapping("/hot")
    @SentinelResource("hot")
    public String hot(
            @RequestParam(value = "num1", required = false) Integer num1,
            @RequestParam(value = "num2", required = false) Integer num2) {
        return num1 + "-" + num2;
    }

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/create")
    public Order create() {
        Order order = new Order(
                1,
                "张三",
                "123123",
                "软件园",
                new Date()
        );
        this.rocketMQTemplate.convertAndSend("orderTopic", order);
        return order;
    }

    @GetMapping("/api1/demo1")
    public String demo1() {
        return "/api1/demo1";
    }

    @GetMapping("/api1/demo2")
    public String demo2() {
        return "/api1/demo2";
    }

    @GetMapping("/api2/demo1")
    public String demo3() {
        return "/api2/demo1";
    }

    @GetMapping("/api2/demo2")
    public String demo4() {
        return "/api2/demo2";
    }
}