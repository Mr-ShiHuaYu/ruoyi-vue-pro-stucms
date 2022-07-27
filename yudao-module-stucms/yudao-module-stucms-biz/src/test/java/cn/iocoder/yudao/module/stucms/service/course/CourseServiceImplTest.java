package cn.iocoder.yudao.module.stucms.service.course;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CoursePageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.course.CourseMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.*;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.COURSE_NOT_EXISTS;

/**
 * {@link CourseServiceImpl} 的单元测试类
 *
 * @author hua
 */
@Import(CourseServiceImpl.class)
public class CourseServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseServiceImpl courseService;

    @Resource
    private CourseMapper courseMapper;

    @Test
    public void testCreateCourse_success() {
        // 准备参数
        CourseCreateReqVO reqVO = randomPojo(CourseCreateReqVO.class);

        // 调用
        Long courseId = this.courseService.createCourse(reqVO);
        // 断言
        assertNotNull(courseId);
        // 校验记录的属性是否正确
        CourseDO course = this.courseMapper.selectById(courseId);
        assertPojoEquals(reqVO, course);
    }

    @Test
    public void testUpdateCourse_success() {
        // mock 数据
        CourseDO dbCourse = randomPojo(CourseDO.class);
        this.courseMapper.insert(dbCourse);// @Sql: 先插入出一条存在的数据
        // 准备参数
        CourseUpdateReqVO reqVO = randomPojo(CourseUpdateReqVO.class, o -> {
            o.setCourseId(dbCourse.getCourseId()); // 设置更新的 ID
        });

        // 调用
        this.courseService.updateCourse(reqVO);
        // 校验是否更新正确
        CourseDO course = this.courseMapper.selectById(reqVO.getCourseId()); // 获取最新的
        assertPojoEquals(reqVO, course);
    }

    @Test
    public void testUpdateCourse_notExists() {
        // 准备参数
        CourseUpdateReqVO reqVO = randomPojo(CourseUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> this.courseService.updateCourse(reqVO), COURSE_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourse_success() {
        // mock 数据
        CourseDO dbCourse = randomPojo(CourseDO.class);
        this.courseMapper.insert(dbCourse);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourse.getCourseId();

        // 调用
        this.courseService.deleteCourse(id);
        // 校验数据不存在了
        assertNull(this.courseMapper.selectById(id));
    }

    @Test
    public void testDeleteCourse_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> this.courseService.deleteCourse(id), COURSE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCoursePage() {
        // mock 数据
        CourseDO dbCourse = randomPojo(CourseDO.class, o -> { // 等会查询到
            o.setCourseName(null);
            o.setCourseFull(null);
            o.setTeacherId(null);
        });
        this.courseMapper.insert(dbCourse);
        // 测试 courseName 不匹配
        this.courseMapper.insert(cloneIgnoreId(dbCourse, o -> o.setCourseName(null)));
        // 测试 courseFull 不匹配
        this.courseMapper.insert(cloneIgnoreId(dbCourse, o -> o.setCourseFull(null)));
        // 测试 teacherId 不匹配
        this.courseMapper.insert(cloneIgnoreId(dbCourse, o -> o.setTeacherId(null)));
        // 准备参数
        CoursePageReqVO reqVO = new CoursePageReqVO();
        reqVO.setCourseName(null);
        reqVO.setCourseFull(null);
        reqVO.setTeacherId(1L);

        // 调用
        PageResult<CourseDO> pageResult = this.courseService.getCoursePage(reqVO);
        // 断言
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(dbCourse, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseList() {
        // mock 数据
        CourseDO dbCourse = randomPojo(CourseDO.class, o -> { // 等会查询到
            o.setCourseName(null);
            o.setCourseFull(null);
            o.setTeacherId(null);
        });
        this.courseMapper.insert(dbCourse);
        // 测试 courseName 不匹配
        this.courseMapper.insert(cloneIgnoreId(dbCourse, o -> o.setCourseName(null)));
        // 测试 courseFull 不匹配
        this.courseMapper.insert(cloneIgnoreId(dbCourse, o -> o.setCourseFull(null)));
        // 测试 teacherId 不匹配
        this.courseMapper.insert(cloneIgnoreId(dbCourse, o -> o.setTeacherId(null)));
        // 准备参数
        CourseExportReqVO reqVO = new CourseExportReqVO();
        reqVO.setCourseName(null);
        reqVO.setCourseFull(null);
        reqVO.setTeacherId(null);

        // 调用
        List<CourseDO> list = this.courseService.getCourseList(reqVO);
        // 断言
        assertEquals(1, list.size());
        assertPojoEquals(dbCourse, list.get(0));
    }

}
