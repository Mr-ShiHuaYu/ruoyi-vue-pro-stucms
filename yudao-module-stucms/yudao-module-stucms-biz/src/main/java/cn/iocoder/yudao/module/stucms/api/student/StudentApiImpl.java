package cn.iocoder.yudao.module.stucms.api.student;

import cn.iocoder.yudao.module.stucms.api.student.dto.StudentRespDTO;
import cn.iocoder.yudao.module.stucms.convert.student.StudentConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.stucms.service.student.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
public class StudentApiImpl implements StudentApi {
    @Resource
    private StudentService studentService;

    @Override
    public StudentRespDTO getStudentByUid(String username) {
        StudentDO studentDO = this.studentService.getStudentByUid(username);
        return StudentConvert.INSTANCE.convertDto(studentDO);
    }

    @Override
    public StudentRespDTO getStudentById(Long studentId) {
        StudentDO studentDO = this.studentService.getStudent(studentId);
        return StudentConvert.INSTANCE.convertDto(studentDO);
    }
}
