package cn.iocoder.yudao.module.stucms.service.course;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CoursePageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 课程 Service 接口
 *
 * @author hua
 */
public interface CourseService {

    /**
     * 创建课程
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourse(@Valid CourseCreateReqVO createReqVO);

    /**
     * 更新课程
     *
     * @param updateReqVO 更新信息
     */
    void updateCourse(@Valid CourseUpdateReqVO updateReqVO);

    /**
     * 批量删除课程
     *
     * @param ids 编号
     */
    void deleteCourse(Collection<Long> ids);

    /**
     * 删除课程
     *
     * @param id 编号
     */
    void deleteCourse(Long id);

    /**
     * 获得课程
     *
     * @param id 编号
     * @return 课程
     */
    CourseDO getCourse(Long id);

    /**
     * 获得课程列表
     *
     * @param ids 编号
     * @return 课程列表
     */
    List<CourseDO> getCourseList(Collection<Long> ids);

    /**
     * 获得课程分页
     *
     * @param pageReqVO 分页查询
     * @return 课程分页
     */
    PageResult<CourseDO> getCoursePage(CoursePageReqVO pageReqVO);

    /**
     * 获得课程列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 课程列表
     */
    List<CourseDO> getCourseList(CourseExportReqVO exportReqVO);

    List<CourseDO> getCourseList();

    Integer getCourseFullById(Long cid);
}
