package cn.iocoder.yudao.module.stucms.api.teacher;

import cn.iocoder.yudao.module.stucms.api.teacher.dto.TeacherRespDTO;

public interface TeacherApi {
    TeacherRespDTO getTeacherByPhone(String phone);

    TeacherRespDTO getTeacherById(Long teacherId);
}
