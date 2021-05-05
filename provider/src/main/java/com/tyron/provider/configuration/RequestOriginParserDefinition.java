package com.tyron.provider.configuration;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
/**
 * @Description: RequestOriginParserDefinition
 * @Author: tyron
 * @Date: Created in 2021/5/5
 */

public class RequestOriginParserDefinition implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String name = httpServletRequest.getParameter("name");
        if(StringUtils.isEmpty(name)){
            throw new RuntimeException("name is null");
        }
        return name;
    }
}