package cn.iocoder.yudao.module.stucms.controller.admin.course.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("管理后台 - 课程分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CoursePageReqVO extends PageParam {

    private static final long serialVersionUID = -8816220897081497823L;
    @ApiModelProperty(value = "名称", example = "语文")
    private String courseName;

    @ApiModelProperty(value = "总分", example = "100")
    private Integer courseFull;

    @ApiModelProperty(value = "授课老师ID", example = "1024")
    private Long teacherId;

}
