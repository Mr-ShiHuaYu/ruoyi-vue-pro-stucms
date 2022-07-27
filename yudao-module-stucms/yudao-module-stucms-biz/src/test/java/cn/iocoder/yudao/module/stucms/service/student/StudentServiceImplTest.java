package cn.iocoder.yudao.module.stucms.service.student;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.StudentCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.StudentExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.StudentPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.StudentUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.student.StudentMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link StudentServiceImpl} 的单元测试类
*
* @author 华
*/
@Import(StudentServiceImpl.class)
public class StudentServiceImplTest extends BaseDbUnitTest {

    @Resource
    private StudentServiceImpl studentService;

    @Resource
    private StudentMapper studentMapper;

    @Test
    public void testCreateStudent_success() {
        // 准备参数
        StudentCreateReqVO reqVO = randomPojo(StudentCreateReqVO.class);

        // 调用
        Long studentId = studentService.createStudent(reqVO);
        // 断言
        assertNotNull(studentId);
        // 校验记录的属性是否正确
        StudentDO student = studentMapper.selectById(studentId);
        assertPojoEquals(reqVO, student);
    }

    @Test
    public void testUpdateStudent_success() {
        // mock 数据
        StudentDO dbStudent = randomPojo(StudentDO.class);
        studentMapper.insert(dbStudent);// @Sql: 先插入出一条存在的数据
        // 准备参数
        StudentUpdateReqVO reqVO = randomPojo(StudentUpdateReqVO.class, o -> {
            o.setId(dbStudent.getId()); // 设置更新的 ID
        });

        // 调用
        studentService.updateStudent(reqVO);
        // 校验是否更新正确
        StudentDO student = studentMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, student);
    }

    @Test
    public void testUpdateStudent_notExists() {
        // 准备参数
        StudentUpdateReqVO reqVO = randomPojo(StudentUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> studentService.updateStudent(reqVO), STUDENT_NOT_EXISTS);
    }

    @Test
    public void testDeleteStudent_success() {
        // mock 数据
        StudentDO dbStudent = randomPojo(StudentDO.class);
        studentMapper.insert(dbStudent);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbStudent.getId();

        // 调用
        studentService.deleteStudent(id);
       // 校验数据不存在了
       assertNull(studentMapper.selectById(id));
    }

    @Test
    public void testDeleteStudent_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> studentService.deleteStudent(id), STUDENT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetStudentPage() {
       // mock 数据
       StudentDO dbStudent = randomPojo(StudentDO.class, o -> { // 等会查询到
           o.setStudentUid(null);
           o.setStudentName(null);
           o.setSex(null);
           o.setPhone(null);
           o.setSysid(null);
           o.setJishu(null);
           o.setLiushou(null);
           o.setCreateTime(null);
       });
       studentMapper.insert(dbStudent);
       // 测试 studentUid 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setStudentUid(null)));
       // 测试 studentName 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setStudentName(null)));
       // 测试 sex 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setSex(null)));
       // 测试 phone 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setPhone(null)));
       // 测试 sysid 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setSysid(null)));
       // 测试 jishu 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setJishu(null)));
       // 测试 liushou 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setLiushou(null)));
       // 测试 createTime 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setCreateTime(null)));
       // 准备参数
       StudentPageReqVO reqVO = new StudentPageReqVO();
       reqVO.setStudentUid(null);
       reqVO.setStudentName(null);
       reqVO.setSex(null);
       reqVO.setPhone(null);
       reqVO.setSysid(null);
       reqVO.setJishu(null);
       reqVO.setLiushou(null);

       // 调用
       PageResult<StudentDO> pageResult = studentService.getStudentPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbStudent, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetStudentList() {
       // mock 数据
       StudentDO dbStudent = randomPojo(StudentDO.class, o -> { // 等会查询到
           o.setStudentUid(null);
           o.setStudentName(null);
           o.setSex(null);
           o.setPhone(null);
           o.setSysid(null);
           o.setJishu(null);
           o.setLiushou(null);
           o.setCreateTime(null);
       });
       studentMapper.insert(dbStudent);
       // 测试 studentUid 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setStudentUid(null)));
       // 测试 studentName 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setStudentName(null)));
       // 测试 sex 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setSex(null)));
       // 测试 phone 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setPhone(null)));
       // 测试 sysid 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setSysid(null)));
       // 测试 jishu 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setJishu(null)));
       // 测试 liushou 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setLiushou(null)));
       // 测试 createTime 不匹配
       studentMapper.insert(cloneIgnoreId(dbStudent, o -> o.setCreateTime(null)));
       // 准备参数
       StudentExportReqVO reqVO = new StudentExportReqVO();
       reqVO.setStudentUid(null);
       reqVO.setStudentName(null);
       reqVO.setSex(null);
       reqVO.setPhone(null);
       reqVO.setSysid(null);
       reqVO.setJishu(null);
       reqVO.setLiushou(null);
       reqVO.setCreateTime((new Date[]{}));

       // 调用
       List<StudentDO> list = studentService.getStudentList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbStudent, list.get(0));
    }

}
