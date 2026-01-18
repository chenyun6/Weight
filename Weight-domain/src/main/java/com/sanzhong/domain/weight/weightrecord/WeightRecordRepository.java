package com.sanzhong.domain.weight.weightrecord;

import com.sanzhong.domain.BaseRepository;
import java.time.LocalDate;

/**
 * 体重记录-聚合根-仓储接口
 *
 * @author visual-ddd
 * @since 1.0
 */
public interface WeightRecordRepository extends BaseRepository<WeightRecord, Long> {

    /**
     * 根据用户ID和日期查询记录
     *
     * @param userId 用户ID
     * @param recordDate 记录日期
     * @return 体重记录
     */
    WeightRecord findByUserIdAndDate(Long userId, LocalDate recordDate);

    /**
     * 查询用户最后一次记录日期
     *
     * @param userId 用户ID
     * @return 最后记录日期
     */
    LocalDate findLastRecordDate(Long userId);
}
