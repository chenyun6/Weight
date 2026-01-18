package com.sanzhong.app.weight;

import com.common.tools.core.dto.ResultDTO;
import com.sanzhong.app.weight.assembler.LoginDTO2LoginCmdConvert;
import com.sanzhong.app.weight.assembler.SendCodeDTO2SendCodeCmdConvert;
import com.sanzhong.app.weight.assembler.WeightRecordCreateDTO2WeightRecordCreateCmdConvert;
import com.sanzhong.app.weight.auth.AuthService;
import com.sanzhong.client.weight.WeightRpcService;
import com.sanzhong.client.weight.dto.LoginResponseDTO;
import com.sanzhong.client.weight.dto.WeightRecordDTO;
import com.sanzhong.client.weight.query.LoginDTO;
import com.sanzhong.client.weight.query.RefreshTokenDTO;
import com.sanzhong.client.weight.query.SendCodeDTO;
import com.sanzhong.client.weight.query.WeightRecordCreateDTO;
import com.sanzhong.domain.weight.user.login.LoginCmdHandler;
import com.sanzhong.domain.weight.user.sendcode.SendCodeCmdHandler;
import com.sanzhong.domain.weight.weightrecord.WeightRecord;
import com.sanzhong.domain.weight.weightrecord.WeightRecordRepository;
import com.sanzhong.domain.weight.weightrecord.create.WeightRecordCreateCmdHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Weighty - 体重管理-RPC能力接口实现
 *
 * @author visual-ddd
 * @since 1.0
 */
@RestController
@RequestMapping("/rpc/weight")
@RequiredArgsConstructor
public class WeightRpcServiceImpl implements WeightRpcService {

    private final SendCodeCmdHandler sendCodeCmdHandler;
    private final LoginCmdHandler loginCmdHandler;
    private final WeightRecordCreateCmdHandler weightRecordCreateCmdHandler;
    private final AuthService authService;
    private final WeightRecordRepository weightRecordRepository;

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
    public ResultDTO<Boolean> checkTodayRecord(com.sanzhong.client.weight.query.CheckTodayRecordDTO dto) {
        LocalDate today = LocalDate.now();
        WeightRecord existing = weightRecordRepository.findByUserIdAndDate(dto.getUserId(), today);
        return ResultDTO.success(existing != null);
    }
    
    @Override
    public ResultDTO<WeightRecordDTO> getTodayRecord(com.sanzhong.client.weight.query.CheckTodayRecordDTO dto) {
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
