package com.cy.app.weight;

import com.common.tools.core.dto.ResultDTO;
import com.cy.app.weight.assembler.LoginDTO2LoginCmdConvert;
import com.cy.app.weight.assembler.SendCodeDTO2SendCodeCmdConvert;
import com.cy.app.weight.assembler.WeightRecordCreateDTO2WeightRecordCreateCmdConvert;
import com.cy.app.weight.auth.AuthService;
import com.cy.client.weight.WeightRpcService;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.client.weight.dto.WeightRecordDTO;
import com.cy.client.weight.query.*;
import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.domain.weight.user.login.LoginCmdHandler;
import com.cy.domain.weight.user.sendcode.SendCodeCmdHandler;
import com.cy.domain.weight.weightrecord.WeightRecord;
import com.cy.domain.weight.weightrecord.WeightRecordFactory;
import com.cy.domain.weight.weightrecord.WeightRecordRepository;
import com.cy.domain.weight.weightrecord.WeightType;
import com.cy.domain.weight.weightrecord.create.WeightRecordCreateCmdHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private UserRepository userRepository;

    @Resource
    private WeightRecordFactory weightRecordFactory;

    @Resource
    private AccountService accountService;

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

    @Override
    public ResultDTO<UserProfileDTO> getUserProfile(CheckTodayRecordDTO dto) {
        User user = userRepository.find(dto.getUserId());
        if (user == null) {
            return ResultDTO.fail(404, "用户不存在");
        }
        UserProfileDTO profile = new UserProfileDTO();
        profile.setUserId(user.getId());
        profile.setPhone(desensitizePhone(user.getPhone()));
        profile.setHasPassword(user.getPassword() != null);
        return ResultDTO.success(profile);
    }

    @Override
    public ResultDTO<WeightRecordDTO> addWeightRecord(WeightAddDTO dto) {
        Long userId = dto.getUserId();
        WeightType weightType = WeightType.fromValue(dto.getWeightType());
        if (weightType == null) {
            return ResultDTO.fail(400, "无效的体重类型");
        }

        // 检查当日是否已记录
        LocalDate recordDate = dto.getRecordDate();
        WeightRecord existing = weightRecordRepository.findByUserIdAndDate(userId, recordDate);
        if (existing != null) {
            return ResultDTO.fail(1001, "今天已经记录过了");
        }

        // 创建记录
        WeightRecord record = weightRecordFactory.getInstance(userId, weightType, recordDate);
        WeightRecord saved = weightRecordRepository.save(record);

        // 转换为DTO返回
        WeightRecordDTO result = new WeightRecordDTO();
        result.setId(saved.getId());
        result.setWeightType(saved.getWeightType().getValue());
        result.setRecordDate(saved.getRecordDate());
        result.setCreateTime(saved.getCreateTime());
        return ResultDTO.success(result);
    }

    @Override
    public ResultDTO<WeightRecordListResultDTO> getWeightRecordList(WeightRecordListQuery query) {
        Long userId = query.getUserId();
        LocalDate startDate = query.getStartDate() != null ? query.getStartDate() : LocalDate.now().minusDays(30);
        LocalDate endDate = query.getEndDate() != null ? query.getEndDate() : LocalDate.now();

        List<WeightRecord> records = weightRecordRepository.findListByUserIdAndDateRange(userId, startDate, endDate);
        if (records == null) {
            records = java.util.Collections.emptyList();
        }

        List<WeightRecordDTO> dtoList = records.stream().map(r -> {
            WeightRecordDTO dto = new WeightRecordDTO();
            dto.setId(r.getId());
            dto.setUserId(r.getUserId());
            dto.setWeightType(r.getWeightType() != null ? r.getWeightType().getValue() : null);
            dto.setRecordDate(r.getRecordDate());
            dto.setCreateTime(r.getCreateTime());
            return dto;
        }).collect(Collectors.toList());

        WeightRecordListResultDTO result = new WeightRecordListResultDTO();
        result.setList(dtoList);
        result.setTotal(dtoList.size());
        return ResultDTO.success(result);
    }

    @Override
    public ResultDTO<LoginResponseDTO> loginWithPassword(PasswordLoginDTO dto) {
        return accountService.loginWithPassword(dto);
    }

    @Override
    public ResultDTO<Void> setPassword(SetPasswordDTO dto) {
        Long userId = dto.getUserId();
        return accountService.setPassword(userId, dto);
    }

    @Override
    public ResultDTO<Void> changePassword(ChangePasswordDTO dto) {
        Long userId = dto.getUserId();
        return accountService.changePassword(userId, dto);
    }

    @Override
    public ResultDTO<Void> deleteAccount(DeleteAccountDTO dto) {
        Long userId = dto.getUserId();
        return accountService.deleteAccount(userId, dto);
    }

    /**
     * 手机号脱敏：中间4位替换为****
     */
    private String desensitizePhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
