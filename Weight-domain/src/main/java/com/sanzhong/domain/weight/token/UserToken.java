package com.sanzhong.domain.weight.token;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户Token-实体
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
public class UserToken {

    /** ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** Access Token */
    private String accessToken;

    /** Refresh Token */
    private String refreshToken;

    /** 过期时间 */
    private LocalDateTime expireTime;

    /** 最后使用时间 */
    private LocalDateTime lastUsedTime;

    /**
     * 检查Token是否已过期（7天未使用）
     *
     * @return true-已过期，false-未过期
     */
    public boolean isExpired() {
        if (lastUsedTime == null) {
            return true;
        }
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return lastUsedTime.isBefore(sevenDaysAgo);
    }

    /**
     * 更新最后使用时间
     */
    public void updateLastUsedTime() {
        this.lastUsedTime = LocalDateTime.now();
    }
}
