package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 新增体重记录请求
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "新增体重记录请求")
public class WeightAddDTO {

    @ApiModelProperty(value = "用户ID（由后端从Token填入，前端无需传）")
    private Long userId;

    @NotNull(message = "体重类型不能为空")
    @ApiModelProperty(value = "体重类型 1-胖了 2-瘦了", required = true)
    private Integer weightType;

    @NotNull(message = "记录日期不能为空")
    @ApiModelProperty(value = "记录日期 yyyy-MM-dd", required = true)
    private LocalDate recordDate;
}
