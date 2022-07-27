package cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 老师 Excel VO
 *
 * @author hua
 */
@Data
public class TeacherExcelVO {

    @ExcelProperty("主键")
    private Long teacherId;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("电话")
    private String phone;

    @ExcelProperty(value = "用户性别（0男 1女 2未知）", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private String sex;

    @ExcelProperty("QQ")
    private String qq;

    @ExcelProperty("创建时间")
    private Date createTime;

}
