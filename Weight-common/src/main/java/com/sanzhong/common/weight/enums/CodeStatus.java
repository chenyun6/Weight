package com.sanzhong.common.weight.enums;

import lombok.Getter;

/**
 * 验证码状态枚举
 *
 * @author visual-ddd
 * @since 1.0
 */
@Getter
public enum CodeStatus {

    /** 未使用 */
    UNUSED(0, "未使用"),

    /** 已使用 */
    USED(1, "已使用"),
    ;

    private final Integer value;
    private final String desc;

    CodeStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 状态值
     * @return 枚举，如果不存在则返回null
     */
    public static CodeStatus fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (CodeStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为已使用状态
     *
     * @param value 状态值
     * @return true-已使用，false-未使用
     */
    public static boolean isUsed(Integer value) {
        return USED.getValue().equals(value);
    }
}
