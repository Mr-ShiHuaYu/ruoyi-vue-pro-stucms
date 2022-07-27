package cn.iocoder.yudao.module.stucms.controller.admin.student.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ApiModel("管理后台 - 学生管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentCreateReqVO extends StudentBaseVO {

    @ApiModelProperty(value = "身份证号", required = true)
    @NotNull(message = "身份证号不能为空")
    private String sysid;

    @ApiModelProperty(value = "户口")
    private String hukou;

    @ApiModelProperty(value = "户籍地址")
    private String huji;

    @ApiModelProperty(value = "现住址")
    private String xianzz;

    @ApiModelProperty(value = "毕业学校")
    private String biye;

    @ApiModelProperty(value = "获奖情况")
    private String huojiang;

}
