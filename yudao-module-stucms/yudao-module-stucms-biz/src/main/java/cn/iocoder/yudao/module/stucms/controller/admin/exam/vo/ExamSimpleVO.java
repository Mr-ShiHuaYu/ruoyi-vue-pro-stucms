package cn.iocoder.yudao.module.stucms.controller.admin.exam.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 考试名称 Response VO")
@Data
@ToString(callSuper = true)
public class ExamSimpleVO {

    @ApiModelProperty(value = "主键", required = true)
    private Long examId;

    @ApiModelProperty(value = "考试名称", required = true)
    @NotNull(message = "考试名称不能为空")
    private String examName;
}
