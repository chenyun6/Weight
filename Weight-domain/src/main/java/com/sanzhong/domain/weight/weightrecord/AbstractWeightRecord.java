package com.sanzhong.domain.weight.weightrecord;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体重记录-聚合根
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
public abstract class AbstractWeightRecord {

    /** 记录ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 体重类型 */
    private WeightType weightType;

    /** 记录日期 */
    private LocalDate recordDate;

    /** 创建时间 */
    private LocalDateTime createTime;

    /**
     * 检查是否可以记录
     * 用户自然日只能点击一次
     */
    public abstract Boolean canRecord(LocalDate date);
}
