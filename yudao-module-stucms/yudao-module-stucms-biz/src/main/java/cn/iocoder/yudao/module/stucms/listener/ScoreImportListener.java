package cn.iocoder.yudao.module.stucms.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
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

import java.math.BigDecimal;
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
    private int currentRow = 0;
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
        this.currentRow = context.readRowHolder().getRowIndex() + 1; // 当前行
        HashMap<String, String> rowMap = new HashMap<>();// 用来存放每一行的数据,map格式
        data.forEach((idx, col) -> {
            String title = this.headMap.get(idx);
            rowMap.put(title, col);
        });
        // 此时的map:{考试=考试, 生物=6, 历史=9, 语文=1, 英语=3, 政治=8, 姓名=俞, 物理=4, 学号=1, 数学=2, 化学=5, 地理=7}
        // 检查姓名和学号是否是正确的,根据姓名和学号去查找,如果找不到,就抛出异常
        this.checkStudentUidExists(rowMap);
        this.checkStudentNameExists(rowMap);
        // 检查考试名称
        this.checkExamNameExists(rowMap);

        List<CourseDO> courseList = this.courseMapper.selectList(); // 所有课程名称
        List<String> courseCols = CollectionUtils.convertList(courseList, CourseDO::getCourseName);// 课程名称的list
        this.headMap.forEach((idx, col) -> {
            if (courseCols.contains(col)) {
                this.checkCourseNameExists(col); // 检查课程名称是否存在
                this.checkScoreRange(rowMap, col); // 检查分数是否合格
            }
        });

        StudentDO stuStudent = this.studentMapper.selectOneByUid(rowMap.get("学号")); // 因为经过上面的检查,这里获取到的一定是有学生的
        // 到此,校验完毕,开始插入
        this.headMap.forEach((idx, col) -> {
            if (courseCols.contains(col)) {
                CourseDO stuCourse = this.courseMapper.selectOneByCourseName(col);
                // 1先根据3个id去查询stu_score表中有没有这个成绩,如果有,再判断是否isUpdateSupport,如果是,则更新,否则报错已存在
                ExamDO stuExam = this.examMapper.selectOneByExamName(rowMap.get("考试"));
                int score = Integer.parseInt(rowMap.get(col));

                ScoreDO stuScore = ScoreDO.builder()
                    .studentId(stuStudent.getId())
                    .examId(stuExam.getExamId())
                    .courseId(stuCourse.getCourseId())
                    .score(new BigDecimal(score)) // 虽然设置了,但selectOne中没有使用到score搜索
                    .build();
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
                    this.failureMsg.append("第").append(this.currentRow).append("行错误:").append(col).append("成绩已存在,请勿重复添加,若要覆盖,请勾选更新数据");
                    throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
                }
            }
        });
        this.successNum++;
        this.successMsg.append("<br/>").append(this.successNum).append(stuStudent.getStudentName()).append("导入成功");
    }

    private void checkScoreRange(HashMap<String, String> rowMap, String col) {
        int score = Integer.parseInt(rowMap.get(col));
        CourseDO stuCourse = this.courseMapper.selectOneByCourseName(col);
        if (score > stuCourse.getCourseFull() || score < 0) {
            this.failureMsg.append("第").append(this.currentRow).append("行错误:").append(col).append("的分数不能大于满分或小于0,当前课程满分为:").append(stuCourse.getCourseFull());
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
    }

    private void checkCourseNameExists(String col) {
        CourseDO stuCourse = this.courseMapper.selectOneByCourseName(col);
        if (stuCourse == null) {
            this.failureMsg.append("第").append(this.currentRow).append("行错误:").append("课程名称:").append(col).append("不存在");
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
    }


    private void checkStudentUidExists(HashMap<String, String> rowMap) {
        String studentUid = rowMap.get("学号");
        StudentDO stuStudent = this.studentMapper.selectOneByUid(studentUid);
        if (ObjectUtil.isNull(stuStudent)) {
            this.failureMsg.append("第").append(this.currentRow).append("行错误:学号:").append(studentUid).append("不存在");
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
    }

    private void checkStudentNameExists(HashMap<String, String> rowMap) {
        String studentName = rowMap.get("姓名");
        StudentDO stuStudent = this.studentMapper.selectOne(StudentDO::getStudentName, studentName);
        if (ObjectUtil.isNull(stuStudent)) {
            this.failureMsg.append("第").append(this.currentRow).append("行错误:").append("姓名:").append(studentName).append("不存在");
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
    }

    private void checkExamNameExists(HashMap<String, String> rowMap) {
        String examName = rowMap.get("考试");
        ExamDO stuExam = this.examMapper.selectOne(ExamDO::getExamName, examName);
        if (ObjectUtil.isNull(stuExam)) {
            this.failureMsg.append("第").append(this.currentRow).append("行错误:").append("考试名称不存在:").append(examName);
            // 如果没有这个考试名称
            throw exception(SCORE_IMPORT_EXCEL_ERROR, this.failureMsg.toString());
        }
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
