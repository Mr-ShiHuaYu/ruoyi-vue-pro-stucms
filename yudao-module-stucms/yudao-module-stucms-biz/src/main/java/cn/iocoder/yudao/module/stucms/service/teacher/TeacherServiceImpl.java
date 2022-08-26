package cn.iocoder.yudao.module.stucms.service.teacher;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherUpdateReqVO;
import cn.iocoder.yudao.module.stucms.convert.teacher.TeacherConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.teacher.TeacherMapper;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.enums.permission.RoleCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.TEACHER_NOT_EXISTS;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.TEACHER_UPDATE_ERROR;


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
    @Resource
    private PermissionApi permissionApi;
    @Resource
    private AdminUserApi adminUserApi;

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
        // 校验更新
        this.validateTeacherUpdate(updateReqVO);
        // 更新
        TeacherDO updateObj = TeacherConvert.INSTANCE.convert(updateReqVO);
        // 更新之前要先保留一下旧的手机号
        TeacherDO teacherDO = teacherMapper.selectById(updateObj.getTeacherId());
        String oldPhone = teacherDO.getPhone();
        String newPhone = updateObj.getPhone();
        teacherMapper.updateById(updateObj);
        // 更新老师后，同步修改用户表的姓名，手机号等信息
        this.updateUserByTeacherPhone(oldPhone, newPhone);
    }

    private void updateUserByTeacherPhone(String oldPhone, String newPhone) {
        adminUserApi.updateUserByTeacherPhone(oldPhone, newPhone);
    }

    /**
     * 检查是否能更新老师
     * 1.非管理员不能修改老师的手机号
     *
     * @param updateReqVO 老师更新 Request VO
     */
    private void validateTeacherUpdate(TeacherUpdateReqVO updateReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        boolean isSuperAdmin = permissionApi.hasAnyRoles(userId, RoleCodeEnum.SUPER_ADMIN.getCode());
        if (!isSuperAdmin) {
            // 不是管理员
            TeacherDO teacherDO = teacherMapper.selectById(updateReqVO.getTeacherId());
            if (!StrUtil.equals(teacherDO.getPhone(), updateReqVO.getPhone())) {
                throw ServiceExceptionUtil.exception(TEACHER_UPDATE_ERROR, "手机号");
            }
        }
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

    @Override
    public TeacherDO getTeacherByPhone(String phone) {
        return teacherMapper.selectOneByPhone(phone);
    }

}
