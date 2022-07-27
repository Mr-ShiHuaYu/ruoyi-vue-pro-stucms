package cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 老师精简信息  Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSimpleRespVO {

    @ApiModelProperty(value = "主键", required = true)
    private Long teacherId;

    @ApiModelProperty(value = "名称", required = true)
    @NotNull(message = "老师名称不能为空")
    private String name;

}
