package com.cy.domain.weight.user.sendcode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送验证码-指令
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCodeCmd {

    /** 手机号 */
    private String phone;

    /** IP地址 */
    private String ip;
}
