package com.cy.client.weight.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改密码请求
 */
@Data
@ApiModel(description = "修改密码请求")
public class ChangePasswordDTO {

    @ApiModelProperty(value = "用户ID（由后端从Token填入，前端无需传）")
    private Long userId;

    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$", message = "密码必须为8-20位，且包含字母、数字和特殊字符")
    @ApiModelProperty(value = "新密码，8-20位，必须包含字母、数字和特殊字符", required = true)
    private String newPassword;
}
