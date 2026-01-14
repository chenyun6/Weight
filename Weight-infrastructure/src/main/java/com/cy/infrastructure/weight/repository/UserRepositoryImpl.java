package com.cy.infrastructure.weight.repository;

import com.common.tools.core.exception.BizException;
import com.common.tools.core.resultcode.CommonResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.infrastructure.weight.repository.model.UserDO;
import com.cy.infrastructure.weight.repository.mapper.UserMapper;
import com.cy.infrastructure.weight.assembler.User2UserDOConvert;

import java.util.List;
import java.util.Optional;

/**
 * 用户-聚合仓储实现类
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserDO userDO = User2UserDOConvert.INSTANCE.dto2Do(user);
        int insert = userMapper.insert(userDO);
        Assert.isTrue(insert == 1, "插入数据库异常，请联系管理员");
        return User2UserDOConvert.INSTANCE.do2Dto(userDO);
    }

    @Override
    public User update(User user) {
        UserDO userDO = User2UserDOConvert.INSTANCE.dto2Do(user);
        int update = userMapper.updateById(userDO);
        Assert.isTrue(update == 1, "更新数据库异常，请联系管理员");
        return User2UserDOConvert.INSTANCE.do2Dto(userDO);
    }

    @Override
    public void remove(User user) {
        UserDO userDO = User2UserDOConvert.INSTANCE.dto2Do(user);
        userMapper.deleteById(userDO.getId());
    }

    @Override
    public User find(Long id) {
        UserDO result = Optional.ofNullable(userMapper.selectById(id))
                .orElseThrow(() -> new BizException(CommonResultCode.NOT_EXISTS));
        return User2UserDOConvert.INSTANCE.do2Dto(result);
    }

    @Override
    public User findByPhone(String phone) {
        UserDO userDO = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserDO>()
                        .eq(UserDO::getPhone, phone)
        );
        if (userDO == null) {
            return null;
        }
        return User2UserDOConvert.INSTANCE.do2Dto(userDO);
    }

    @Override
    public List<User> findAll() {
        List<UserDO> userDOList = userMapper.selectList(null);
        return User2UserDOConvert.INSTANCE.doList2DtoList(userDOList);
    }
}
