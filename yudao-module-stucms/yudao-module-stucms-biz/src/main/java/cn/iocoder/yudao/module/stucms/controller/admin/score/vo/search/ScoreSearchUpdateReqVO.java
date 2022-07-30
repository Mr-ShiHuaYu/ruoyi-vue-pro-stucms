package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("管理后台 - 成绩查询分页 Request VO")
@Data
public class ScoreSearchUpdateReqVO {

    @ApiModelProperty(value = "考试ID")
    @NotNull(message = "考试ID不能为空")
    private Long examId;

    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @ApiModelProperty(value = "学生ID")
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @ApiModelProperty(value = "分数")
    @NotNull(message = "分数不能为空")
    @DecimalMin(value = "0", message = "分数不能小于0")
    private BigDecimal score;
}
