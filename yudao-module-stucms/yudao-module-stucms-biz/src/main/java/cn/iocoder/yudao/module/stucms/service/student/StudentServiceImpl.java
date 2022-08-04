package cn.iocoder.yudao.module.stucms.service.student;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.*;
import cn.iocoder.yudao.module.stucms.convert.student.StudentConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.student.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;

/**
 * 学生管理 Service 实现类
 *
 * @author 华
 */
@Service
@Validated
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Override
    public Long createStudent(StudentCreateReqVO createReqVO) {
        // 插入
        StudentDO student = StudentConvert.INSTANCE.convert(createReqVO);
        this.studentMapper.insert(student);
        // 返回
        return student.getId();
    }

    @Override
    public void updateStudent(StudentUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateStudentExists(updateReqVO.getId());
        // 更新
        StudentDO updateObj = StudentConvert.INSTANCE.convert(updateReqVO);
        this.studentMapper.updateById(updateObj);
    }

    @Override
    public void deleteStudent(Long id) {
        // 校验存在
        this.validateStudentExists(id);
        // 删除
        this.studentMapper.deleteById(id);
    }

    private void validateStudentExists(Long id) {
        if (this.studentMapper.selectById(id) == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
    }

    @Override
    public StudentDO getStudent(Long id) {
        return this.studentMapper.selectById(id);
    }

    @Override
    public PageResult<StudentDO> getStudentPage(StudentPageReqVO pageReqVO) {
        return this.studentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StudentDO> getStudentList(StudentExportReqVO exportReqVO) {
        return this.studentMapper.selectList(exportReqVO);
    }

    @Override
    public PageResult<StudentDO> getStudentSimplePage(StudentSimplePageReqVO reqVO) {
        return this.studentMapper.selectSimplePage(reqVO);
    }

}
