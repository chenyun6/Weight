package com.cy.infrastructure.weight.assembler;

import com.cy.domain.weight.verificationcode.VerificationCode;
import com.cy.infrastructure.weight.repository.model.VerificationCodeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * VerificationCode转VerificationCodeDO转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface VerificationCode2VerificationCodeDOConvert {

    VerificationCode2VerificationCodeDOConvert INSTANCE = Mappers.getMapper(VerificationCode2VerificationCodeDOConvert.class);

    VerificationCodeDO dto2Do(VerificationCode verificationCode);

    VerificationCode do2Dto(VerificationCodeDO verificationCodeDO);
}
