package cn.iocoder.yudao.module.stucms.controller.admin.student.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("管理后台 - 学生管理 Response VO")
@Data
public class StudentSimpleRespVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "学号", required = true, example = "3042013024")
    private String studentUid;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String studentName;

}
