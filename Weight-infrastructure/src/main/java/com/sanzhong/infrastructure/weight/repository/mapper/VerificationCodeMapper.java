package com.sanzhong.infrastructure.weight.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sanzhong.infrastructure.weight.repository.model.VerificationCodeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * VerificationCodeMapper接口
 *
 * @author visual-ddd
 * @since 1.0
 */
@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCodeDO> {

    /**
     * 统计指定时间内的发送次数
     */
    Long countByPhoneAndTimeRange(@Param("phone") String phone,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定IP在指定时间内的发送次数
     */
    Long countByIpAndTimeRange(@Param("ip") String ip,
                               @Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);
}
