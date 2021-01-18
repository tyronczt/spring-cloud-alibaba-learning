package com.tyron.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
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
}