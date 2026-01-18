package com.sanzhong.adapter.weight;

import com.common.tools.core.dto.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Weighty - 体重管理-APP端
 *
 * @author visual-ddd
 * @since 1.0
 */
@RestController
@RequestMapping
@Api(tags = "测试接口")
public class TestController {

    @ApiOperation("ok")
    @GetMapping("/ok")
    public ResultDTO<String> ok() {
        return ResultDTO.success("ok");
    }

}
