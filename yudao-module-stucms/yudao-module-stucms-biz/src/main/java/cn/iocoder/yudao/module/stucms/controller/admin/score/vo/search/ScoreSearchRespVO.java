package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("成绩视图对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreSearchRespVO {

    @ApiModelProperty("主键")
    private Long scoreId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("考试名称")
    private String examName;

    @ApiModelProperty("学生学号")
    private String studentUid;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("考试ID")
    private Long examId;

    @ApiModelProperty("平均分")
    private BigDecimal avg;

    @ApiModelProperty("总分")
    private BigDecimal sum;

    @ApiModelProperty("标准差")
    private BigDecimal std;
}
