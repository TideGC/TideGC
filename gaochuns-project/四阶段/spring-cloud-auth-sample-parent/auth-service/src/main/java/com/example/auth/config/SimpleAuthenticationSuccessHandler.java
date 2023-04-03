package com.example.auth.config;

import com.example.auth.util.JwtUtils;
import com.example.auth.util.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper defaultMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        // 生成 jwt-token
        String jwt = JwtUtils.createJWT(user.getUsername());
        ResponseResult<String> responseResult = new ResponseResult<>(200, "success", jwt);

        // 将 jwt-token 存入 redis-sever 中，并设置30分钟超时
        String key = "user:jwt:" + user.getUsername();
        String val = jwt;
        stringRedisTemplate.opsForValue().set(key, val, 30, TimeUnit.MINUTES);

        // 对象的集合转成 逗号分隔的字符串
        key = "user:authorities:" + user.getUsername();
        val = StringUtils.collectionToCommaDelimitedString(user.getAuthorities());
        stringRedisTemplate.opsForValue().set(key, val, 30, TimeUnit.MINUTES);

        // http 响应返回
        String jsonStr = defaultMapper.writeValueAsString(responseResult);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(jsonStr);
    }


}
