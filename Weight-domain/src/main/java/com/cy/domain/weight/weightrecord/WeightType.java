package com.cy.domain.weight.weightrecord;

import com.common.tools.core.base.BaseEnum;

/**
 * 体重类型-枚举
 *
 * @author visual-ddd
 * @since 1.0
 */
public enum WeightType implements BaseEnum {

    /** 胖了 */
    GAIN(1, "胖了"),

    /** 瘦了 */
    LOSS(2, "瘦了"),
    ;

    private final Integer value;
    private final String desc;

    WeightType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static WeightType fromValue(Integer value) {
        for (WeightType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
