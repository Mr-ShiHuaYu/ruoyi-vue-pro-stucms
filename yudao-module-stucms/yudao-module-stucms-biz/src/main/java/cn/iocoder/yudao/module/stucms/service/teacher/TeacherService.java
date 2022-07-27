package cn.iocoder.yudao.module.stucms.service.teacher;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 老师 Service 接口
 *
 * @author hua
 */
public interface TeacherService {

    /**
     * 创建老师
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTeacher(@Valid TeacherCreateReqVO createReqVO);

    /**
     * 更新老师
     *
     * @param updateReqVO 更新信息
     */
    void updateTeacher(@Valid TeacherUpdateReqVO updateReqVO);

    /**
     * 删除老师
     *
     * @param id 编号
     */
    void deleteTeacher(Long id);

    /**
     * 获得老师
     *
     * @param id 编号
     * @return 老师
     */
    TeacherDO getTeacher(Long id);

    /**
     * 获得老师列表
     *
     * @param ids 编号
     * @return 老师列表
     */
    List<TeacherDO> getTeacherList(Collection<Long> ids);

    /**
     * 获得老师分页
     *
     * @param pageReqVO 分页查询
     * @return 老师分页
     */
    PageResult<TeacherDO> getTeacherPage(TeacherPageReqVO pageReqVO);

    /**
     * 获得老师列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 老师列表
     */
    List<TeacherDO> getTeacherList(TeacherExportReqVO exportReqVO);

    List<TeacherDO> getSimpleTeachers();
}
