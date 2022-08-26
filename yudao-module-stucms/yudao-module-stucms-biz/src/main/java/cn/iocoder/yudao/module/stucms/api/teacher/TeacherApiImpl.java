package cn.iocoder.yudao.module.stucms.api.teacher;

import cn.iocoder.yudao.module.stucms.api.teacher.dto.TeacherRespDTO;
import cn.iocoder.yudao.module.stucms.convert.teacher.TeacherConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.service.teacher.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
public class TeacherApiImpl implements TeacherApi {
    @Resource
    private TeacherService teacherService;

    @Override
    public TeacherRespDTO getTeacherByPhone(String phone) {
        TeacherDO teacherDO = teacherService.getTeacherByPhone(phone);
        return TeacherConvert.INSTANCE.convertDTO(teacherDO);
    }

    @Override
    public TeacherRespDTO getTeacherById(Long teacherId) {
        TeacherDO teacher = teacherService.getTeacher(teacherId);
        return TeacherConvert.INSTANCE.convertDTO(teacher);
    }
}
