package cn.iocoder.yudao.module.stucms.controller.admin.student.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
* 学生管理 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class StudentBaseVO {

    @ApiModelProperty(value = "学号", required = true, example = "3042013024")
    @NotNull(message = "学号不能为空")
    private String studentUid;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    @NotNull(message = "姓名不能为空")
    private String studentName;

    @ApiModelProperty(value = "性别", required = true, example = "男")
    @NotNull(message = "性别不能为空")
    private String sex;

    @ApiModelProperty(value = "手机", required = true, example = "18179871100")
    @NotNull(message = "手机不能为空")
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = TIME_ZONE_DEFAULT)
    @ApiModelProperty(value = "出生日期", required = true)
    @NotNull(message = "出生日期不能为空")
    private Date birth;

    @ApiModelProperty(value = "民族", required = true)
    @NotNull(message = "民族不能为空")
    private String minzu;

    @ApiModelProperty(value = "寄宿", required = true, example = "寄宿")
    @NotNull(message = "寄宿不能为空")
    private String jishu;

    @ApiModelProperty(value = "留守儿童", required = true, example = "是")
    @NotNull(message = "留守儿童不能为空")
    private Integer liushou;

}
