package com.sanzhong.common.weight.resultcode;


import com.common.tools.core.resultcode.ResultCode;

/**
 * Weight 业务异常返回码
 * 范围：180001-189999
 * 注意：如需范围校验，需要在 ResultCode 的静态块中注册该类
 *
 * @author visual-ddd
 * @since 1.0
 */
public class WeightResultCode extends ResultCode {

    /** 发送验证码过于频繁 */
    public static final WeightResultCode SEND_CODE_TOO_FREQUENT = new WeightResultCode(180001, "发送过于频繁，请%d秒后再试");

    /** 今日发送次数已达上限 */
    public static final WeightResultCode SEND_CODE_REACH_LIMIT = new WeightResultCode(180002, "今日发送次数已达上限，请稍后再试");

    /** IP发送过于频繁 */
    public static final WeightResultCode SEND_CODE_IP_TOO_FREQUENT = new WeightResultCode(180003, "发送过于频繁，请稍后再试");

    /** 手机号或验证码错误 */
    public static final WeightResultCode LOGIN_PHONE_OR_CODE_ERROR = new WeightResultCode(180004, "手机号或验证码错误，请重试");

    /** 体重类型无效 */
    public static final WeightResultCode WEIGHT_TYPE_INVALID = new WeightResultCode(180005, "体重类型无效");

    /** 今天已经记录过了 */
    public static final WeightResultCode WEIGHT_RECORD_ALREADY_EXISTS = new WeightResultCode(180006, "今天已经记录过了，明天再来吧~");

    public WeightResultCode(Integer code, String msg) {
        super(code, msg);
    }
}
