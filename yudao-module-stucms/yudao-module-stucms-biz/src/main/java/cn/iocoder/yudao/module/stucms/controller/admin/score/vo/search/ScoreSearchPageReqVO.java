package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("管理后台 - 成绩查询分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScoreSearchPageReqVO extends PageParam {
    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学生学号")
    private String studentUid;

    @ApiModelProperty(value = "考试ID")
    private Long examId;
}
