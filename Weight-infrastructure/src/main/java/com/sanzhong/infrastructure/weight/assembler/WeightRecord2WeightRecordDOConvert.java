package com.sanzhong.infrastructure.weight.assembler;

import com.sanzhong.domain.weight.weightrecord.WeightRecord;
import com.sanzhong.domain.weight.weightrecord.WeightType;
import com.sanzhong.infrastructure.weight.repository.model.WeightRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * WeightRecord转WeightRecordDO转换器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface WeightRecord2WeightRecordDOConvert {

    WeightRecord2WeightRecordDOConvert INSTANCE = Mappers.getMapper(WeightRecord2WeightRecordDOConvert.class);

    WeightRecordDO dto2Do(WeightRecord weightRecord);

    WeightRecord do2Dto(WeightRecordDO weightRecordDO);

    /**
     * MapStruct: WeightType -> Integer
     */
    default Integer map(WeightType value) {
        return value == null ? null : value.getValue();
    }

    /**
     * MapStruct: Integer -> WeightType
     */
    default WeightType map(Integer value) {
        return value == null ? null : WeightType.fromValue(value);
    }
}
