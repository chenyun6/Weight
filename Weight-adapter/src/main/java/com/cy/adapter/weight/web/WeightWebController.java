package com.cy.adapter.weight.web;

import com.common.tools.core.dto.ResultDTO;
import com.cy.client.weight.WeightRpcService;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.client.weight.dto.WeightRecordDTO;
import com.cy.client.weight.query.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Weighty - 体重管理-B端
 *
 * @author visual-ddd
 * @since 1.0
 */
@RestController
@RequestMapping("/app/weight")
@Api(value = "/app/weight", tags = "[APP] Weighty-体重管理")
public class WeightWebController {

    @Resource
    private WeightRpcService weightRpcService;

    @ApiOperation("发送验证码")
    @PostMapping("/send-code")
    public ResultDTO<String> sendCode(@RequestBody @Valid SendCodeDTO dto, HttpServletRequest request) {
        // 获取客户端IP
        String ip = getClientIp(request);
        dto.setIp(ip);
        return weightRpcService.sendCode(dto);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResultDTO<LoginResponseDTO> login(@RequestBody @Valid LoginDTO dto, HttpServletRequest request) {
        // 获取客户端IP
        String ip = getClientIp(request);
        dto.setIp(ip);
        return weightRpcService.login(dto);
    }

    @ApiOperation("刷新Token")
    @PostMapping("/refresh-token")
    public ResultDTO<LoginResponseDTO> refreshToken(@RequestBody @Valid com.cy.client.weight.query.RefreshTokenDTO dto) {
        return weightRpcService.refreshToken(dto);
    }

    @ApiOperation("设置密码")
    @PostMapping("/set-password")
    public ResultDTO<Void> setPassword(@RequestBody @Valid com.cy.client.weight.query.SetPasswordDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.fail(401, "未登录或Token已过期");
        }
        dto.setUserId(userId);
        return weightRpcService.setPassword(dto);
    }

    @ApiOperation("修改密码")
    @PostMapping("/change-password")
    public ResultDTO<Void> changePassword(@RequestBody @Valid com.cy.client.weight.query.ChangePasswordDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.fail(401, "未登录或Token已过期");
        }
        dto.setUserId(userId);
        return weightRpcService.changePassword(dto);
    }

    @ApiOperation("删除账号")
    @PostMapping("/delete-account")
    public ResultDTO<Void> deleteAccount(@RequestBody @Valid com.cy.client.weight.query.DeleteAccountDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.fail(401, "未登录或Token已过期");
        }
        dto.setUserId(userId);
        return weightRpcService.deleteAccount(dto);
    }

    @ApiOperation("密码登录")
    @PostMapping("/login/password")
    public ResultDTO<LoginResponseDTO> loginWithPassword(@RequestBody @Valid PasswordLoginDTO dto, HttpServletRequest request) {
        // 获取客户端IP
        String ip = getClientIp(request);
        return weightRpcService.loginWithPassword(dto);
    }

    @ApiOperation("检查今天是否已记录")
    @PostMapping("/check-today-record")
    public ResultDTO<Boolean> checkTodayRecord(HttpServletRequest request) {
        // 从拦截器设置的属性中获取userId
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.success(false);
        }
        com.cy.client.weight.query.CheckTodayRecordDTO dto = new com.cy.client.weight.query.CheckTodayRecordDTO();
        dto.setUserId(userId);
        return weightRpcService.checkTodayRecord(dto);
    }

    @ApiOperation("获取今天的记录详情")
    @PostMapping("/get-today-record")
    public ResultDTO<WeightRecordDTO> getTodayRecord(HttpServletRequest request) {
        // 从拦截器设置的属性中获取userId
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.success(null);
        }
        CheckTodayRecordDTO dto = new CheckTodayRecordDTO();
        dto.setUserId(userId);
        return weightRpcService.getTodayRecord(dto);
    }

    @ApiOperation("创建体重记录")
    @PostMapping("/create-record")
    public ResultDTO<Long> createRecord(@RequestBody @Valid WeightRecordCreateDTO dto, HttpServletRequest request) {
        // 从拦截器设置的属性中获取userId
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null) {
            dto.setUserId(userId);
        }
        return weightRpcService.createRecord(dto);
    }

    @ApiOperation("获取用户信息")
    @PostMapping("/user/profile")
    public ResultDTO<UserProfileDTO> getUserProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.fail(401, "未登录或Token已过期");
        }
        CheckTodayRecordDTO dto = new CheckTodayRecordDTO();
        dto.setUserId(userId);
        return weightRpcService.getUserProfile(dto);
    }

    @ApiOperation("新增体重记录")
    @PostMapping("/weight/add")
    public ResultDTO<WeightRecordDTO> addWeightRecord(@RequestBody @Valid WeightAddDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        dto.setUserId(userId);
        return weightRpcService.addWeightRecord(dto);
    }

    @ApiOperation("体重记录列表")
    @PostMapping("/weight/list")
    public ResultDTO<WeightRecordListResultDTO> getWeightRecordList(@RequestBody(required = false) WeightRecordListQuery query, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResultDTO.fail(401, "未登录或Token已过期");
        }
        if (query == null) {
            query = new WeightRecordListQuery();
        }
        query.setUserId(userId);
        return weightRpcService.getWeightRecordList(query);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
