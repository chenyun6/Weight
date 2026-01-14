package com.cy.app.weight.assembler;

import com.cy.client.weight.query.SendCodeDTO;
import com.cy.domain.weight.user.sendcode.SendCodeCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * SendCodeDTO转SendCodeCmd转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface SendCodeDTO2SendCodeCmdConvert {

    SendCodeDTO2SendCodeCmdConvert INSTANCE = Mappers.getMapper(SendCodeDTO2SendCodeCmdConvert.class);

    SendCodeCmd dto2Do(SendCodeDTO dto);
}
