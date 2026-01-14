package com.cy.client.weight.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体重记录数据传输对象
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "体重记录数据传输对象")
public class WeightRecordDTO {

    @ApiModelProperty(value = "记录ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "体重类型 1-胖了 2-瘦了")
    private Integer weightType;

    @ApiModelProperty(value = "记录日期")
    private LocalDate recordDate;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
