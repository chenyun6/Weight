package com.cy.adapter.weight.web;

import com.common.tools.core.dto.ResultDTO;
import com.cy.client.weight.WeightRpcService;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.client.weight.query.LoginDTO;
import com.cy.client.weight.query.SendCodeDTO;
import com.cy.client.weight.query.WeightRecordCreateDTO;
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
@RequestMapping("/web/weight")
@Api(value = "/web/weight", tags = "[WEB] Weighty-体重管理")
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
