package cn.iocoder.yudao.module.stucms.dal.dataobject.exam;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 考试 DO
 *
 * @author hua
 */
@TableName("stucms_exam")
@KeySequence("stucms_exam_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long examId;
    /**
     * 考试名称
     */
    private String examName;
    /**
     * 考试时间
     */
    private Date examTime;

}
