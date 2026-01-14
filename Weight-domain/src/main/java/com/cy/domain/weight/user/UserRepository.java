package com.cy.domain.weight.user;

import com.cy.domain.BaseRepository;

import java.util.List;

/**
 * 用户-聚合根-仓储接口
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户
     */
    User findByPhone(String phone);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> findAll();
}
