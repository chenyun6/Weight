package com.cy.client.weight.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "登录响应")
public class LoginResponseDTO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "访问Token")
    private String accessToken;

    @ApiModelProperty(value = "刷新Token")
    private String refreshToken;

    @ApiModelProperty(value = "Token过期时间（毫秒时间戳）")
    private Long expireTime;
}
