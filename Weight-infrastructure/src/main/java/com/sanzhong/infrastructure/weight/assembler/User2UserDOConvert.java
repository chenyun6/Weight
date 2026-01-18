package com.sanzhong.infrastructure.weight.assembler;

import com.sanzhong.domain.weight.user.User;
import com.sanzhong.infrastructure.weight.repository.model.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * User转UserDO转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface User2UserDOConvert {

    User2UserDOConvert INSTANCE = Mappers.getMapper(User2UserDOConvert.class);

    UserDO dto2Do(User user);

    User do2Dto(UserDO userDO);

    List<User> doList2DtoList(List<UserDO> userDOList);
}
