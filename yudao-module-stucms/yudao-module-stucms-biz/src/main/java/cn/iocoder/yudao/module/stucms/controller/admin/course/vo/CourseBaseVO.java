package cn.iocoder.yudao.module.stucms.controller.admin.course.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 课程 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class CourseBaseVO {

    @ApiModelProperty(value = "名称", required = true, example = "语文")
    @NotNull(message = "名称不能为空")
    private String courseName;

    @ApiModelProperty(value = "总分", required = true, example = "100")
    @NotNull(message = "总分不能为空")
    private Integer courseFull;

    @ApiModelProperty(value = "授课老师", required = true, example = "1")
    @NotNull(message = "授课老师不能为空")
    private Long teacherId;

}
