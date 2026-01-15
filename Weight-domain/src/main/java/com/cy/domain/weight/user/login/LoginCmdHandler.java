package com.cy.domain.weight.user.login;

import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.domain.weight.verificationcode.VerificationCode;
import com.cy.domain.weight.verificationcode.VerificationCodeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录-指令处理器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class LoginCmdHandler {

    @Resource
    private UserRepository userRepository;

    @Resource
    private VerificationCodeRepository verificationCodeRepository;

    public Long handle(LoginCmd cmd) {
        // TODO: 开发测试阶段，验证码验证默认通过
        // 后续接入短信服务后，需要恢复验证码验证逻辑
        // 验证验证码
        // VerificationCode verificationCode = verificationCodeRepository.findByPhoneAndCode(cmd.getPhone(), cmd.getCode());
        // if (verificationCode == null) {
        //     throw new RuntimeException("验证码不存在");
        // }
        // if (!verificationCode.isValid(cmd.getCode())) {
        //     throw new RuntimeException("验证码无效或已过期");
        // }
        // 
        // // 标记验证码为已使用
        // verificationCode.markAsUsed();
        // verificationCodeRepository.save(verificationCode);

        // 查询用户，不存在则创建
        User user = userRepository.findByPhone(cmd.getPhone());
        if (user == null) {
            user = User.create(cmd.getPhone());
            user = userRepository.save(user);
        }

        return user.getId();
    }
}
