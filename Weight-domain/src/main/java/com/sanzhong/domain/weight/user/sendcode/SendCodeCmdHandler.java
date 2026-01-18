package com.sanzhong.domain.weight.user.sendcode;

import com.common.tools.core.exception.BizException;
import com.sanzhong.common.weight.constants.CodeConstants;
import com.sanzhong.common.weight.enums.CodeStatus;
import com.sanzhong.common.weight.resultcode.WeightResultCode;
import com.sanzhong.domain.weight.sms.SmsService;
import com.sanzhong.domain.weight.user.UserRepository;
import com.sanzhong.domain.weight.verificationcode.VerificationCode;
import com.sanzhong.domain.weight.verificationcode.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 发送验证码-指令处理器
 * 负责处理发送验证码的业务逻辑，包括防刷校验、验证码生成、短信发送等
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SendCodeCmdHandler {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final SmsService smsService;

    @Value("${sms.enabled:false}")
    private boolean smsEnabled;

    /**
     * 处理发送验证码指令
     *
     * @param cmd 发送验证码指令
     * @return 生成的验证码
     */
    public String handle(SendCodeCmd cmd) {
        LocalDateTime now = LocalDateTime.now();
        
        // 防刷校验
        validateAntiBrush(cmd, now);
        
        // 生成验证码
        String code = generateVerificationCode(cmd.getPhone());
        
        // 保存验证码到本地数据库（用于兼容和测试模式）
        saveVerificationCode(cmd, code, now);
        
        // 发送短信（如果开启）
        sendSmsIfEnabled(cmd.getPhone(), code);
        
        return code;
    }

    /**
     * 防刷校验
     * 检查发送频率限制，包括时间间隔、手机号限流、IP限流
     *
     * @param cmd 发送验证码指令
     * @param now 当前时间
     */
    private void validateAntiBrush(SendCodeCmd cmd, LocalDateTime now) {
        // 检查发送时间间隔
        validateSendInterval(cmd.getPhone(), now);
        
        // 检查手机号发送次数
        validatePhoneSendCount(cmd.getPhone(), now);
        
        // 检查IP发送次数
        if (cmd.getIp() != null) {
            validateIpSendCount(cmd.getIp(), now);
        }
    }

    /**
     * 校验发送时间间隔
     * 同一手机号60秒内不能重复发送
     *
     * @param phone 手机号
     * @param now   当前时间
     */
    private void validateSendInterval(String phone, LocalDateTime now) {
        VerificationCode lastCode = verificationCodeRepository.findLatestByPhone(phone);
        if (lastCode == null || lastCode.getSendTime() == null) {
            return;
        }
        
        LocalDateTime oneMinuteAgo = now.minusSeconds(CodeConstants.MIN_SEND_INTERVAL_SECONDS);
        if (lastCode.getSendTime().isAfter(oneMinuteAgo)) {
            long remainingSeconds = CodeConstants.MIN_SEND_INTERVAL_SECONDS - 
                Duration.between(lastCode.getSendTime(), now).getSeconds();
            throw new BizException(WeightResultCode.SEND_CODE_TOO_FREQUENT, 
                String.format(WeightResultCode.SEND_CODE_TOO_FREQUENT.getMsg(), remainingSeconds));
        }
    }

    /**
     * 校验手机号发送次数
     * 同一手机号1小时内最多发送5次
     *
     * @param phone 手机号
     * @param now   当前时间
     */
    private void validatePhoneSendCount(String phone, LocalDateTime now) {
        LocalDateTime oneHourAgo = now.minusHours(1);
        Long phoneCount = verificationCodeRepository.countByPhoneAndTimeRange(phone, oneHourAgo, now);
        if (phoneCount >= CodeConstants.MAX_SEND_COUNT_PER_HOUR) {
            throw new BizException(WeightResultCode.SEND_CODE_REACH_LIMIT);
        }
    }

    /**
     * 校验IP发送次数
     * 同一IP1小时内最多发送10次
     *
     * @param ip  IP地址
     * @param now 当前时间
     */
    private void validateIpSendCount(String ip, LocalDateTime now) {
        LocalDateTime oneHourAgo = now.minusHours(1);
        Long ipCount = verificationCodeRepository.countByIpAndTimeRange(ip, oneHourAgo, now);
        if (ipCount >= CodeConstants.MAX_SEND_COUNT_PER_IP_PER_HOUR) {
            throw new BizException(WeightResultCode.SEND_CODE_IP_TOO_FREQUENT);
        }
    }

    /**
     * 生成验证码
     * 根据短信开关决定使用随机验证码还是固定验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    private String generateVerificationCode(String phone) {
        if (smsEnabled) {
            String code = generateRandomCode();
            log.debug("生成随机验证码。手机号：{}", phone);
            return code;
        } else {
            log.info("短信服务未开启，使用固定验证码。手机号：{}，验证码：{}", phone, CodeConstants.FIXED_CODE);
            return CodeConstants.FIXED_CODE;
        }
    }

    /**
     * 生成随机6位数字验证码
     *
     * @return 6位数字验证码
     */
    private String generateRandomCode() {
        Random random = new Random();
        int code = random.nextInt(CodeConstants.RANDOM_CODE_RANGE) + CodeConstants.RANDOM_CODE_MIN;
        return String.valueOf(code);
    }

    /**
     * 保存验证码到数据库
     *
     * @param cmd  发送验证码指令
     * @param code 验证码
     * @param now  当前时间
     */
    private void saveVerificationCode(SendCodeCmd cmd, String code, LocalDateTime now) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(cmd.getPhone());
        verificationCode.setCode(code);
        verificationCode.setExpireTime(now.plusMinutes(CodeConstants.CODE_EXPIRE_MINUTES));
        verificationCode.setSendTime(now);
        verificationCode.setIp(cmd.getIp());
        verificationCode.setUsed(CodeStatus.UNUSED.getValue());
        verificationCodeRepository.save(verificationCode);
        log.debug("验证码已保存。手机号：{}", cmd.getPhone());
    }

    /**
     * 如果短信服务开启，则发送短信
     *
     * @param phone 手机号
     * @param code  验证码
     */
    private void sendSmsIfEnabled(String phone, String code) {
        if (!smsEnabled) {
            return;
        }
        
        boolean sendSuccess = smsService.sendVerificationCode(phone, code);
        if (!sendSuccess) {
            log.warn("短信发送失败，但验证码已保存。手机号：{}，验证码：{}", phone, code);
            // 短信发送失败不影响验证码保存，用户可以通过其他方式获取验证码（如客服）
        } else {
            log.info("短信发送成功。手机号：{}", phone);
        }
    }

}
