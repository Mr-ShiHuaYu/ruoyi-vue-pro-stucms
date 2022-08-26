package cn.iocoder.yudao.module.stucms.service.student;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.*;
import cn.iocoder.yudao.module.stucms.convert.student.StudentConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.student.StudentMapper;
import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.enums.permission.RoleCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.STUDENT_UPDATE_ERROR;

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
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private PermissionApi permissionApi;

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
        // 校验更新学生
        this.validateStudentUpdate(updateReqVO);
        // 更新
        StudentDO updateObj = StudentConvert.INSTANCE.convert(updateReqVO);
        // 更新前获取学生的旧学号
        String oldUid = getStudent(updateObj.getId()).getStudentUid();
        String newUid = updateObj.getStudentUid();
        this.studentMapper.updateById(updateObj);
        this.updateUserByStudentUid(oldUid, newUid);
    }

    private void updateUserByStudentUid(String oldUid, String newUid) {
        adminUserApi.updateUserByStudentUid(oldUid, newUid);
    }

    /**
     * 检查是否能更新学生
     * //1.不是管理员不能修改学号
     * //2.非管理员不能修改姓名
     *
     * @param updateReqVO
     */
    private void validateStudentUpdate(StudentUpdateReqVO updateReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        boolean isSuperAdmin = permissionApi.hasAnyRoles(userId, RoleCodeEnum.SUPER_ADMIN.getCode());
        if (!isSuperAdmin) {
            // 如果不是超级管理员
            StudentDO studentDO = studentMapper.selectById(updateReqVO.getId());
            if (!StrUtil.equals(studentDO.getStudentUid(), updateReqVO.getStudentUid())) {
                // 前端的学号和数据库中的学号不相等，说明前端修改了学号
                throw exception(STUDENT_UPDATE_ERROR, "学号");
            }
            // 非管理员不能修改姓名
            if (!StrUtil.equals(studentDO.getStudentName(), updateReqVO.getStudentName())) {
                // 前端的学号和数据库中的学号不相等，说明前端修改了学号
                throw exception(STUDENT_UPDATE_ERROR, "姓名");
            }
        }
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
        return this.studentMapper.selectPage(pageReqVO, this.adminUserApi.getDeptCondition(pageReqVO.getDeptId()));
    }

    @Override
    public List<StudentDO> getStudentList(StudentExportReqVO exportReqVO) {
        return this.studentMapper.selectList(exportReqVO);
    }

    @Override
    public PageResult<StudentDO> getStudentSimplePage(StudentSimplePageReqVO reqVO) {
        return this.studentMapper.selectSimplePage(reqVO);
    }

    @Override
    public StudentDO getStudentByUid(String username) {
        return this.studentMapper.selectOneByUid(username);
    }
}
