package cn.iocoder.yudao.module.stucms.dal.dataobject.score;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stucms_score")
@KeySequence("stucms_score_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "score_id")
    private Long scoreId;
    /**
     * 成绩
     */
    private BigDecimal score;
    /**
     * 学生ID
     */
    private Long studentId;
    /**
     * 考试ID
     */
    private Long examId;
    /**
     * 课程ID
     */
    private Long courseId;
}
