package com.cy.infrastructure.weight.auth;

import com.common.tools.core.exception.BizException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 密码加密与验证服务
 *
 * @author visual-ddd
 * @since 1.0
 */
@Service
public class PasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 校验密码复杂度（新密码规则）
     */
    public void validateComplexity(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8 || rawPassword.length() > 20) {
            throw new BizException("密码必须为8-20位");
        }
        // 检查连续3个重复字符（如 aaa、111、!!!）
        for (int i = 0; i < rawPassword.length() - 2; i++) {
            char c = rawPassword.charAt(i);
            if (c == rawPassword.charAt(i + 1) && c == rawPassword.charAt(i + 2)) {
                throw new BizException("密码不能包含3个连续相同的字符");
            }
        }
    }

    /**
     * 加密密码（BCrypt）
     */
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     */
    public boolean matches(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }
}
