package cn.iocoder.yudao.module.stucms.controller.admin.course.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "管理后台 - 课程 Excel 导出 Request VO", description = "参数和 CoursePageReqVO 是一致的")
@Data
public class CourseExportReqVO {

    @ApiModelProperty(value = "名称", example = "语文")
    private String courseName;

    @ApiModelProperty(value = "总分", example = "100")
    private Integer courseFull;

    @ApiModelProperty(value = "授课老师", example = "张三")
    private Long teacherId;

}
