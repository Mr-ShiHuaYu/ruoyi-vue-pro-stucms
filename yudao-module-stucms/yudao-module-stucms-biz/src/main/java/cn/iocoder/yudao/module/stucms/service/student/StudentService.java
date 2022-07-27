package cn.iocoder.yudao.module.stucms.service.student;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.*;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 学生管理 Service 接口
 *
 * @author 华
 */
public interface StudentService {

    /**
     * 创建学生管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStudent(@Valid StudentCreateReqVO createReqVO);

    /**
     * 更新学生管理
     *
     * @param updateReqVO 更新信息
     */
    void updateStudent(@Valid StudentUpdateReqVO updateReqVO);

    /**
     * 删除学生管理
     *
     * @param id 编号
     */
    void deleteStudent(Long id);

    /**
     * 获得学生管理
     *
     * @param id 编号
     * @return 学生管理
     */
    StudentDO getStudent(Long id);

    /**
     * 获得学生管理列表
     *
     * @param ids 编号
     * @return 学生管理列表
     */
    List<StudentDO> getStudentList(Collection<Long> ids);

    /**
     * 获得学生管理分页
     *
     * @param pageReqVO 分页查询
     * @return 学生管理分页
     */
    PageResult<StudentDO> getStudentPage(StudentPageReqVO pageReqVO);

    /**
     * 获得学生管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 学生管理列表
     */
    List<StudentDO> getStudentList(StudentExportReqVO exportReqVO);

}
