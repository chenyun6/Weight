package com.cy.infrastructure.weight.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.cy.domain.weight.verificationcode.VerificationCode;
import com.cy.domain.weight.verificationcode.VerificationCodeRepository;
import com.cy.infrastructure.weight.repository.model.VerificationCodeDO;
import com.cy.infrastructure.weight.repository.mapper.VerificationCodeMapper;
import com.cy.infrastructure.weight.assembler.VerificationCode2VerificationCodeDOConvert;

import java.time.LocalDateTime;

/**
 * 验证码-仓储实现类
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@Component
public class VerificationCodeRepositoryImpl implements VerificationCodeRepository {

    private final VerificationCodeMapper verificationCodeMapper;

    public VerificationCodeRepositoryImpl(VerificationCodeMapper verificationCodeMapper) {
        this.verificationCodeMapper = verificationCodeMapper;
    }

    @Override
    public VerificationCode save(VerificationCode verificationCode) {
        VerificationCodeDO codeDO = VerificationCode2VerificationCodeDOConvert.INSTANCE.dto2Do(verificationCode);
        if (codeDO.getId() == null) {
            int insert = verificationCodeMapper.insert(codeDO);
            Assert.isTrue(insert == 1, "插入数据库异常，请联系管理员");
        } else {
            int update = verificationCodeMapper.updateById(codeDO);
            Assert.isTrue(update == 1, "更新数据库异常，请联系管理员");
        }
        return VerificationCode2VerificationCodeDOConvert.INSTANCE.do2Dto(codeDO);
    }

    @Override
    public VerificationCode findByPhoneAndCode(String phone, String code) {
        VerificationCodeDO codeDO = verificationCodeMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VerificationCodeDO>()
                        .eq(VerificationCodeDO::getPhone, phone)
                        .eq(VerificationCodeDO::getCode, code)
                        .orderByDesc(VerificationCodeDO::getSendTime)
                        .last("LIMIT 1")
        );
        if (codeDO == null) {
            return null;
        }
        return VerificationCode2VerificationCodeDOConvert.INSTANCE.do2Dto(codeDO);
    }

    @Override
    public Long countByPhoneAndTimeRange(String phone, LocalDateTime startTime, LocalDateTime endTime) {
        return verificationCodeMapper.countByPhoneAndTimeRange(phone, startTime, endTime);
    }

    @Override
    public Long countByIpAndTimeRange(String ip, LocalDateTime startTime, LocalDateTime endTime) {
        return verificationCodeMapper.countByIpAndTimeRange(ip, startTime, endTime);
    }

    @Override
    public VerificationCode findLatestByPhone(String phone) {
        VerificationCodeDO codeDO = verificationCodeMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<VerificationCodeDO>()
                        .eq(VerificationCodeDO::getPhone, phone)
                        .orderByDesc(VerificationCodeDO::getSendTime)
                        .last("LIMIT 1")
        );
        if (codeDO == null) {
            return null;
        }
        return VerificationCode2VerificationCodeDOConvert.INSTANCE.do2Dto(codeDO);
    }
}
