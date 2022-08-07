package cn.iocoder.yudao.module.stucms.dal.dataobject.student;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.stucms.enums.student.JishuEnum;
import cn.iocoder.yudao.module.stucms.enums.student.LiuShouEnum;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * 学生管理 DO
 *
 * @author 华
 */
@TableName("stucms_student")
@KeySequence("stucms_student_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDO extends BaseDO {

    private static final long serialVersionUID = 612895446097884353L;
    /**
     * 主键
     */
    @TableId("student_id")
    private Long id;

    /**
     * 班级ID
     */
    private Long deptId;
    /**
     * 学号
     */
    private String studentUid;
    /**
     * 姓名
     */
    private String studentName;
    /**
     * 性别
     *
     * 枚举 {@link DictTypeConstants#USER_SEX}
     */
    private String sex;
    /**
     * 手机
     */
    private String phone;
    /**
     * 身份证号
     */
    private String sysid;
    /**
     * 出生日期
     */
    private Date birth;
    /**
     * 民族
     */
    private String minzu;
    /**
     * 户口
     */
    private String hukou;
    /**
     * 寄宿
     *
     * 枚举 {@link JishuEnum}
     */
    private String jishu;
    /**
     * 户籍地址
     */
    private String huji;
    /**
     * 现住址
     */
    private String xianzz;
    /**
     * 留守儿童
     *
     * 枚举 {@link LiuShouEnum}
     */
    private Integer liushou;
    /**
     * 毕业学校
     */
    private String biye;
    /**
     * 获奖情况
     */
    private String huojiang;

}
