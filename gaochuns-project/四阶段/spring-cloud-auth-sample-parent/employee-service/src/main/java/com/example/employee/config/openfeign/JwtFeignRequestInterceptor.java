package com.example.employee.config.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

public class JwtFeignRequestInterceptor implements RequestInterceptor {

    /**
     * 本项目中通过 OpenFeign 发出请求之前，都会执行下列的代码。
     * 典型的使用场景就是为发出去的 HTTP 请求添加请求头
     */
    @Override
    public void apply(RequestTemplate template) {

        // 获取当前请求的所有请求头
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();

        // 有可能，当前请求没有请求头
        if (headerNames == null)
            return;

        // 循环遍历当前请求的所有请求头，将"我"收到的请求头，再在发送 OpenFeign 的时候发给我的"下家"
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String values = request.getHeader(name);
            if (Objects.equals("x-jwt-token", name) || Objects.equals("x-username", name)) {
                template.header(name, values);
            }
        }

    }

}
