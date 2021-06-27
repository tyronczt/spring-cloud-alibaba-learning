package com.tyron.configuration;

import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.tyron.handler.ExceptionHandler;

import javax.annotation.PostConstruct;

/**
 * @Description: SentinelConfiguration
 * @Author: tyron
 * @Date: Created in 2021/5/5
 */
//@Configuration
public class SentinelConfiguration {

    @PostConstruct
    public void init() {
        WebCallbackManager.setUrlBlockHandler(new ExceptionHandler());
        WebCallbackManager.setRequestOriginParser(new RequestOriginParserDefinition());
    }
}
