package com.sanzhong.domain.weight.weightrecord;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体重记录-聚合根能力
 *
 * @author visual-ddd
 * @since 1.0
 */
public class WeightRecord extends AbstractWeightRecord {

    @Override
    public Boolean canRecord(LocalDate date) {
        // 检查记录日期是否与当前日期相同
        if (this.getRecordDate() != null && this.getRecordDate().equals(date)) {
            return false;
        }
        return true;
    }

    /**
     * 创建记录
     */
    public void create(Long userId, WeightType weightType, LocalDate recordDate) {
        this.setUserId(userId);
        this.setWeightType(weightType);
        this.setRecordDate(recordDate);
        this.setCreateTime(LocalDateTime.now());
    }
}
