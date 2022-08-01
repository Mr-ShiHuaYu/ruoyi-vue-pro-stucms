package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 总体分析 Request VO")
@Data
public class ScoreSearchAllReqVO {
    @ApiModelProperty(value = "考试ID")
    @NotNull(message = "考试ID不能为空")
    private Long examId;
}
