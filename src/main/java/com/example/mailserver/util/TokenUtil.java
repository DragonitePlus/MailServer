package com.example.mailserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Base64;

@Component
public class TokenUtil {

    private static final String SECRET_KEY = "yourSecretKey"; // 请使用更安全的方式管理密钥
    private static final long EXPIRATION_TIME = 864000000; // 10 day in milliseconds

    /**
     * 生成包含管理员信息的Token
     *
     * @param username 用户名
     * @param isAdmin  是否为管理员
     * @return 生成的JWT Token
     */
    public String generateToken(String username, boolean isAdmin) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .claim("isAdmin", isAdmin) // 添加自定义声明
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .compact();
    }

    /**
     * 解析Token获取Claims
     *
     * @param token JWT Token
     * @return Claims对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
}
