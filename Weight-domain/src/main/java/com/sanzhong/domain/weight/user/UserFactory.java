package com.sanzhong.domain.weight.user;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 用户-聚合根-工厂
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class UserFactory {

    /**
     * 创建用户
     */
    public User getInstance(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }
}
