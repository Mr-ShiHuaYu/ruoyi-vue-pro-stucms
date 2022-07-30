package cn.iocoder.yudao.module.stucms.controller.admin.exam.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 考试 Excel VO
 *
 * @author hua
 */
@Data
public class ExamExcelVO {

    @ExcelProperty("主键")
    private Long examId;

    @ExcelProperty("考试名称")
    private String examName;

    @ExcelProperty("考试日期")
    private Date examTime;

    @ExcelProperty("创建时间")
    private Date createTime;

}
