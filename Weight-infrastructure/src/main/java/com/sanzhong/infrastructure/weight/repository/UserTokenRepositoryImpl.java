package com.sanzhong.infrastructure.weight.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sanzhong.domain.weight.token.UserToken;
import com.sanzhong.domain.weight.token.UserTokenRepository;
import com.sanzhong.infrastructure.weight.assembler.UserToken2UserTokenDOConvert;
import com.sanzhong.infrastructure.weight.repository.mapper.UserTokenMapper;
import com.sanzhong.infrastructure.weight.repository.model.UserTokenDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 用户Token Repository实现
 *
 * @author visual-ddd
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class UserTokenRepositoryImpl implements UserTokenRepository {

    private final UserTokenMapper userTokenMapper;

    @Override
    public UserToken saveOrUpdate(UserToken token) {
        UserTokenDO tokenDO = UserToken2UserTokenDOConvert.INSTANCE.domain2DO(token);
        UserTokenDO existing = userTokenMapper.selectByUserId(tokenDO.getUserId());
        if (existing != null) {
            tokenDO.setId(existing.getId());
            userTokenMapper.updateById(tokenDO);
            token.setId(existing.getId());
        } else {
            userTokenMapper.insert(tokenDO);
            token.setId(tokenDO.getId());
        }
        return token;
    }

    @Override
    public UserToken findByUserId(Long userId) {
        UserTokenDO tokenDO = userTokenMapper.selectByUserId(userId);
        if (tokenDO == null) {
            return null;
        }
        return UserToken2UserTokenDOConvert.INSTANCE.do2Domain(tokenDO);
    }

    @Override
    public UserToken findByAccessToken(String accessToken) {
        UserTokenDO tokenDO = userTokenMapper.selectByAccessToken(accessToken);
        if (tokenDO == null) {
            return null;
        }
        return UserToken2UserTokenDOConvert.INSTANCE.do2Domain(tokenDO);
    }

    @Override
    public UserToken findByRefreshToken(String refreshToken) {
        UserTokenDO tokenDO = userTokenMapper.selectByRefreshToken(refreshToken);
        if (tokenDO == null) {
            return null;
        }
        return UserToken2UserTokenDOConvert.INSTANCE.do2Domain(tokenDO);
    }

    @Override
    public void updateLastUsedTime(Long userId) {
        userTokenMapper.updateLastUsedTime(userId, LocalDateTime.now());
    }

    @Override
    public void deleteUnusedTokens(int days) {
        userTokenMapper.deleteUnusedTokens(days);
    }

    @Override
    public void deleteByUserId(Long userId) {
        userTokenMapper.delete(new LambdaQueryWrapper<UserTokenDO>()
                .eq(UserTokenDO::getUserId, userId));
    }
}
