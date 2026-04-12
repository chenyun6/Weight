package com.cy.client.weight;

import com.common.tools.core.dto.ResultDTO;
import com.cy.client.weight.dto.LoginResponseDTO;
import com.cy.client.weight.dto.WeightRecordDTO;
import com.cy.client.weight.query.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Weighty - 体重管理-RPC能力接口
 *
 * @author visual-ddd
 * @since 1.0
 */
@FeignClient(name = "weighty", path = "/weighty/rpc/weight")
@Api(tags = "[RPC] Weighty-体重管理")
public interface WeightRpcService {

    @ApiOperation("发送验证码")
    @PostMapping("/send-code")
    ResultDTO<String> sendCode(@RequestBody @Valid SendCodeDTO dto);

    @ApiOperation("登录")
    @PostMapping("/login")
    ResultDTO<LoginResponseDTO> login(@RequestBody @Valid LoginDTO dto);

    @ApiOperation("刷新Token")
    @PostMapping("/refresh-token")
    ResultDTO<LoginResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenDTO dto);

    @ApiOperation("检查今天是否已记录")
    @PostMapping("/check-today-record")
    ResultDTO<Boolean> checkTodayRecord(@RequestBody @Valid CheckTodayRecordDTO dto);

    @ApiOperation("获取今天的记录详情")
    @PostMapping("/get-today-record")
    ResultDTO<WeightRecordDTO> getTodayRecord(@RequestBody @Valid CheckTodayRecordDTO dto);

    @ApiOperation("创建体重记录")
    @PostMapping("/create-record")
    ResultDTO<Long> createRecord(@RequestBody @Valid WeightRecordCreateDTO dto);

    @ApiOperation("获取用户信息")
    @PostMapping("/user/profile")
    ResultDTO<com.cy.client.weight.query.UserProfileDTO> getUserProfile(@RequestBody @Valid com.cy.client.weight.query.CheckTodayRecordDTO dto);

    @ApiOperation("新增体重记录")
    @PostMapping("/weight/add")
    ResultDTO<com.cy.client.weight.dto.WeightRecordDTO> addWeightRecord(@RequestBody @Valid com.cy.client.weight.query.WeightAddDTO dto);

    @ApiOperation("体重记录列表")
    @PostMapping("/weight/list")
    ResultDTO<com.cy.client.weight.query.WeightRecordListResultDTO> getWeightRecordList(@RequestBody @Valid com.cy.client.weight.query.WeightRecordListQuery query);

    @ApiOperation("密码登录")
    @PostMapping("/login/password")
    ResultDTO<LoginResponseDTO> loginWithPassword(@RequestBody @Valid com.cy.client.weight.query.PasswordLoginDTO dto);

    @ApiOperation("设置密码")
    @PostMapping("/account/set-password")
    ResultDTO<Void> setPassword(@RequestBody @Valid com.cy.client.weight.query.SetPasswordDTO dto);

    @ApiOperation("修改密码")
    @PostMapping("/account/change-password")
    ResultDTO<Void> changePassword(@RequestBody @Valid com.cy.client.weight.query.ChangePasswordDTO dto);

    @ApiOperation("删除账号")
    @PostMapping("/account/delete")
    ResultDTO<Void> deleteAccount(@RequestBody @Valid com.cy.client.weight.query.DeleteAccountDTO dto);
}
