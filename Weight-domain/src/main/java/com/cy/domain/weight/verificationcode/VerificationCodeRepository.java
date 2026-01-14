package com.cy.domain.weight.verificationcode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 验证码-仓储接口
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface VerificationCodeRepository {

    /**
     * 保存验证码
     */
    VerificationCode save(VerificationCode verificationCode);

    /**
     * 根据手机号和验证码查询
     */
    VerificationCode findByPhoneAndCode(String phone, String code);

    /**
     * 统计指定时间内的发送次数（防刷）
     */
    Long countByPhoneAndTimeRange(String phone, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定IP在指定时间内的发送次数（防刷）
     */
    Long countByIpAndTimeRange(String ip, LocalDateTime startTime, LocalDateTime endTime);
}
