package cn.iocoder.yudao.module.stucms.controller.admin.course.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 课程 Excel VO
 *
 * @author hua
 */
@Data
public class CourseExcelVO {

    @ExcelProperty("主键")
    private Long courseId;

    @ExcelProperty("名称")
    private String courseName;

    @ExcelProperty("总分")
    private Integer courseFull;

    @ExcelProperty("授课老师")
    private Long teacherId;

}
