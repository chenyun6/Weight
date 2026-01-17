package com.cy.app.weight.auth;

import com.common.tools.core.exception.BizException;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.infrastructure.weight.auth.TokenService;
import com.cy.infrastructure.weight.repository.UserTokenRepositoryImpl;
import com.cy.infrastructure.weight.repository.model.UserTokenDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 认证服务
 *
 * @author visual-ddd
 * @since 1.0
 */
@Service
public class AuthService {

    @Resource
    private TokenService tokenService;

    @Resource
    private UserTokenRepositoryImpl userTokenRepository;

    @Resource
    private UserRepository userRepository;

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
        UserTokenDO tokenDO = new UserTokenDO();
        tokenDO.setUserId(userId);
        tokenDO.setAccessToken(accessToken);
        tokenDO.setRefreshToken(refreshToken);
        tokenDO.setExpireTime(expireTime);
        tokenDO.setLastUsedTime(LocalDateTime.now());

        userTokenRepository.saveOrUpdate(tokenDO);

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
        UserTokenDO tokenDO = userTokenRepository.findByRefreshToken(refreshToken);
        if (tokenDO == null) {
            throw new BizException("Token不存在");
        }

        // 检查是否7天未使用
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        if (tokenDO.getLastUsedTime().isBefore(sevenDaysAgo)) {
            // 删除过期Token
            userTokenRepository.deleteByUserId(tokenDO.getUserId());
            throw new BizException("Token已过期，请重新登录");
        }

        // 生成新Token
        return generateToken(tokenDO.getUserId());
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

        UserTokenDO tokenDO = userTokenRepository.findByAccessToken(accessToken);
        if (tokenDO == null) {
            return null;
        }

        // 检查是否7天未使用
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        if (tokenDO.getLastUsedTime().isBefore(sevenDaysAgo)) {
            userTokenRepository.deleteByUserId(tokenDO.getUserId());
            return null;
        }

        // 更新最后使用时间
        userTokenRepository.updateLastUsedTime(tokenDO.getUserId());

        return tokenDO.getUserId();
    }
}
