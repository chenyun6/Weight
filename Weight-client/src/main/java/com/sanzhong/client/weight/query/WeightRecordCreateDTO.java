package com.sanzhong.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 创建体重记录请求
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "创建体重记录请求")
public class WeightRecordCreateDTO {

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotNull(message = "体重类型不能为空")
    @ApiModelProperty(value = "体重类型 1-胖了 2-瘦了", required = true)
    private Integer weightType;
}
