package com.cy.app.weight.assembler;

import com.cy.client.weight.query.LoginDTO;
import com.cy.domain.weight.user.login.LoginCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * LoginDTO转LoginCmd转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface LoginDTO2LoginCmdConvert {

    LoginDTO2LoginCmdConvert INSTANCE = Mappers.getMapper(LoginDTO2LoginCmdConvert.class);

    LoginCmd dto2Do(LoginDTO dto);
}
