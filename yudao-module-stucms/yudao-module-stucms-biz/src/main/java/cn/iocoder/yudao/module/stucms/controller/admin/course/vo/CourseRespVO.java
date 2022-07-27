package cn.iocoder.yudao.module.stucms.controller.admin.course.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 课程 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CourseRespVO extends CourseBaseVO {

    @ApiModelProperty(value = "主键", required = true)
    private Long courseId;

    private Teacher teacher;

    @ApiModel("老师")
    @Data
    public static class Teacher {
        @ApiModelProperty(value = "主键", required = true)
        private Long teacherId;

        @ApiModelProperty(value = "名称", required = true)
        @NotNull(message = "老师名称不能为空")
        private String name;

    }

}
