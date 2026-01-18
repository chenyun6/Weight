package com.sanzhong.domain.weight.weightrecord.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建体重记录-指令
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightRecordCreateCmd {

    /** 用户ID */
    private Long userId;

    /** 体重类型 1-胖了 2-瘦了 */
    private Integer weightType;
}
