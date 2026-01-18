package com.sanzhong.common.weight.constants;

/**
 * 验证码相关常量
 *
 * @author visual-ddd
 * @since 1.0
 */
public final class CodeConstants {

    private CodeConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }

    /** 验证码有效期（分钟） */
    public static final int CODE_EXPIRE_MINUTES = 5;

    /** 短信未开启时使用的固定验证码 */
    public static final String FIXED_CODE = "111111";

    /** 同一手机号1小时内最多发送次数 */
    public static final int MAX_SEND_COUNT_PER_HOUR = 5;

    /** 同一IP1小时内最多发送次数 */
    public static final int MAX_SEND_COUNT_PER_IP_PER_HOUR = 10;

    /** 最短发送间隔（秒） */
    public static final int MIN_SEND_INTERVAL_SECONDS = 60;

    /** 随机验证码最小值 */
    public static final int RANDOM_CODE_MIN = 100000;

    /** 随机验证码最大值 */
    public static final int RANDOM_CODE_MAX = 999999;

    /** 随机验证码范围（用于生成随机数） */
    public static final int RANDOM_CODE_RANGE = RANDOM_CODE_MAX - RANDOM_CODE_MIN + 1;
}
