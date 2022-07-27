package cn.iocoder.yudao.module.stucms.service.exam;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.exam.ExamMapper;
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
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.EXAM_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
* {@link ExamServiceImpl} 的单元测试类
*
* @author hua
*/
@Import(ExamServiceImpl.class)
public class ExamServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ExamServiceImpl examService;

    @Resource
    private ExamMapper examMapper;

    @Test
    public void testCreateExam_success() {
        // 准备参数
        ExamCreateReqVO reqVO = randomPojo(ExamCreateReqVO.class);

        // 调用
        Long examId = examService.createExam(reqVO);
        // 断言
        assertNotNull(examId);
        // 校验记录的属性是否正确
        ExamDO exam = examMapper.selectById(examId);
        assertPojoEquals(reqVO, exam);
    }

    @Test
    public void testUpdateExam_success() {
        // mock 数据
        ExamDO dbExam = randomPojo(ExamDO.class);
        examMapper.insert(dbExam);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ExamUpdateReqVO reqVO = randomPojo(ExamUpdateReqVO.class, o -> {
            o.setExamId(dbExam.getExamId()); // 设置更新的 ID
        });

        // 调用
        examService.updateExam(reqVO);
        // 校验是否更新正确
        ExamDO exam = examMapper.selectById(reqVO.getExamId()); // 获取最新的
        assertPojoEquals(reqVO, exam);
    }

    @Test
    public void testUpdateExam_notExists() {
        // 准备参数
        ExamUpdateReqVO reqVO = randomPojo(ExamUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> examService.updateExam(reqVO), EXAM_NOT_EXISTS);
    }

    @Test
    public void testDeleteExam_success() {
        // mock 数据
        ExamDO dbExam = randomPojo(ExamDO.class);
        examMapper.insert(dbExam);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbExam.getExamId();

        // 调用
        examService.deleteExam(id);
       // 校验数据不存在了
       assertNull(examMapper.selectById(id));
    }

    @Test
    public void testDeleteExam_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> examService.deleteExam(id), EXAM_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetExamPage() {
       // mock 数据
       ExamDO dbExam = randomPojo(ExamDO.class, o -> { // 等会查询到
           o.setExamName(null);
           o.setExamTime(null);
       });
       examMapper.insert(dbExam);
       // 测试 examName 不匹配
       examMapper.insert(cloneIgnoreId(dbExam, o -> o.setExamName(null)));
       // 测试 examTime 不匹配
       examMapper.insert(cloneIgnoreId(dbExam, o -> o.setExamTime(null)));
       // 准备参数
       ExamPageReqVO reqVO = new ExamPageReqVO();
       reqVO.setExamName(null);
       reqVO.setExamTime((new Date[]{}));

       // 调用
       PageResult<ExamDO> pageResult = examService.getExamPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbExam, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetExamList() {
       // mock 数据
       ExamDO dbExam = randomPojo(ExamDO.class, o -> { // 等会查询到
           o.setExamName(null);
           o.setExamTime(null);
       });
       examMapper.insert(dbExam);
       // 测试 examName 不匹配
       examMapper.insert(cloneIgnoreId(dbExam, o -> o.setExamName(null)));
       // 测试 examTime 不匹配
       examMapper.insert(cloneIgnoreId(dbExam, o -> o.setExamTime(null)));
       // 准备参数
       ExamExportReqVO reqVO = new ExamExportReqVO();
       reqVO.setExamName(null);
       reqVO.setExamTime((new Date[]{}));

       // 调用
       List<ExamDO> list = examService.getExamList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbExam, list.get(0));
    }

}
