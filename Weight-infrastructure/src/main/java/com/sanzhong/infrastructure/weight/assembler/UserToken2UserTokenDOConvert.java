package com.sanzhong.infrastructure.weight.assembler;

import com.sanzhong.domain.weight.token.UserToken;
import com.sanzhong.infrastructure.weight.repository.model.UserTokenDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserToken转UserTokenDO转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface UserToken2UserTokenDOConvert {

    UserToken2UserTokenDOConvert INSTANCE = Mappers.getMapper(UserToken2UserTokenDOConvert.class);

    UserTokenDO domain2DO(UserToken token);

    UserToken do2Domain(UserTokenDO tokenDO);
}
