package com.sanzhong.adapter.weight.app;

import com.common.tools.core.dto.ResultDTO;
import com.sanzhong.client.weight.WeightRpcService;
import com.sanzhong.client.weight.dto.LoginResponseDTO;
import com.sanzhong.client.weight.dto.WeightRecordDTO;
import com.sanzhong.client.weight.query.CheckTodayRecordDTO;
import com.sanzhong.client.weight.query.LoginDTO;
import com.sanzhong.client.weight.query.SendCodeDTO;
import com.sanzhong.client.weight.query.WeightRecordCreateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Weighty - 体重管理-APP端
 *
 * @author visual-ddd
 * @since 1.0
 */
@RestController
@RequestMapping("/app/weight")
@Api(value = "/app/weight", tags = "[APP] Weighty-体重管理")
@RequiredArgsConstructor
public class WeightAppController {

    private static final String USER_ID = "userId";
    private final WeightRpcService weightRpcService;

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
    public ResultDTO<LoginResponseDTO> refreshToken(@RequestBody @Valid com.sanzhong.client.weight.query.RefreshTokenDTO dto) {
        return weightRpcService.refreshToken(dto);
    }

    @ApiOperation("检查今天是否已记录")
    @PostMapping("/check-today-record")
    public ResultDTO<Boolean> checkTodayRecord(HttpServletRequest request) {
        // 从拦截器设置的属性中获取userId
        Long userId = (Long) request.getAttribute(USER_ID);
        if (userId == null) {
            return ResultDTO.success(false);
        }
        com.sanzhong.client.weight.query.CheckTodayRecordDTO dto = new com.sanzhong.client.weight.query.CheckTodayRecordDTO();
        dto.setUserId(userId);
        return weightRpcService.checkTodayRecord(dto);
    }

    @ApiOperation("获取今天的记录详情")
    @PostMapping("/get-today-record")
    public ResultDTO<WeightRecordDTO> getTodayRecord(HttpServletRequest request) {
        // 从拦截器设置的属性中获取userId
        Long userId = (Long) request.getAttribute(USER_ID);
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
        Long userId = (Long) request.getAttribute(USER_ID);
        if (userId != null) {
            dto.setUserId(userId);
        }
        return weightRpcService.createRecord(dto);
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
