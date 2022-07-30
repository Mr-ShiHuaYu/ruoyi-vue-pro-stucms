package cn.iocoder.yudao.module.stucms.controller.admin.exam.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 考试 Excel 导出 Request VO", description = "参数和 ExamPageReqVO 是一致的")
@Data
public class ExamExportReqVO {

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "考试日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] examTime;

}
