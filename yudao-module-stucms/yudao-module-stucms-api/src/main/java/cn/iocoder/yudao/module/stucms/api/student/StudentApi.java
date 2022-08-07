package cn.iocoder.yudao.module.stucms.api.student;

import cn.iocoder.yudao.module.stucms.api.student.dto.StudentRespDTO;

public interface StudentApi {
    StudentRespDTO getStudentByUid(String username);
}
