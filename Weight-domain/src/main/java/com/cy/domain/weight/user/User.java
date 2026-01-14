package com.cy.domain.weight.user;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户-聚合根
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
public class User {

    /** 用户ID */
    private Long id;

    /** 手机号 */
    private String phone;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /**
     * 创建用户
     */
    public static User create(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }
}
