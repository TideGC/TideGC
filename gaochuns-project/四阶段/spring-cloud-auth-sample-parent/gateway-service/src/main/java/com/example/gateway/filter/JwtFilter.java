package com.example.gateway.filter;

import com.example.gateway.util.JwtUtils;
import com.example.gateway.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * gateway 网关技术层面上的特殊性：
 *
 *  1. 其它的微服务的项目直接引入的包是 spring-web；
 *      但是网关（作为项目中的一个微服务）它间接引入的包是 spring-webflux
 *
 *  2. 其它的微服务可以直接或使用 jdbc 操作数据库；
 *      但是网关这里最好不要，会影响网关的性能（因为，jdbc 暂时还不支持响应式编程，即，无法配合发会 webflux 全部的能力）
 *
 *  3. 其它的微服务的项目操作 redis ，引入的包是 spring-data-redis；
 *      但是网关这里最好不要，也是性能问题。但是，好在 redis 有spring-data-reactive-redis 能配合 webflux 发会全部能力。
 *
 *  为了能够在面试的时候有个话题，所以我们是各个微服务自己在做鉴权，网关处就没有做太多的工作。
 *  在这里，我们做了一个“实际价值”不太大工作，从请求头中所携带的 jwt-token 中“抠”出了 username ，再添加到请求头中。
 *  这样，“后面”的各个微服务的 JwtFilter 就直接通过 request.getHeader("x-username") 就能获得当前用户名。
 *
 */
@Component
@RequiredArgsConstructor
public class JwtFilter implements GlobalFilter {

    private final ObjectMapper defaultMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        Mono<Void> mono = null;

        // 请求头中没有 x-jwt-token ，直接放行
        List<String> stringList = request.getHeaders().get("x-jwt-token");
        if (stringList == null || stringList.isEmpty()) {
            return chain.filter(exchange);
        }

        String jwtToken = stringList.get(0);

        try {
            String username = JwtUtils.getUsernameFromJWT(jwtToken);
            // webflux 的特殊性：不能直接修改原 Request 对象，只能基于它再生成一个新的 Request 对象用。
            request = request.mutate().header("x-username", username).build();
            return chain.filter(exchange);
        } catch (RuntimeException e) {
            return responseError(response, 400, "failure", "jwt-token error");
        }

    }

    /**
     *  响应错误信息
     */
    private Mono<Void> responseError(ServerHttpResponse response, int code, String msg, String data) {
        ResponseResult<String> responseResult = new ResponseResult<>(code, msg, data);

        String jsonStr = "{\"status\":\"-1\", \"msg\":\"error\"}";

        try {
            jsonStr = defaultMapper.writeValueAsString(responseResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        }

    }
}


