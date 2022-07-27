package cn.iocoder.yudao.module.stucms.dal.dataobject.course;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 课程 DO
 *
 * @author hua
 */
@TableName("stucms_course")
@KeySequence("stucms_course_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDO extends BaseDO {

    private static final long serialVersionUID = -224780569990039286L;
    /**
     * 主键
     */
    @TableId
    private Long courseId;
    /**
     * 名称
     */
    private String courseName;
    /**
     * 总分
     */
    private Integer courseFull;
    /**
     * 授课老师
     */
    private Long teacherId;

}
