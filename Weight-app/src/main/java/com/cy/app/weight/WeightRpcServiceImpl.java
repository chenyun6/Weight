package com.cy.app.weight;

import com.common.tools.core.dto.ResultDTO;
import com.cy.client.weight.WeightRpcService;
import com.cy.client.weight.query.SendCodeDTO;
import com.cy.client.weight.query.LoginDTO;
import com.cy.client.weight.query.WeightRecordCreateDTO;
import com.cy.app.weight.assembler.SendCodeDTO2SendCodeCmdConvert;
import com.cy.app.weight.assembler.LoginDTO2LoginCmdConvert;
import com.cy.app.weight.assembler.WeightRecordCreateDTO2WeightRecordCreateCmdConvert;
import com.cy.domain.weight.user.login.LoginCmdHandler;
import com.cy.domain.weight.user.sendcode.SendCodeCmdHandler;
import com.cy.domain.weight.weightrecord.create.WeightRecordCreateCmdHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Override
    public ResultDTO<String> sendCode(SendCodeDTO dto) {
        String code = sendCodeCmdHandler.handle(SendCodeDTO2SendCodeCmdConvert.INSTANCE.dto2Do(dto));
        return ResultDTO.success(code);
    }

    @Override
    public ResultDTO<Long> login(LoginDTO dto) {
        Long userId = loginCmdHandler.handle(LoginDTO2LoginCmdConvert.INSTANCE.dto2Do(dto));
        return ResultDTO.success(userId);
    }

    @Override
    public ResultDTO<Long> createRecord(WeightRecordCreateDTO dto) {
        Long id = weightRecordCreateCmdHandler.handle(WeightRecordCreateDTO2WeightRecordCreateCmdConvert.INSTANCE.dto2Do(dto));
        return ResultDTO.success(id);
    }
}
