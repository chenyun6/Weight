package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 体重记录列表查询DTO
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "体重记录列表查询")
public class WeightRecordListQuery {

    @ApiModelProperty(value = "用户ID（由后端从Token填入，前端无需传）")
    private Long userId;

    @ApiModelProperty(value = "起始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;
}
