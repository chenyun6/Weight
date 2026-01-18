package com.sanzhong.domain.weight.user.login;

import com.common.tools.core.exception.BizException;
import com.sanzhong.common.weight.resultcode.WeightResultCode;
import com.sanzhong.domain.weight.auth.PhoneAuthService;
import com.sanzhong.domain.weight.user.User;
import com.sanzhong.domain.weight.user.UserFactory;
import com.sanzhong.domain.weight.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 登录-指令处理器
 * 支持短信验证码登录方式
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCmdHandler {

    private final UserRepository userRepository;
    private final PhoneAuthService phoneAuthService;
    private final UserFactory userFactory;

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
            throw new BizException(WeightResultCode.LOGIN_PHONE_OR_CODE_ERROR);
        }

        // 查询用户，不存在则创建
        User user = userRepository.findByPhone(cmd.getPhone());
        if (user != null) {
            return user.getId();
        }

        user = userFactory.getInstance(cmd.getPhone());
        user = userRepository.save(user);
        log.info("创建新用户。手机号：{}，用户ID：{}", cmd.getPhone(), user.getId());
        return user.getId();
    }
}
