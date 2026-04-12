package com.cy.domain.weight.weightrecord;

import com.cy.domain.BaseRepository;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * 根据用户ID和日期范围查询记录列表
     *
     * @param userId 用户ID
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @return 体重记录列表
     */
    List<WeightRecord> findListByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 删除用户的所有体重记录
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);
}
