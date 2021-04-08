package com.tyron.provider.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @Description: service层接口
 * @Author: tyron
 * @Date: Created in 2021/4/8
 */
@Service
public class ProviderService {

    @SentinelResource("provider")
    public String provider(){
        return "ProviderService";
    }
}
