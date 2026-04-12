package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息DTO
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "用户信息")
public class UserProfileDTO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "手机号（脱敏）")
    private String phone;

    @ApiModelProperty(value = "是否已设置密码")
    private Boolean hasPassword;
}
