package cn.iocoder.yudao.module.stucms.controller.admin.student.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 学生管理 Excel VO
 *
 * @author 华
 */
@Data
public class StudentExcelVO {

    @ExcelProperty(value = "主键")
    private Long id;

    @ExcelProperty("学号")
    private String studentUid;

    @ExcelProperty("姓名")
    private String studentName;

    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private String sex;

    @ExcelProperty("手机")
    private String phone;

    @ExcelProperty("出生日期")
    private Date birth;

    @ExcelProperty("民族")
    private String minzu;

    @ExcelProperty(value = "寄宿", converter = DictConvert.class)
    @DictFormat(cn.iocoder.yudao.module.stucms.enums.DictTypeConstants.STUCMS_JISHU_TYPE)
    private String jishu;

    @ExcelProperty(value = "留守儿童", converter = DictConvert.class)
    @DictFormat(cn.iocoder.yudao.module.stucms.enums.DictTypeConstants.STUCMS_LIUSHOU_TYPE)
    private Integer liushou;

    @ExcelProperty("身份证号")
    private String sysid;

    @ExcelProperty("户口")
    private String hukou;

    @ExcelProperty("户籍地址")
    private String huji;

    @ExcelProperty("现住址")
    private String xianzz;

    @ExcelProperty("毕业学校")
    private String biye;

    @ExcelProperty("获奖情况")
    private String huojiang;

    @ExcelProperty("创建时间")
    private Date createTime;

}
