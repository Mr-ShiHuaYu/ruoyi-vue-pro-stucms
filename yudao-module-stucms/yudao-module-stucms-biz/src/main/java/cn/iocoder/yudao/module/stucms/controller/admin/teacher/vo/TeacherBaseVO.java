package cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
* 老师 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class TeacherBaseVO {

    @ApiModelProperty(value = "名称", required = true)
    @NotNull(message = "老师名称不能为空")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）", example = "男")
    private String sex;

    @ApiModelProperty(value = "QQ")
    private String qq;

}
