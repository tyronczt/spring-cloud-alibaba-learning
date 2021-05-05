package com.tyron.test;

import org.springframework.web.client.RestTemplate;

/**
 * @Description: 模拟访问测试类
 * @Author: tyron
 * @Date: Created in 2021/4/8
 */
public class TestController {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 1000; i++) {
            restTemplate.getForEntity("http://localhost:3333/list",String.class);
            Thread.sleep(200);
        }
    }
}
