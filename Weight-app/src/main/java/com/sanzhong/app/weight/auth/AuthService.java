package com.sanzhong.app.weight.auth;

import com.common.tools.core.exception.BizException;
import com.sanzhong.client.weight.dto.LoginResponseDTO;
import com.sanzhong.domain.weight.token.TokenService;
import com.sanzhong.domain.weight.token.UserToken;
import com.sanzhong.domain.weight.token.UserTokenFactory;
import com.sanzhong.domain.weight.token.UserTokenRepository;
import com.sanzhong.domain.weight.user.User;
import com.sanzhong.domain.weight.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 认证服务
 *
 * @author visual-ddd
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;
    private final UserTokenFactory userTokenFactory;

    /**
     * 生成Token并保存
     */
    public LoginResponseDTO generateToken(Long userId) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 生成Token
        String accessToken = tokenService.generateAccessToken(userId, user.getPhone());
        String refreshToken = tokenService.generateRefreshToken(userId, user.getPhone());

        // 计算过期时间（7天后）
        LocalDateTime expireTime = LocalDateTime.now().plusDays(7);

        // 保存Token到数据库
        UserToken token = userTokenFactory.getInstance(userId, accessToken, refreshToken, expireTime);
        userTokenRepository.saveOrUpdate(token);

        // 构建响应
        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(userId);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpireTime(expireTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        return response;
    }

    /**
     * 刷新Token
     */
    public LoginResponseDTO refreshToken(String refreshToken) {
        // 验证RefreshToken
        if (!tokenService.validateToken(refreshToken) || !tokenService.isRefreshToken(refreshToken)) {
            throw new BizException("无效的RefreshToken");
        }

        // 从数据库查询Token
        UserToken token = userTokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new BizException("Token不存在");
        }

        // 检查是否7天未使用
        if (token.isExpired()) {
            // 删除过期Token
            userTokenRepository.deleteByUserId(token.getUserId());
            throw new BizException("Token已过期，请重新登录");
        }

        // 生成新Token
        return generateToken(token.getUserId());
    }

    /**
     * 更新最后使用时间
     */
    public void updateLastUsedTime(Long userId) {
        userTokenRepository.updateLastUsedTime(userId);
    }

    /**
     * 验证Token并返回用户ID
     */
    public Long validateAndGetUserId(String accessToken) {
        if (!tokenService.validateToken(accessToken)) {
            return null;
        }

        UserToken token = userTokenRepository.findByAccessToken(accessToken);
        if (token == null) {
            return null;
        }

        // 检查是否7天未使用
        if (token.isExpired()) {
            userTokenRepository.deleteByUserId(token.getUserId());
            return null;
        }

        // 更新最后使用时间
        userTokenRepository.updateLastUsedTime(token.getUserId());

        return token.getUserId();
    }
}
