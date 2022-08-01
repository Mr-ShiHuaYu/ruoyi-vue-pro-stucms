package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author ysh
 * @date 2022-05-13
 */
@Data
@ApiModel("总体分析视图对象")
public class ScoreAllRespVo {

    @ApiModelProperty("课程名称")
    private String course;

    @ApiModelProperty("课程ID")
    private Long cid;

    @ApiModelProperty("考试ID")
    private Long eid;

    @ApiModelProperty("考试名称")
    private String exam;

    @ApiModelProperty("满分")
    private Integer full;

    @ApiModelProperty("参考人数")
    private Integer joinNum;

    @ApiModelProperty("优秀人数")
    private Integer youxiu;

    @ApiModelProperty("良好人数")
    private Integer lianghao;

    @ApiModelProperty("及格人数")
    private Integer jige;

    @ApiModelProperty("不及格人数")
    private Integer bujige;

    @ApiModelProperty("标准差")
    private BigDecimal std;

    @ApiModelProperty("平均分")
    private BigDecimal avg;

    @ApiModelProperty("最高分")
    private BigDecimal max;

    @ApiModelProperty("最低分")
    private BigDecimal min;
}
