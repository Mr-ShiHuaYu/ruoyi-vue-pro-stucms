package cn.iocoder.yudao.module.stucms.controller.admin.exam.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
* 考试 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class ExamBaseVO {

    @ApiModelProperty(value = "考试名称", required = true)
    @NotNull(message = "考试名称不能为空")
    private String examName;

    @ApiModelProperty(value = "考试日期", required = true)
    @NotNull(message = "考试日期不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date examTime;

}
