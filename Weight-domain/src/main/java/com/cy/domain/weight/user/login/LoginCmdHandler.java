package com.cy.domain.weight.user.login;

import com.cy.domain.weight.auth.PhoneAuthService;
import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录-指令处理器
 * 支持短信验证码登录方式
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
public class LoginCmdHandler {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PhoneAuthService phoneAuthService;

    /**
     * 处理登录指令
     * 
     * @param cmd 登录指令
     * @return 用户ID
     */
    public Long handle(LoginCmd cmd) {
        // 验证手机号和验证码
        boolean verified = phoneAuthService.verifyPhone(cmd.getPhone(), cmd.getCode());
        
        if (!verified) {
            throw new RuntimeException("手机号或验证码错误，请重试");
        }

        // 查询用户，不存在则创建
        User user = userRepository.findByPhone(cmd.getPhone());
        if (user == null) {
            user = User.create(cmd.getPhone());
            user = userRepository.save(user);
            log.info("创建新用户。手机号：{}，用户ID：{}", cmd.getPhone(), user.getId());
        }

        return user.getId();
    }
}
