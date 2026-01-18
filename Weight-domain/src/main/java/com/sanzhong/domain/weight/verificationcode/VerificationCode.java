package com.sanzhong.domain.weight.verificationcode;

import com.sanzhong.common.weight.enums.CodeStatus;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 验证码-值对象
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
public class VerificationCode {

    /** ID */
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

    /**
     * 检查验证码是否有效
     */
    public Boolean isValid(String inputCode) {
        if (CodeStatus.isUsed(used)) {
            return false;
        }
        if (LocalDateTime.now().isAfter(expireTime)) {
            return false;
        }
        return code.equals(inputCode);
    }

    /**
     * 标记为已使用
     */
    public void markAsUsed() {
        this.used = CodeStatus.USED.getValue();
    }
}
