package com.sanzhong.infrastructure.weight.repository;

import com.common.tools.core.exception.BizException;
import com.common.tools.core.resultcode.CommonResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.sanzhong.domain.weight.weightrecord.WeightRecord;
import com.sanzhong.domain.weight.weightrecord.WeightRecordRepository;
import com.sanzhong.infrastructure.weight.repository.model.WeightRecordDO;
import com.sanzhong.infrastructure.weight.repository.mapper.WeightRecordMapper;
import com.sanzhong.infrastructure.weight.assembler.WeightRecord2WeightRecordDOConvert;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 体重记录-聚合仓储实现类
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeightRecordRepositoryImpl implements WeightRecordRepository {

    private final WeightRecordMapper weightRecordMapper;

    @Override
    public WeightRecord save(WeightRecord weightRecord) {
        WeightRecordDO recordDO = WeightRecord2WeightRecordDOConvert.INSTANCE.dto2Do(weightRecord);
        int insert = weightRecordMapper.insert(recordDO);
        Assert.isTrue(insert == 1, "插入数据库异常，请联系管理员");
        return WeightRecord2WeightRecordDOConvert.INSTANCE.do2Dto(recordDO);
    }

    @Override
    public WeightRecord update(WeightRecord weightRecord) {
        WeightRecordDO recordDO = WeightRecord2WeightRecordDOConvert.INSTANCE.dto2Do(weightRecord);
        int update = weightRecordMapper.updateById(recordDO);
        Assert.isTrue(update == 1, "更新数据库异常，请联系管理员");
        return WeightRecord2WeightRecordDOConvert.INSTANCE.do2Dto(recordDO);
    }

    @Override
    public void remove(WeightRecord weightRecord) {
        WeightRecordDO recordDO = WeightRecord2WeightRecordDOConvert.INSTANCE.dto2Do(weightRecord);
        weightRecordMapper.deleteById(recordDO.getId());
    }

    @Override
    public WeightRecord find(Long id) {
        WeightRecordDO result = Optional.ofNullable(weightRecordMapper.selectById(id))
                .orElseThrow(() -> new BizException(CommonResultCode.NOT_EXISTS));
        return WeightRecord2WeightRecordDOConvert.INSTANCE.do2Dto(result);
    }

    @Override
    public WeightRecord findByUserIdAndDate(Long userId, LocalDate recordDate) {
        WeightRecordDO recordDO = weightRecordMapper.findByUserIdAndDate(userId, recordDate);
        if (recordDO == null) {
            return null;
        }
        return WeightRecord2WeightRecordDOConvert.INSTANCE.do2Dto(recordDO);
    }

    @Override
    public LocalDate findLastRecordDate(Long userId) {
        return weightRecordMapper.findLastRecordDate(userId);
    }
}
