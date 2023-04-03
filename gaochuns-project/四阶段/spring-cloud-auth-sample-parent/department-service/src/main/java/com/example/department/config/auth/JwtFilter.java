package com.example.department.config.auth;

import com.example.department.util.JwtUtils;
import com.example.department.util.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper defaultMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("x-jwt-token");

        // 请求头中没有携带 jwtToken，无条件放行。
        if (StringUtils.isEmpty(jwtToken)) {
            filterChain.doFilter(request, response);
            return;
        }

//        String username = JwtUtils.getUsernameFromJWT(jwtToken);
        String username = request.getHeader("x-username");
        Boolean b = stringRedisTemplate.delete("user:jwt:" + username);

        // jwt-token 是伪造的，或者是过期了。要求用户重新登录
        if (!b) {
            ResponseResult<Void> responseResult = new ResponseResult<>(400, "jwt-token 非法，请重新的登录");
            String jsonStr = defaultMapper.writeValueAsString(responseResult);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonStr);
            return;
        }

        // 取当前 jwt-token 所代表的这个用户所具有的所有的权限（在 Redis 中）
        String authorityStr = stringRedisTemplate.opsForValue().get("user:authorities:" + username);
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorityStr);

        // 向 spring-security 上下文中存入 authentication-token
        Authentication token = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);

        // jwt-token 再存一次到 Redis
        stringRedisTemplate.opsForValue().set("user:jwt:" + username, jwtToken, 30, TimeUnit.MINUTES);
        stringRedisTemplate.expire("user:authorities:" + username, 30, TimeUnit.MINUTES);

        filterChain.doFilter(request, response);
    }

}
