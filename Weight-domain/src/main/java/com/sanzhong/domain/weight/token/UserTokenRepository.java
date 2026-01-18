package com.sanzhong.domain.weight.token;

/**
 * 用户Token-仓储接口
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface UserTokenRepository {

    /**
     * 保存或更新Token
     *
     * @param token 用户Token
     * @return 用户Token
     */
    UserToken saveOrUpdate(UserToken token);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 用户Token
     */
    UserToken findByUserId(Long userId);

    /**
     * 根据AccessToken查询
     *
     * @param accessToken Access Token
     * @return 用户Token
     */
    UserToken findByAccessToken(String accessToken);

    /**
     * 根据RefreshToken查询
     *
     * @param refreshToken Refresh Token
     * @return 用户Token
     */
    UserToken findByRefreshToken(String refreshToken);

    /**
     * 更新最后使用时间
     *
     * @param userId 用户ID
     */
    void updateLastUsedTime(Long userId);

    /**
     * 删除指定天数未使用的Token
     *
     * @param days 天数
     */
    void deleteUnusedTokens(int days);

    /**
     * 删除用户Token
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);
}
