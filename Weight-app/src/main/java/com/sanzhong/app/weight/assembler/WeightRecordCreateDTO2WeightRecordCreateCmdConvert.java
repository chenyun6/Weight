package com.sanzhong.app.weight.assembler;

import com.sanzhong.client.weight.query.WeightRecordCreateDTO;
import com.sanzhong.domain.weight.weightrecord.create.WeightRecordCreateCmd;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * WeightRecordCreateDTO转WeightRecordCreateCmd转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface WeightRecordCreateDTO2WeightRecordCreateCmdConvert {

    WeightRecordCreateDTO2WeightRecordCreateCmdConvert INSTANCE = Mappers.getMapper(WeightRecordCreateDTO2WeightRecordCreateCmdConvert.class);

    WeightRecordCreateCmd dto2Do(WeightRecordCreateDTO dto);
}
