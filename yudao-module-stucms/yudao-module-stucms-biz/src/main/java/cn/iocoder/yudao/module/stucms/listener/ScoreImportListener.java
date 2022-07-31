package cn.iocoder.yudao.module.stucms.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.score.ScoreDO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.course.CourseMapper;
import cn.iocoder.yudao.module.stucms.dal.mysql.exam.ExamMapper;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import cn.iocoder.yudao.module.stucms.dal.mysql.student.StudentMapper;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.SCORE_IMPORT_EXCEL_ERROR;

/**
 * 因为课程名称是动态的,所以,不能使用一个固定的类来导入
 */
public class ScoreImportListener extends AnalysisEventListener<Map<Integer, String>> implements ExcelListener<Map<Integer, String>> {
    private final Boolean isUpdateSupport;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;
    private final ScoreMapper scoreMapper;
    private final ExamMapper examMapper;
    // private final ScoreSearchService scoreService;
    private int successNum = 0;
    // 采用的是,如果有一个数据错误,就全部失败,用不到这个
    // private final int failureNum = 0; // 采取有异常就抛出的形式,不需要使用failureNum
    private Map<Integer, String> headMap;

    public ScoreImportListener(Boolean isUpdateSupport) {
        this.isUpdateSupport = isUpdateSupport;
        this.courseMapper = SpringUtil.getBean(CourseMapper.class);
        this.examMapper = SpringUtil.getBean(ExamMapper.class);
        this.studentMapper = SpringUtil.getBean(StudentMapper.class);
        this.scoreMapper = SpringUtil.getBean(ScoreMapper.class);
        // this.scoreService = SpringUtil.getBean(ScoreSearchService.class);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        /*{0:"学生成绩表"}
{0:"姓名",1:"学号",2:"考试",3:"语文",4:"数学",5:"英语",6:"物理",7:"化学",8:"生物",9:"地理",10:"政治",11:"历史",12:"标准差",13:"平均分",14:"总分"}*/
        Integer rowIndex = context.readRowHolder().getRowIndex();
        if (rowIndex == 1) {
            // 真正的标题行
            // 对标题行的校验移到下面每行的数据中,为了可以同时获取courseId
            this.headMap = headMap;
        }
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        int currentRow = context.readRowHolder().getRowIndex() + 1; // 当前行
        HashMap<String, String> map = new HashMap<>();// 用来存放每一行的数据,map格式
        data.forEach((idx, col) -> {
            String title = this.headMap.get(idx);
            map.put(title, col);
        });
        // 此时的map:{考试=考试, 生物=6, 历史=9, 语文=1, 英语=3, 政治=8, 姓名=俞, 物理=4, 学号=1, 数学=2, 化学=5, 地理=7}
        // 检查姓名和学号是否是正确的,根据姓名和学号去查找,如果找不到,就抛出异常
        String studentUid = map.get("学号");
        String studentName = map.get("姓名");
        StudentDO stuStudent = this.studentMapper.selectOne(new LambdaQueryWrapperX<StudentDO>()
            .eqIfPresent(StudentDO::getStudentName, studentName)
            .eqIfPresent(StudentDO::getStudentUid, studentUid));
        if (ObjectUtil.isNull(stuStudent)) {
            this.failureMsg.append("第").append(currentRow).append("行错误:学号:").append(studentUid).append("或姓名:").append(studentName).append("不存在");
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
        // 检查考试名称
        String examName = map.get("考试");
        ExamDO stuExam = this.examMapper.selectOne(new LambdaQueryWrapperX<ExamDO>()
            .eqIfPresent(ExamDO::getExamName, examName));
        if (ObjectUtil.isNull(stuExam)) {
            this.failureMsg.append("第").append(currentRow).append("行错误:").append("考试名称不存在:").append(examName);
            // 如果没有这个考试名称
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
        // 检查课程分数是否超过满分
        // 非课程列
        List<String> notCourseCols = Arrays.asList("姓名", "学号", "考试");
        this.headMap.forEach((idx, col) -> {
            if (!notCourseCols.contains(col)) {
                // 此时的col为每个课程的课程名称
                // 根据课程名称去查找课程的满分
                CourseDO stuCourse = this.courseMapper.selectOne(new LambdaQueryWrapper<CourseDO>()
                    .eq(CourseDO::getCourseName, col)
                    .last(" limit 1"));
                if (stuCourse == null) {
                    this.failureMsg.append("第").append(currentRow).append("行错误:").append("课程名称:").append(col).append("不存在");
                    throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
                }
                // 判断这个课程的分数是否大于课程满分
                int score = Integer.parseInt(map.get(col));
                if (score > stuCourse.getCourseFull() || score < 0) {
                    this.failureMsg.append("第").append(currentRow).append("行错误:").append(col).append("的分数不能大于满分或小于0,当前课程满分为:").append(stuCourse.getCourseFull());
                    throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
                }
                // 到此,校验完毕,开始插入
                // 1先根据3个id去查询stu_score表中有没有这个成绩,如果有,再判断是否isUpdateSupport,如果是,则更新,否则报错已存在
                ScoreDO stuScore = new ScoreDO();
                stuScore.setStudentId(stuStudent.getId());
                stuScore.setExamId(stuExam.getExamId());
                stuScore.setCourseId(stuCourse.getCourseId());
                stuScore.setScore(new BigDecimal(score)); // 虽然设置了,但selectOne中没有使用到score搜索

                ScoreDO stuScore1 = this.scoreMapper.selectOneByStudentIdAndExamIdAndCourseId(stuScore);
                if (ObjectUtil.isNull(stuScore1)) {
                    // 不存在,添加,添加时要没有主键的
                    this.scoreMapper.insert(stuScore);
                } else if (this.isUpdateSupport) {
                    // 如果选择了覆盖,就更新,更新时要传入有主键的
                    stuScore.setScoreId(stuScore1.getScoreId());
                    this.scoreMapper.updateById(stuScore);
                } else {
                    // 已存在,异常
                    this.failureMsg.append("第").append(currentRow).append("行错误:").append(col).append("成绩已存在,请勿重复添加,若要覆盖,请勾选更新数据");
                    throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
                }
            }
        });
        this.successNum++;
        this.successMsg.append("<br/>").append(this.successNum).append(stuStudent.getStudentName()).append("导入成功");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public ExcelResult<Map<Integer, String>> getExcelResult() {
        return new ExcelResult<Map<Integer, String>>() {
            @Override
            public List<Map<Integer, String>> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }

            @Override
            public String getAnalysis() {
                ScoreImportListener.this.successMsg.insert(0, "恭喜您，已全部导入成功！共 " + ScoreImportListener.this.successNum + "条，数据如下：");
                return ScoreImportListener.this.successMsg.toString();
            }
        };
    }
}
