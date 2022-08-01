package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author ysh
 * @date 2022-05-13
 */
@Data
@ApiModel("总体分析视图对象")
public class ScoreAllCoursePieReqVO {

    @ApiModelProperty("课程ID")
    @NotNull(message = "课程ID不能为空")
    private Long cid;

    @ApiModelProperty("考试ID")
    @NotNull(message = "考试ID不能为空")
    private Long eid;
}
