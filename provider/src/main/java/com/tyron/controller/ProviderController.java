package com.tyron.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.tyron.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
