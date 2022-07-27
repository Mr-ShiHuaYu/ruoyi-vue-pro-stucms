package cn.iocoder.yudao.module.stucms.controller.admin.exam.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

@ApiModel("管理后台 - 考试 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExamRespVO extends ExamBaseVO {

    @ApiModelProperty(value = "主键", required = true)
    private Long examId;

    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

}
