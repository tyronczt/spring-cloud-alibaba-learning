package com.tyron.provider.configuration;

import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.tyron.provider.handler.ExceptionHandler;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description: SentinelConfiguration
 * @Author: tyron
 * @Date: Created in 2021/5/5
 */
@Configuration
public class SentinelConfiguration {

    @PostConstruct
    public void init() {
        WebCallbackManager.setUrlBlockHandler(new ExceptionHandler());
        WebCallbackManager.setRequestOriginParser(new RequestOriginParserDefinition());
    }
}
