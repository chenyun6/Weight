package com.sanzhong.domain.weight.sms;

/**
 * 短信服务接口
 * 领域层定义接口，基础设施层实现
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface SmsService {

    /**
     * 发送验证码短信
     * 使用阿里云号码认证服务的SendSmsVerifyCode接口发送验证码
     *
     * @param phone 手机号
     * @param code  验证码（可选，如果为null则使用动态生成验证码）
     * @return 是否发送成功
     */
    boolean sendVerificationCode(String phone, String code);

    /**
     * 核验验证码
     * 使用阿里云号码认证服务的CheckSmsVerifyCode接口核验验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 是否核验成功
     */
    boolean verifyCode(String phone, String code);
}
