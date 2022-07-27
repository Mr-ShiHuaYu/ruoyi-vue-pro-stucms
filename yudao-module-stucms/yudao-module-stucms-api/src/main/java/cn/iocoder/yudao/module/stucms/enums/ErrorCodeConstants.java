package cn.iocoder.yudao.module.stucms.enums;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Stucms 错误码枚举类
 *
 * stucms 系统，使用 1-010-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 学生管理 1010001001 ==========
    ErrorCode STUDENT_NOT_EXISTS = new ErrorCode(1010001001, "学生管理不存在");


    // ========== 课程 1010002001 ==========
    ErrorCode COURSE_NOT_EXISTS = new ErrorCode(1010002001, "课程不存在");
    ErrorCode COURSE_COURSE_NAME_EXISTS = new ErrorCode(1010002002, "课程名称:【{}】已经存在");
    // ========== 老师 1010003001 ==========
    ErrorCode TEACHER_NOT_EXISTS = new ErrorCode(1010003001, "老师不存在");


}
