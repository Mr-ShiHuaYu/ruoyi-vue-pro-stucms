package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


/**
 * @author ysh
 * @date 2022-05-13
 */
@Data
@ApiModel("总体分析点击课程图表对象")
@Builder
public class ScoreAllChartPieRespVo {

    @ApiModelProperty("饼图块名称")
    private String name;

    @ApiModelProperty("饼图块值")
    private Long value;
}
