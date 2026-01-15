package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检查今天是否已记录请求
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "检查今天是否已记录请求")
public class CheckTodayRecordDTO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;
}
