package cn.iocoder.yudao.module.stucms.service.teacher;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherUpdateReqVO;
import cn.iocoder.yudao.module.stucms.convert.teacher.TeacherConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.teacher.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.TEACHER_NOT_EXISTS;


/**
 * 老师 Service 实现类
 *
 * @author hua
 */
@Service
@Validated
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public Long createTeacher(TeacherCreateReqVO createReqVO) {
        // 插入
        TeacherDO teacher = TeacherConvert.INSTANCE.convert(createReqVO);
        teacherMapper.insert(teacher);
        // 返回
        return teacher.getTeacherId();
    }

    @Override
    public void updateTeacher(TeacherUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateTeacherExists(updateReqVO.getTeacherId());
        // 更新
        TeacherDO updateObj = TeacherConvert.INSTANCE.convert(updateReqVO);
        teacherMapper.updateById(updateObj);
    }

    @Override
    public void deleteTeacher(Long id) {
        // 校验存在
        this.validateTeacherExists(id);
        // 删除
        teacherMapper.deleteById(id);
    }

    private void validateTeacherExists(Long id) {
        if (teacherMapper.selectById(id) == null) {
            throw exception(TEACHER_NOT_EXISTS);
        }
    }

    @Override
    public TeacherDO getTeacher(Long id) {
        return teacherMapper.selectById(id);
    }

    @Override
    public List<TeacherDO> getTeacherList(Collection<Long> ids) {
        return teacherMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<TeacherDO> getTeacherPage(TeacherPageReqVO pageReqVO) {
        return teacherMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TeacherDO> getTeacherList(TeacherExportReqVO exportReqVO) {
        return teacherMapper.selectList(exportReqVO);
    }

    @Override
    public List<TeacherDO> getSimpleTeachers() {
        return teacherMapper.selectList();
    }

}
