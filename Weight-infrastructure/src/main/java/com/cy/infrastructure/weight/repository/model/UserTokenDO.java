package com.cy.infrastructure.weight.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户Token DO
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@TableName("user_token")
public class UserTokenDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expireTime;
    private LocalDateTime lastUsedTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
