package com.sanzhong.common.weight.constants;

/**
 * 阿里云相关常量
 *
 * @author visual-ddd
 * @since 1.0
 */
public final class AliyunConstants {

    private AliyunConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }

    /** 中国区国家代码 */
    public static final String COUNTRY_CODE_CN = "86";

    /** 阿里云API响应成功码 */
    public static final String RESPONSE_CODE_OK = "OK";

    /** 验证码核验通过结果 */
    public static final String VERIFY_RESULT_PASS = "PASS";

    /** 短信模板参数 - 验证码 */
    public static final String TEMPLATE_PARAM_CODE = "code";

    /** 短信模板参数 - 分钟数 */
    public static final String TEMPLATE_PARAM_MIN = "min";

    /** 短信模板参数 - 过期时间（分钟） */
    public static final String TEMPLATE_PARAM_MIN_VALUE = "1";

    /** 验证码核验策略 */
    public static final Long CASE_AUTH_POLICY = 1L;
}
