package com.example.gateway.fileter;

import com.example.util.JwtUtils;
import com.example.util.MapUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.*;
import org.springframework.http.server.reactive.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@Component // 只要它是 spring ioc 容器中的一个单例对象，它就对所有请求有效。
public class SimpleGlobalFilter implements GlobalFilter {

    @Resource
    private ObjectMapper defaultObjectMapper;

    @Resource(name = "reactiveStringRedisTemplate") // 因为想头一点点懒，这里需要指定 bean 的名字。
    private ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // @todo 准备工作
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        LinkedHashSet<URI> urlSet = exchange.getAttribute("org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayOriginalRequestUrl");
        URI[] urls = urlSet.toArray(new URI[0]);
        // 获取当前请求的请求路径。例如，/department-service/delete
        String uri = machiningUri(urls[0].getPath());    // 处理一下路径中的 ID 转 * 的问题
        // 获取当前请求的请求方法：GET | POST
        String method = exchange.getRequest().getMethodValue();
        // 拼接 method + uri 作为 redis 查询的 key 。
        String key = method + ":" + uri;
        // 获取请求投中的 jwt-token
        List<String> stringList = request.getHeaders().get("x-jwt-token");

        System.out.println("正在访问路径：" + key);
        // @todo 判断一：所访问路径是否在表中。
        Mono<Boolean> booleanMono = redisTemplate.hasKey(key);

        return booleanMono.flatMap(exist -> {
            if (!exist) {
                System.out.println("判断一：所访问路径无权限要求，所以放行。");
                return chain.filter(exchange);
            } else {
                // @todo 判断二：请求头中是否有 jwt-token。
                if (Objects.isNull(stringList)) {
                    System.out.println("判断二：未携带 jwt-token，所以拒绝。");
                    return responseError("10000", "请登陆后重试", exchange);
                }

                // @todo 判断三：jwt-token 是否合法，是否是伪造的
                if (!JwtUtils.verify(stringList.get(0))) {
                    System.out.println("判断三：jwt-token 是伪造的，所以拒绝。");
                    return responseError("10000", "请登陆后重试", exchange);
                }

                // @todo 判断四：权限是否足够访问。
                // 当前用户所拥有的权限
                String hasAuthorities = JwtUtils.getAuthoritiesFromJWT(stringList.get(0));
                Set<String> hasAuthoritySet = StringUtils.commaDelimitedListToSet(hasAuthorities);
                System.out.println("当前用户所具有的所有权限：" + hasAuthorities);

                // 当前路径所必要的权限。
                Mono<String> authoritiesMono = redisTemplate.opsForValue().get(key);

                return authoritiesMono.flatMap(requiredAuthorities -> {
                    Set<String> requiredAuthoritySet = StringUtils.commaDelimitedListToSet(requiredAuthorities);
                    System.out.println("当路径所要求的权限是：" + requiredAuthorities + " 之一");
                    for (String authority : hasAuthoritySet) {
                        if (requiredAuthoritySet.contains(authority)) {
                            System.out.println("判断四：拥有要求权限之一，所以放行。");
                            return chain.filter(exchange);
                        }
                    }

                    System.out.println("判断四：未能拥有要求权限，所以拒绝。");
                    return responseError("10000", "权限不够", exchange);
                });
            }
        });

    }

    @SneakyThrows
    private Mono<Void> responseError(String code, String msg, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        Map<String, String> map = MapUtils.of("code", code, "msg", msg);
        String jsonStr = defaultObjectMapper.writeValueAsString(map);

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    private String machiningUri(String uri) {
        String[] parts = StringUtils.delimitedListToStringArray(uri, "/");

        return Arrays.stream(parts).map(s -> {
            try {
                Long.parseLong(s);
            } catch (NumberFormatException e) {
                return s;
            }
            return "*";
        }).collect(Collectors.joining("/"));
    }

}
