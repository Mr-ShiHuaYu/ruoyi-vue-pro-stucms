package cn.iocoder.yudao.module.stucms.service.teacher;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.teacher.TeacherMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.buildTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.TEACHER_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TeacherServiceImpl} 的单元测试类
 *
 * @author hua
 */
@Import(TeacherServiceImpl.class)
public class TeacherServiceImplTest extends BaseDbUnitTest {

    @Resource
    private TeacherServiceImpl teacherService;

    @Resource
    private TeacherMapper teacherMapper;

    @Test
    public void testCreateTeacher_success() {
        // 准备参数
        TeacherCreateReqVO reqVO = randomPojo(TeacherCreateReqVO.class);

        // 调用
        Long teacherId = teacherService.createTeacher(reqVO);
        // 断言
        assertNotNull(teacherId);
        // 校验记录的属性是否正确
        TeacherDO teacher = teacherMapper.selectById(teacherId);
        assertPojoEquals(reqVO, teacher);
    }

    @Test
    public void testUpdateTeacher_success() {
        // mock 数据
        TeacherDO dbTeacher = randomPojo(TeacherDO.class);
        teacherMapper.insert(dbTeacher);// @Sql: 先插入出一条存在的数据
        // 准备参数
        TeacherUpdateReqVO reqVO = randomPojo(TeacherUpdateReqVO.class, o -> {
            o.setTeacherId(dbTeacher.getTeacherId()); // 设置更新的 ID
        });

        // 调用
        teacherService.updateTeacher(reqVO);
        // 校验是否更新正确
        TeacherDO teacher = teacherMapper.selectById(reqVO.getTeacherId()); // 获取最新的
        assertPojoEquals(reqVO, teacher);
    }

    @Test
    public void testUpdateTeacher_notExists() {
        // 准备参数
        TeacherUpdateReqVO reqVO = randomPojo(TeacherUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> teacherService.updateTeacher(reqVO), TEACHER_NOT_EXISTS);
    }

    @Test
    public void testDeleteTeacher_success() {
        // mock 数据
        TeacherDO dbTeacher = randomPojo(TeacherDO.class);
        teacherMapper.insert(dbTeacher);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbTeacher.getTeacherId();

        // 调用
        teacherService.deleteTeacher(id);
        // 校验数据不存在了
        assertNull(teacherMapper.selectById(id));
    }

    @Test
    public void testDeleteTeacher_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> teacherService.deleteTeacher(id), TEACHER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetTeacherPage() {
        // mock 数据
        TeacherDO dbTeacher = randomPojo(TeacherDO.class, o -> { // 等会查询到
            o.setName(null);
            o.setPhone(null);
            o.setSex(null);
            o.setQq(null);
            o.setCreateTime(null);
        });
        teacherMapper.insert(dbTeacher);
        // 测试 name 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setName(null)));
        // 测试 phone 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setPhone(null)));
        // 测试 sex 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setSex(null)));
        // 测试 qq 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setQq(null)));
        // 测试 createTime 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setCreateTime(null)));
        // 准备参数
        TeacherPageReqVO reqVO = new TeacherPageReqVO();
        reqVO.setName(null);
        reqVO.setPhone(null);
        reqVO.setSex(null);
        reqVO.setQq(null);
        reqVO.setCreateTime((new Date[]{buildTime(2020, 12, 1), buildTime(2020, 12, 24)}));

        // 调用
        PageResult<TeacherDO> pageResult = teacherService.getTeacherPage(reqVO);
        // 断言
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(dbTeacher, pageResult.getList().get(0));
    }

    @Test
    @Disabled
    public void testGetTeacherList() {
        // mock 数据
        TeacherDO dbTeacher = randomPojo(TeacherDO.class, o -> { // 等会查询到
            o.setName("石华");
            o.setPhone("18179871320");
            o.setSex("1");
            o.setQq("974988176");
            o.setCreateTime(DateUtils.buildTime(2022, 11, 11));
        });
        teacherMapper.insert(dbTeacher);
        // 测试 name 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setName("不匹配")));
        // 测试 phone 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setPhone("111")));
        // 测试 sex 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setSex("2")));
        // 测试 qq 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setQq("999")));
        // 测试 createTime 不匹配
        teacherMapper.insert(cloneIgnoreId(dbTeacher, o -> o.setCreateTime(DateUtils.buildTime(1994, 12, 6))));
        // 准备参数
        TeacherExportReqVO reqVO = new TeacherExportReqVO();
        reqVO.setName(null);
        reqVO.setPhone(null);
        reqVO.setSex(null);
        reqVO.setQq(null);
        reqVO.setCreateTime((new Date[]{buildTime(2020, 12, 1), buildTime(2020, 12, 24)}));
        // 调用
        List<TeacherDO> list = teacherService.getTeacherList(reqVO);
        // 断言
        assertEquals(1, list.size());
        assertPojoEquals(dbTeacher, list.get(0));
    }

}
