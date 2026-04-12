package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 删除账号请求
 */
@Data
@ApiModel(description = "删除账号请求")
public class DeleteAccountDTO {

    @ApiModelProperty(value = "用户ID（由后端从Token填入，前端无需传）")
    private Long userId;

    @NotBlank(message = "验证类型不能为空")
    @ApiModelProperty(value = "验证方式：sms=短信验证码，password=密码", required = true)
    private String verifyType;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码或密码（根据verifyType决定含义）", required = true)
    private String code;
}
