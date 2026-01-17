package com.cy.domain.weight.user.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录-指令
 * 支持短信验证码登录方式
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCmd {

    /** 手机号 */
    private String phone;

    /** 验证码 */
    private String code;

    /** IP地址 */
    private String ip;
}
