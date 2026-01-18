package com.sanzhong.domain.weight.weightrecord;

import org.springframework.stereotype.Component;
import java.time.LocalDate;

/**
 * 体重记录-聚合根-工厂
 *
 * @author visual-ddd
 * @since 1.0
 */
@Component
public class WeightRecordFactory {

    public WeightRecord getInstance(Long userId, WeightType weightType, LocalDate recordDate) {
        WeightRecord instance = new WeightRecord();
        instance.create(userId, weightType, recordDate);
        return instance;
    }
}
