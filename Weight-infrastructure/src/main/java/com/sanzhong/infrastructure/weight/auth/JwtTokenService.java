package com.sanzhong.infrastructure.weight.auth;

import com.sanzhong.common.weight.constants.TokenConstants;
import com.sanzhong.domain.weight.token.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token服务实现
 * 实现领域层定义的TokenService接口
 * 负责JWT Token的生成、解析和验证
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Service
public class JwtTokenService implements TokenService {

    @Value("${jwt.secret:weighty-secret-key-for-jwt-token-generation-please-change-in-production}")
    private String secret;

    @Value("${jwt.expiration:604800000}") // 7天，单位：毫秒
    private Long expiration;

    @Value("${jwt.refresh-expiration:1209600000}") // 14天，单位：毫秒
    private Long refreshExpiration;

    /**
     * 生成Access Token
     *
     * @param userId 用户ID
     * @param phone 手机号
     * @return Access Token字符串
     */
    @Override
    public String generateAccessToken(Long userId, String phone) {
        Map<String, Object> claims = buildBaseClaims(userId, phone);
        return generateToken(claims, expiration);
    }

    /**
     * 生成Refresh Token
     *
     * @param userId 用户ID
     * @param phone 手机号
     * @return Refresh Token字符串
     */
    @Override
    public String generateRefreshToken(Long userId, String phone) {
        Map<String, Object> claims = buildBaseClaims(userId, phone);
        claims.put(TokenConstants.CLAIM_TYPE, TokenConstants.TOKEN_TYPE_REFRESH);
        return generateToken(claims, refreshExpiration);
    }

    /**
     * 验证Token是否有效
     * 检查Token是否可以正确解析且未过期
     *
     * @param token JWT Token字符串
     * @return true-有效，false-无效
     */
    @Override
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        Claims claims = parseClaimsFromToken(token);
        return claims != null && !isTokenExpired(claims);
    }

    /**
     * 构建基础Claims（包含userId和phone）
     *
     * @param userId 用户ID
     * @param phone 手机号
     * @return Claims Map
     */
    private Map<String, Object> buildBaseClaims(Long userId, String phone) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenConstants.CLAIM_USER_ID, userId);
        claims.put(TokenConstants.CLAIM_PHONE, phone);
        return claims;
    }

    /**
     * 生成JWT Token
     *
     * @param claims Token Claims（包含用户信息等）
     * @param expiration 过期时间（毫秒）
     * @return JWT Token字符串
     */
    private String generateToken(Map<String, Object> claims, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        SecretKey signingKey = getSigningKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 获取JWT签名密钥
     *
     * @return SecretKey
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从Token中解析Claims
     *
     * @param token JWT Token字符串
     * @return Claims，解析失败返回null
     */
    private Claims parseClaimsFromToken(String token) {
        try {
            SecretKey signingKey = getSigningKey();
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("Token解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取用户ID（内部使用，不对外暴露）
     *
     * @param token JWT Token字符串
     * @return 用户ID，解析失败返回null
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        Object userId = claims.get(TokenConstants.CLAIM_USER_ID);
        return convertToLong(userId);
    }

    /**
     * 将Object转换为Long类型
     *
     * @param value 待转换的值
     * @return Long值，转换失败返回null
     */
    private Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        return null;
    }

    /**
     * 检查Token是否已过期
     *
     * @param claims Token Claims
     * @return true-已过期，false-未过期
     */
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    /**
     * 检查是否为Refresh Token
     *
     * @param token JWT Token字符串
     * @return true-是Refresh Token，false-不是
     */
    @Override
    public boolean isRefreshToken(String token) {
        Claims claims = parseClaimsFromToken(token);
        if (claims == null) {
            return false;
        }
        Object type = claims.get(TokenConstants.CLAIM_TYPE);
        return TokenConstants.TOKEN_TYPE_REFRESH.equals(type);
    }
}
