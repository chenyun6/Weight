package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 刷新Token请求
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "刷新Token请求")
public class RefreshTokenDTO {

    @NotBlank(message = "RefreshToken不能为空")
    @ApiModelProperty(value = "RefreshToken", required = true)
    private String refreshToken;
}
