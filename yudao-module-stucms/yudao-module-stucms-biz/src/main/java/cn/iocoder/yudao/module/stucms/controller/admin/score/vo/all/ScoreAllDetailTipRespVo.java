package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


@Data
@ApiModel("总体分析显示具体学生姓名及成绩对象")
@Builder
public class ScoreAllDetailTipRespVo {

    @ApiModelProperty("学生姓名")
    private String name;

    @ApiModelProperty("分数")
    private String score;
}
