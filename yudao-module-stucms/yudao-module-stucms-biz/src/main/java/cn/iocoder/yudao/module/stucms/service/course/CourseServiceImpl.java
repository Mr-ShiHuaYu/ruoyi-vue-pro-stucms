package cn.iocoder.yudao.module.stucms.service.course;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.*;
import cn.iocoder.yudao.module.stucms.convert.course.CourseConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.course.CourseMapper;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.COURSE_COURSE_NAME_EXISTS;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.COURSE_NOT_EXISTS;


/**
 * 课程 Service 实现类
 *
 * @author hua
 */
@Service
@Validated
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public Long createCourse(CourseCreateReqVO createReqVO) {
        // 校验
        this.checkCreateOrUpdate(null, createReqVO.getCourseName());
        // 插入
        CourseDO course = CourseConvert.INSTANCE.convert(createReqVO);
        this.courseMapper.insert(course);
        // 返回
        return course.getCourseId();
    }

    @Override
    public void updateCourse(CourseUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateCourseExists(updateReqVO.getCourseId());
        // 校验新名称是否重复
        this.checkCreateOrUpdate(updateReqVO.getCourseId(), updateReqVO.getCourseName());
        // 更新
        CourseDO updateObj = CourseConvert.INSTANCE.convert(updateReqVO);
        this.courseMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourse(Collection<Long> ids) {
        // 校验存在
        if (CollUtil.isEmpty(ids)) {
            throw exception(COURSE_NOT_EXISTS);
        }
        ids.forEach(this::validateCourseExists);
        // 删除
        this.courseMapper.deleteBatchIds(ids);
    }

    @Override
    public void deleteCourse(Long id) {
        // 校验存在
        this.validateCourseExists(id);
        // 删除
        this.courseMapper.deleteById(id);
    }

    /**
     * 根据课程ID校验课程是否存在,不存在就抛出异常
     *
     * @param id
     */
    private void validateCourseExists(Long id) {
        if (this.courseMapper.selectById(id) == null) {
            throw exception(COURSE_NOT_EXISTS);
        }
    }

    @Override
    public CourseDO getCourse(Long id) {
        return this.courseMapper.selectById(id);
    }

    @Override
    public List<CourseDO> getCourseList(Collection<Long> ids) {
        return this.courseMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CourseDO> getCoursePage(CoursePageReqVO pageReqVO) {
        return this.courseMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CourseDO> getCourseList(CourseExportReqVO exportReqVO) {
        return this.courseMapper.selectList(exportReqVO);
    }

    @Override
    public List<CourseDO> getCourseList() {
        return this.courseMapper.selectList();
    }

    private void checkCreateOrUpdate(Long id, String courseName) {
        this.checkCourseNameUnique(id, courseName);
    }

    /**
     * 有传入id时为更新
     * 没有传入id时为增加
     *
     * @param id
     * @param courseName
     */
    @VisibleForTesting
    public void checkCourseNameUnique(Long id, String courseName) {
        if (StrUtil.isBlank(courseName)) {
            return;
        }
        CourseDO course = this.courseMapper.selectByCourseName(courseName);
        if (course == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id,直接抛出异常
        if (id == null) {
            throw exception(COURSE_COURSE_NAME_EXISTS, courseName);
        }
        if (!course.getCourseId().equals(id)) {
            // 如果有传入ID,根据课程名称获取到的course的id和传入的id不一致
            // 场景:一条数据,更新的时候更新成了其他课程的名称,也是不允许的
            throw exception(COURSE_COURSE_NAME_EXISTS, courseName);
        }
    }
}
