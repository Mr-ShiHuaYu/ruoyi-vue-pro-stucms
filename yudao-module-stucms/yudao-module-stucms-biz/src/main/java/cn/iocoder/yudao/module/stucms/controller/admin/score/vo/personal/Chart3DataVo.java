package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chart3DataVo {
    @ApiModelProperty("总分")
    private BigDecimal value;

    @ApiModelProperty("排名")
    private Long rank;
}
