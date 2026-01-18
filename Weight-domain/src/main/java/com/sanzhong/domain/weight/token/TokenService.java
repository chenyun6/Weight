package com.sanzhong.domain.weight.token;

/**
 * Token服务接口
 * 负责Token的生成和验证
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface TokenService {

    /**
     * 生成Access Token
     *
     * @param userId 用户ID
     * @param phone 手机号
     * @return Access Token
     */
    String generateAccessToken(Long userId, String phone);

    /**
     * 生成Refresh Token
     *
     * @param userId 用户ID
     * @param phone 手机号
     * @return Refresh Token
     */
    String generateRefreshToken(Long userId, String phone);

    /**
     * 验证Token是否有效
     *
     * @param token Token
     * @return true-有效，false-无效
     */
    boolean validateToken(String token);

    /**
     * 检查是否为Refresh Token
     *
     * @param token Token
     * @return true-是Refresh Token，false-不是
     */
    boolean isRefreshToken(String token);
}
