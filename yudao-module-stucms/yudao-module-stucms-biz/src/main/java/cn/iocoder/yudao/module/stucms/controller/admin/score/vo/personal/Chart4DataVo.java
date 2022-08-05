package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chart4DataVo {
    @ApiModelProperty("总分")
    private BigDecimal sumScore;

    @ApiModelProperty("排名")
    private Long value;
}
