package cn.iocoder.yudao.module.stucms.controller.admin.student.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("管理后台 - 学生管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentSimplePageReqVO extends PageParam {

    private static final long serialVersionUID = 3439724210754732948L;
    @ApiModelProperty(value = "学号", example = "3042013024")
    private String studentUid;

    @ApiModelProperty(value = "姓名", example = "张三")
    private String studentName;
}
