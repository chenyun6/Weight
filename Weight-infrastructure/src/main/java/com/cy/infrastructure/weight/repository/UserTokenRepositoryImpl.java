package com.cy.infrastructure.weight.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cy.infrastructure.weight.repository.mapper.UserTokenMapper;
import com.cy.infrastructure.weight.repository.model.UserTokenDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 用户Token Repository实现
 *
 * @author visual-ddd
 * @since 1.0
 */
@Repository
public class UserTokenRepositoryImpl {

    @Resource
    private UserTokenMapper userTokenMapper;

    /**
     * 保存或更新Token
     */
    public void saveOrUpdate(UserTokenDO tokenDO) {
        UserTokenDO existing = userTokenMapper.selectByUserId(tokenDO.getUserId());
        if (existing != null) {
            tokenDO.setId(existing.getId());
            userTokenMapper.updateById(tokenDO);
        } else {
            userTokenMapper.insert(tokenDO);
        }
    }

    /**
     * 根据用户ID查询
     */
    public UserTokenDO findByUserId(Long userId) {
        return userTokenMapper.selectByUserId(userId);
    }

    /**
     * 根据AccessToken查询
     */
    public UserTokenDO findByAccessToken(String accessToken) {
        return userTokenMapper.selectByAccessToken(accessToken);
    }

    /**
     * 根据RefreshToken查询
     */
    public UserTokenDO findByRefreshToken(String refreshToken) {
        return userTokenMapper.selectByRefreshToken(refreshToken);
    }

    /**
     * 更新最后使用时间
     */
    public void updateLastUsedTime(Long userId) {
        userTokenMapper.updateLastUsedTime(userId, LocalDateTime.now());
    }

    /**
     * 删除指定天数未使用的Token
     */
    public void deleteUnusedTokens(int days) {
        userTokenMapper.deleteUnusedTokens(days);
    }

    /**
     * 删除用户Token
     */
    public void deleteByUserId(Long userId) {
        userTokenMapper.delete(new LambdaQueryWrapper<UserTokenDO>()
                .eq(UserTokenDO::getUserId, userId));
    }
}
