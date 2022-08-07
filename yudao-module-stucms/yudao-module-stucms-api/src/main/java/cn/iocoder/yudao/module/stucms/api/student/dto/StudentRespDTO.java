package cn.iocoder.yudao.module.stucms.api.student.dto;

import lombok.Data;

/**
 * 学生管理 DO
 *
 * @author 华
 */
@Data
public class StudentRespDTO{

    /**
     * 主键
     */
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
     */
    private String sex;
}
