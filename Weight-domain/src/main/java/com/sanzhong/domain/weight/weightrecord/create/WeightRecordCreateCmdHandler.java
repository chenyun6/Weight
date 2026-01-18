package com.sanzhong.domain.weight.weightrecord.create;

import com.common.tools.core.exception.BizException;
import com.sanzhong.common.weight.resultcode.WeightResultCode;
import com.sanzhong.domain.weight.weightrecord.WeightRecord;
import com.sanzhong.domain.weight.weightrecord.WeightRecordFactory;
import com.sanzhong.domain.weight.weightrecord.WeightRecordRepository;
import com.sanzhong.domain.weight.weightrecord.WeightType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 创建体重记录-指令处理器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class WeightRecordCreateCmdHandler {

    private final WeightRecordRepository repository;
    private final WeightRecordFactory factory;

    public Long handle(WeightRecordCreateCmd cmd) {
        LocalDate today = LocalDate.now();
        WeightType weightType = WeightType.fromValue(cmd.getWeightType());
        if (weightType == null) {
            throw new BizException(WeightResultCode.WEIGHT_TYPE_INVALID);
        }

        // 检查今天是否已记录
        WeightRecord existingRecord = repository.findByUserIdAndDate(cmd.getUserId(), today);
        if (existingRecord != null) {
            throw new BizException(WeightResultCode.WEIGHT_RECORD_ALREADY_EXISTS);
        }

        // 创建记录
        WeightRecord record = factory.getInstance(cmd.getUserId(), weightType, today);
        WeightRecord newRecord = repository.save(record);

        return newRecord.getId();
    }
}
