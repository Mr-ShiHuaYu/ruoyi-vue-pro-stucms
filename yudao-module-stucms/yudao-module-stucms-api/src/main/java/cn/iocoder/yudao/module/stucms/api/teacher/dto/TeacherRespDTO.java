package cn.iocoder.yudao.module.stucms.api.teacher.dto;

import lombok.Data;

@Data
public class TeacherRespDTO {
    /**
     * 主键
     */
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
     */
    private String sex;
    /**
     * QQ
     */
    private String qq;
}
