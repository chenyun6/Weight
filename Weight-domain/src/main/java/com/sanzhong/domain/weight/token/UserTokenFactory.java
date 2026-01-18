package com.sanzhong.domain.weight.token;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 用户token-聚合根-工厂
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class UserTokenFactory {

    /**
     * 创建UserToken
     *
     * @param userId 用户ID
     * @param accessToken Access Token
     * @param refreshToken Refresh Token
     * @param expireTime 过期时间
     * @return UserToken
     */
    public UserToken getInstance(Long userId, String accessToken, String refreshToken, LocalDateTime expireTime) {
        UserToken token = new UserToken();
        token.setUserId(userId);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpireTime(expireTime);
        token.setLastUsedTime(LocalDateTime.now());
        return token;
    }
}
