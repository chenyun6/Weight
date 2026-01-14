package com.cy.domain.weight.user.sendcode;

import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.domain.weight.verificationcode.VerificationCode;
import com.cy.domain.weight.verificationcode.VerificationCodeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 发送验证码-指令处理器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class SendCodeCmdHandler {

    @Resource
    private UserRepository userRepository;

    @Resource
    private VerificationCodeRepository verificationCodeRepository;

    private static final int CODE_EXPIRE_MINUTES = 5;
    private static final int MAX_SEND_COUNT_PER_HOUR = 5;
    private static final int MAX_SEND_COUNT_PER_IP_PER_HOUR = 10;

    public String handle(SendCodeCmd cmd) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(1);

        // 防刷：检查手机号发送次数
        Long phoneCount = verificationCodeRepository.countByPhoneAndTimeRange(cmd.getPhone(), oneHourAgo, now);
        if (phoneCount >= MAX_SEND_COUNT_PER_HOUR) {
            throw new RuntimeException("发送过于频繁，请稍后再试");
        }

        // 防刷：检查IP发送次数
        if (cmd.getIp() != null) {
            Long ipCount = verificationCodeRepository.countByIpAndTimeRange(cmd.getIp(), oneHourAgo, now);
            if (ipCount >= MAX_SEND_COUNT_PER_IP_PER_HOUR) {
                throw new RuntimeException("发送过于频繁，请稍后再试");
            }
        }

        // 生成验证码
        String code = generateCode();

        // 保存验证码
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(cmd.getPhone());
        verificationCode.setCode(code);
        verificationCode.setExpireTime(now.plusMinutes(CODE_EXPIRE_MINUTES));
        verificationCode.setSendTime(now);
        verificationCode.setIp(cmd.getIp());
        verificationCode.setUsed(0);
        verificationCodeRepository.save(verificationCode);

        // TODO 发送短信到手机号
        // 这里应该调用短信服务发送验证码

        return code;
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
