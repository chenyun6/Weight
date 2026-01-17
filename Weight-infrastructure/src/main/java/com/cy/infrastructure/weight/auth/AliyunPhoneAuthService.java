package com.cy.infrastructure.weight.auth;

import com.cy.domain.weight.auth.PhoneAuthService;
import com.cy.domain.weight.sms.SmsService;
import com.cy.domain.weight.verificationcode.VerificationCode;
import com.cy.domain.weight.verificationcode.VerificationCodeRepository;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 手机号认证服务实现
 * 实现领域层定义的PhoneAuthService接口
 * 支持短信验证码验证，优先使用阿里云CheckSmsVerifyCode接口核验
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Service
public class AliyunPhoneAuthService implements PhoneAuthService {

    @Resource
    private VerificationCodeRepository verificationCodeRepository;

    @Resource
    private SmsService smsService;

    @Value("${sms.enabled:false}")
    private boolean smsEnabled;

    /**
     * 验证手机号和验证码
     * 优先使用阿里云CheckSmsVerifyCode接口核验，失败则回退到本地数据库验证
     * 包括测试模式（固定验证码"111111"）
     *
     * @param phone 手机号
     * @param code 验证码
     * @return 是否验证成功
     */
    @Override
    public boolean verifyPhone(String phone, String code) {
        if (StrUtil.isBlank(code)) {
            log.warn("短信验证码为空。手机号：{}", phone);
            return false;
        }

        // 优先使用阿里云CheckSmsVerifyCode接口核验（如果已开启）
        if (smsEnabled && smsService != null) {
            boolean verified = smsService.verifyCode(phone, code);
            if (verified) {
                // 如果阿里云核验成功，标记本地验证码为已使用（如果存在）
                VerificationCode verificationCode = verificationCodeRepository.findByPhoneAndCode(phone, code);
                if (verificationCode != null && verificationCode.getUsed() == 0) {
                    verificationCode.markAsUsed();
                    verificationCodeRepository.save(verificationCode);
                }
                return true;
            } else {
                log.warn("阿里云验证码核验失败，回退到本地验证。手机号：{}", phone);
            }
        }

        // 回退到本地数据库验证（兼容旧方式）
        VerificationCode verificationCode = verificationCodeRepository.findByPhoneAndCode(phone, code);
        if (verificationCode == null) {
            log.warn("验证码不存在。手机号：{}，验证码：{}", phone, code);
            return false;
        }

        // 验证验证码有效性
        if (!verificationCode.isValid(code)) {
            if (verificationCode.getUsed() != null && verificationCode.getUsed() == 1) {
                log.warn("验证码已使用。手机号：{}", phone);
            } else {
                log.warn("验证码无效或已过期。手机号：{}", phone);
            }
            return false;
        }

        // 标记验证码为已使用
        verificationCode.markAsUsed();
        verificationCodeRepository.save(verificationCode);
        log.info("本地验证码验证成功。手机号：{}", phone);
        return true;
    }
}
