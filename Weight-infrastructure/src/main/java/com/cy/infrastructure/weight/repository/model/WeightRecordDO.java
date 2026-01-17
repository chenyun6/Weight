package com.cy.infrastructure.weight.repository.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 体重记录实体类
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("weight_record")
public class WeightRecordDO extends BaseDO {

    /** 用户ID */
    private Long userId;

    /** 体重类型 */
    private Integer weightType;

    /** 记录日期 */
    private LocalDate recordDate;
}
