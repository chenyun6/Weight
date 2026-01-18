package com.sanzhong.infrastructure.weight.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 验证码实体类
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@TableName("verification_code")
public class VerificationCodeDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号 */
    private String phone;

    /** 验证码 */
    private String code;

    /** 过期时间 */
    private LocalDateTime expireTime;

    /** 发送时间 */
    private LocalDateTime sendTime;

    /** IP地址 */
    private String ip;

    /** 是否已使用 0-未使用 1-已使用 */
    private Integer used;
}
