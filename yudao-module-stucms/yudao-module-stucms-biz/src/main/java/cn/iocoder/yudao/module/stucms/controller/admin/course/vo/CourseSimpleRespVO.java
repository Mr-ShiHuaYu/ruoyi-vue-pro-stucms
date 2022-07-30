package cn.iocoder.yudao.module.stucms.controller.admin.course.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 课程名称 Response VO")
@Data
public class CourseSimpleRespVO {
    @ApiModelProperty(value = "主键", required = true)
    private Long courseId;

    @ApiModelProperty(value = "总分", required = true, example = "100")
    @NotNull(message = "总分不能为空")
    private Integer courseFull;


    @ApiModelProperty(value = "名称", required = true, example = "语文")
    @NotNull(message = "名称不能为空")
    private String courseName;
}
