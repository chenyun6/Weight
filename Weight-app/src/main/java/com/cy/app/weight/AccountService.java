package com.cy.app.weight;

import com.common.tools.core.dto.ResultDTO;
import com.common.tools.core.exception.BizException;
import com.cy.app.weight.auth.AuthService;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.client.weight.query.*;
import com.cy.domain.weight.user.User;
import com.cy.domain.weight.user.UserRepository;
import com.cy.domain.weight.verificationcode.VerificationCode;
import com.cy.domain.weight.verificationcode.VerificationCodeRepository;
import com.cy.infrastructure.weight.auth.PasswordService;
import com.cy.infrastructure.weight.repository.UserTokenRepositoryImpl;
import com.cy.infrastructure.weight.repository.VerificationCodeRepositoryImpl;
import com.cy.domain.weight.weightrecord.WeightRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 账号管理服务
 */
@Service
public class AccountService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordService passwordService;

    @Resource
    private AuthService authService;

    @Resource
    private VerificationCodeRepository verificationCodeRepository;

    @Resource
    private UserTokenRepositoryImpl userTokenRepository;

    @Resource
    private WeightRecordRepository weightRecordRepository;

    /**
     * 密码登录
     */
    public ResultDTO<LoginResponseDTO> loginWithPassword(PasswordLoginDTO dto) {
        User user = userRepository.findByPhone(dto.getPhone());
        if (user == null || user.getPassword() == null) {
            throw new BizException("手机号或密码错误");
        }

        if (!passwordService.matches(dto.getPassword(), user.getPassword())) {
            throw new BizException("手机号或密码错误");
        }

        LoginResponseDTO response = authService.generateToken(user.getId());
        return ResultDTO.success(response);
    }

    /**
     * 设置密码（首次）
     */
    @Transactional
    public ResultDTO<Void> setPassword(Long userId, SetPasswordDTO dto) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        if (user.getPassword() != null) {
            throw new BizException("已设置过密码");
        }

        passwordService.validateComplexity(dto.getPassword());
        user.setPassword(passwordService.encrypt(dto.getPassword()));
        userRepository.updatePassword(user);
        return ResultDTO.success(null);
    }

    /**
     * 修改密码
     */
    @Transactional
    public ResultDTO<Void> changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        if (user.getPassword() == null) {
            throw new BizException("请先设置密码后再修改");
        }

        if (!passwordService.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BizException("旧密码错误");
        }

        passwordService.validateComplexity(dto.getNewPassword());
        user.setPassword(passwordService.encrypt(dto.getNewPassword()));
        userRepository.updatePassword(user);
        return ResultDTO.success(null);
    }

    /**
     * 删除账号
     */
    @Transactional
    public ResultDTO<Void> deleteAccount(Long userId, DeleteAccountDTO dto) {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        // 验证身份
        if ("password".equals(dto.getVerifyType())) {
            // 密码验证
            if (user.getPassword() == null || !passwordService.matches(dto.getCode(), user.getPassword())) {
                throw new BizException("密码错误");
            }
        } else if ("sms".equals(dto.getVerifyType())) {
            // 短信验证码验证
            VerificationCode latest = verificationCodeRepository.findLatestByPhone(user.getPhone());
            if (latest == null || !latest.getCode().equals(dto.getCode())) {
                throw new BizException("验证码错误或已过期");
            }
            if (latest.getExpireTime().isBefore(LocalDateTime.now())) {
                throw new BizException("验证码已过期");
            }
            if (latest.getUsed() != null && latest.getUsed() == 1) {
                throw new BizException("验证码已使用");
            }
            // 标记验证码为已使用，防止重放
            verificationCodeRepository.markAsUsed(latest.getId());
        } else {
            throw new BizException("不支持的验证方式");
        }

        // 删除关联数据
        weightRecordRepository.deleteByUserId(userId);
        userTokenRepository.deleteByUserId(userId);
        verificationCodeRepository.deleteByPhone(user.getPhone());

        // 删除用户
        userRepository.deleteUserWithRelations(userId);

        return ResultDTO.success(null);
    }
}
