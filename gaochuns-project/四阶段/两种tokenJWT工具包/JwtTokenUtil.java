package com.woniu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtTokenUtil {
    // 过期时间 30 分钟
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
//        private static final long EXPIRE_TIME = 60 * 1000;
    // jwt 签名密钥
    private static final String SECRET = "woniuxy";

    /**
     * 生产token
     *
     * @param username
     * @return
     */
    public static String createToken(String username) {
        String token = null;
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        try {
            token = JWT.create()
                    .withAudience(username)                     // 将用户名设置到载荷里面
                    .withExpiresAt(expireDate)                  // 设置过期时间
                    //.withClaim("testClaim", "testClaim")          // 载荷其它信息
                    //.withSubject("JWT_token")                     // 项目信息
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (Exception e) {
            log.error("token 生成异常，请重新获取", e);
        }
        return token;
    }

    /**
     * 校验 token
     *
     * @param token
     * @return
     */
    public static boolean checkToke(String token) {
        try {
            JWTVerifier verifier = JWT
                    .require(Algorithm.HMAC256(SECRET))     // 签名加密方式
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("token无效，请重新获取", e);
            return false;
        } catch (Exception e) {
            log.error("token无效，请重新获取", e);
            return false;
        }
    }

    /**
     * 根据token获取username
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            String username = JWT.decode(token).getAudience().get(0);
            return username;
        } catch (JWTDecodeException e) {
            log.error("token异常", e);
            throw e;
        }
    }
}
