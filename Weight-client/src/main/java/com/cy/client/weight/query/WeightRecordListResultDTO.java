package com.cy.client.weight.query;

import com.cy.client.weight.dto.WeightRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 体重记录列表结果DTO
 *
 * @author visual-ddd
 * @since 1.0
 */
@Data
@ApiModel(description = "体重记录列表结果")
public class WeightRecordListResultDTO {

    @ApiModelProperty(value = "记录列表")
    private List<WeightRecordDTO> list;

    @ApiModelProperty(value = "总数")
    private Integer total;
}
