package com.cy.infrastructure.weight.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cy.infrastructure.weight.repository.model.WeightRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * WeightRecordMapper接口
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface WeightRecordMapper extends BaseMapper<WeightRecordDO> {

    /**
     * 根据用户ID和日期查询记录
     */
    WeightRecordDO findByUserIdAndDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);

    /**
     * 查询用户最后一次记录日期
     */
    LocalDate findLastRecordDate(@Param("userId") Long userId);
}
