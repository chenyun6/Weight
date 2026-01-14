package com.cy.domain.weight.weightrecord.create;

import com.cy.domain.weight.weightrecord.WeightRecord;
import com.cy.domain.weight.weightrecord.WeightRecordFactory;
import com.cy.domain.weight.weightrecord.WeightRecordRepository;
import com.cy.domain.weight.weightrecord.WeightType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 创建体重记录-指令处理器
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class WeightRecordCreateCmdHandler {

    @Resource
    private WeightRecordRepository repository;

    @Resource
    private WeightRecordFactory factory;

    public Long handle(WeightRecordCreateCmd cmd) {
        LocalDate today = LocalDate.now();
        WeightType weightType = WeightType.fromValue(cmd.getWeightType());
        if (weightType == null) {
            throw new RuntimeException("体重类型无效");
        }

        // 检查今天是否已记录
        WeightRecord existingRecord = repository.findByUserIdAndDate(cmd.getUserId(), today);
        if (existingRecord != null) {
            throw new RuntimeException("今天已经记录过了，每天只能记录一次");
        }

        // 创建记录
        WeightRecord record = factory.getInstance(cmd.getUserId(), weightType, today);
        WeightRecord newRecord = repository.save(record);

        return newRecord.getId();
    }
}
