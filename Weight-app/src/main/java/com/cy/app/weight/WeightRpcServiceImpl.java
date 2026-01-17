package com.cy.app.weight;

import com.common.tools.core.dto.ResultDTO;
import com.cy.app.weight.assembler.LoginDTO2LoginCmdConvert;
import com.cy.app.weight.assembler.SendCodeDTO2SendCodeCmdConvert;
import com.cy.app.weight.assembler.WeightRecordCreateDTO2WeightRecordCreateCmdConvert;
import com.cy.app.weight.auth.AuthService;
import com.cy.client.weight.WeightRpcService;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.client.weight.dto.WeightRecordDTO;
import com.cy.client.weight.query.LoginDTO;
import com.cy.client.weight.query.RefreshTokenDTO;
import com.cy.client.weight.query.SendCodeDTO;
import com.cy.client.weight.query.WeightRecordCreateDTO;
import com.cy.domain.weight.user.login.LoginCmdHandler;
import com.cy.domain.weight.user.sendcode.SendCodeCmdHandler;
import com.cy.domain.weight.weightrecord.WeightRecord;
import com.cy.domain.weight.weightrecord.WeightRecordRepository;
import com.cy.domain.weight.weightrecord.create.WeightRecordCreateCmdHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Weighty - 体重管理-RPC能力接口实现
 *
 * @author visual-ddd
 * @since 1.0
 */
@RestController
@RequestMapping("/rpc/weight")
public class WeightRpcServiceImpl implements WeightRpcService {

    @Resource
    private SendCodeCmdHandler sendCodeCmdHandler;

    @Resource
    private LoginCmdHandler loginCmdHandler;

    @Resource
    private WeightRecordCreateCmdHandler weightRecordCreateCmdHandler;

    @Resource
    private AuthService authService;

    @Resource
    private WeightRecordRepository weightRecordRepository;

    @Override
    public ResultDTO<String> sendCode(SendCodeDTO dto) {
        String code = sendCodeCmdHandler.handle(SendCodeDTO2SendCodeCmdConvert.INSTANCE.dto2Do(dto));
        return ResultDTO.success(code);
    }

    @Override
    public ResultDTO<LoginResponseDTO> login(LoginDTO dto) {
        Long userId = loginCmdHandler.handle(LoginDTO2LoginCmdConvert.INSTANCE.dto2Do(dto));
        LoginResponseDTO response = authService.generateToken(userId);
        return ResultDTO.success(response);
    }

    @Override
    public ResultDTO<LoginResponseDTO> refreshToken(RefreshTokenDTO dto) {
        LoginResponseDTO response = authService.refreshToken(dto.getRefreshToken());
        return ResultDTO.success(response);
    }

    @Override
    public ResultDTO<Boolean> checkTodayRecord(com.cy.client.weight.query.CheckTodayRecordDTO dto) {
        LocalDate today = LocalDate.now();
        WeightRecord existing = weightRecordRepository.findByUserIdAndDate(dto.getUserId(), today);
        return ResultDTO.success(existing != null);
    }
    
    @Override
    public ResultDTO<com.cy.client.weight.dto.WeightRecordDTO> getTodayRecord(com.cy.client.weight.query.CheckTodayRecordDTO dto) {
        LocalDate today = LocalDate.now();
        WeightRecord existing = weightRecordRepository.findByUserIdAndDate(dto.getUserId(), today);
        if (existing == null) {
            return ResultDTO.success(null);
        }
        // 转换为DTO
        WeightRecordDTO dtoResult = new WeightRecordDTO();
        dtoResult.setId(existing.getId());
        dtoResult.setUserId(existing.getUserId());
        dtoResult.setWeightType(existing.getWeightType() != null ? existing.getWeightType().getValue() : null);
        dtoResult.setRecordDate(existing.getRecordDate());
        dtoResult.setCreateTime(existing.getCreateTime());
        return ResultDTO.success(dtoResult);
    }

    @Override
    public ResultDTO<Long> createRecord(WeightRecordCreateDTO dto) {
        Long id = weightRecordCreateCmdHandler.handle(WeightRecordCreateDTO2WeightRecordCreateCmdConvert.INSTANCE.dto2Do(dto));
        return ResultDTO.success(id);
    }
}
