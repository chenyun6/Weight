package com.sanzhong.domain.weight.auth;

/**
 * 手机号认证服务接口
 * 支持短信验证码验证
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface PhoneAuthService {

    /**
     * 验证手机号和验证码
     * 
     * @param phone 手机号
     * @param code 验证码
     * @return 是否验证成功
     */
    boolean verifyPhone(String phone, String code);
}
