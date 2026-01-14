package com.cy.infrastructure.weight.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.infrastructure.weight.repository.model.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper接口
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
