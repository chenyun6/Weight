package com.cy.adapter.weight.web;

import com.common.tools.core.dto.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cy.client.weight.WeightRpcService;
import com.cy.client.weight.query.SendCodeDTO;
import com.cy.client.weight.query.LoginDTO;
import com.cy.client.weight.query.WeightRecordCreateDTO;

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
    public ResultDTO<Long> login(@RequestBody @Valid LoginDTO dto, HttpServletRequest request) {
        // 获取客户端IP
        String ip = getClientIp(request);
        dto.setIp(ip);
        return weightRpcService.login(dto);
    }

    @ApiOperation("创建体重记录")
    @PostMapping("/create-record")
    public ResultDTO<Long> createRecord(@RequestBody @Valid WeightRecordCreateDTO dto) {
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
