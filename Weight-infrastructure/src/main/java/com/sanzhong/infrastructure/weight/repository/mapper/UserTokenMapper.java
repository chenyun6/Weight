package com.sanzhong.infrastructure.weight.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sanzhong.infrastructure.weight.repository.model.UserTokenDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 用户Token Mapper
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface UserTokenMapper extends BaseMapper<UserTokenDO> {
    
    /**
     * 根据用户ID查询Token
     */
    UserTokenDO selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据AccessToken查询
     */
    UserTokenDO selectByAccessToken(@Param("accessToken") String accessToken);
    
    /**
     * 根据RefreshToken查询
     */
    UserTokenDO selectByRefreshToken(@Param("refreshToken") String refreshToken);
    
    /**
     * 更新最后使用时间
     */
    int updateLastUsedTime(@Param("userId") Long userId, @Param("lastUsedTime") LocalDateTime lastUsedTime);
    
    /**
     * 删除7天未使用的Token
     */
    int deleteUnusedTokens(@Param("days") int days);
}
