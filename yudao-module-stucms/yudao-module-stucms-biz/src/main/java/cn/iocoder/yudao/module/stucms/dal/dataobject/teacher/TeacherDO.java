package cn.iocoder.yudao.module.stucms.dal.dataobject.teacher;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.system.enums.common.SexEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 老师 DO
 *
 * @author hua
 */
@TableName("stucms_teacher")
@KeySequence("stucms_teacher_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDO extends BaseDO {

    private static final long serialVersionUID = -1808908739376567112L;
    /**
     * 主键
     */
    @TableId
    private Long teacherId;
    /**
     * 名称
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 性别（1男 2女 3未知）
     * <p>
     * 枚举 {@link SexEnum}
     */
    private String sex;
    /**
     * QQ
     */
    private String qq;

}
