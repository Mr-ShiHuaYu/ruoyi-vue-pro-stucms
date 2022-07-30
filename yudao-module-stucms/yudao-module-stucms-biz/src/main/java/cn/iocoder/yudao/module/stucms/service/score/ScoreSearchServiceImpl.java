package cn.iocoder.yudao.module.stucms.service.score;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchUpdateReqVO;
import cn.iocoder.yudao.module.stucms.convert.score.ScoreSearchConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.score.ScoreDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
import cn.iocoder.yudao.module.stucms.utils.ExcelUtil;
import cn.iocoder.yudao.module.stucms.utils.NumberUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.SCORE_GT_FULL;

@Service
@Validated
public class ScoreSearchServiceImpl implements ScoreSearchService {
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private CourseService courseService;

    @Override
    public PageResult<ScoreSearchRespVO> getScoreSearchPage(ScoreSearchPageReqVO pageVO) {
        return this.scoreMapper.selectScoreSearchPage(pageVO);
    }

    /**
     * 在里面添加每一科的具体成绩,转为Map
     * 目标:要构建一个类似这样的map: 学号:xx,姓名:xx,数学:99,语文:89,
     *
     * @param scores 数据库查询出来的没有每一科成绩
     * @return
     */
    @Override
    public List<Map<String, Object>> convertIncludeCourseScoreList(List<ScoreSearchRespVO> scores) {
        List<Map<String, Object>> scoreList = new ArrayList<>(); // 保存添加了课程后的成绩列表
        List<CourseDO> courseList = this.courseService.getCourseList(); // 所有课程
        // 1.遍历每一个成绩
        scores.forEach(score -> {
            Map<String, Object> scoreMap = BeanUtil.beanToMap(score);
            ScoreDO scoreDO = ScoreSearchConvert.INSTANCE.convertToDO(score);
            // 2.在每个成绩中遍历所有课程,添加这个课程的成绩
            courseList.forEach(course -> {
                // scoreDO中是没有courseId的,手动添加上
                scoreDO.setCourseId(course.getCourseId());
                ScoreDO scoreDOWithScore = this.scoreMapper.selectOneByStudentIdAndExamIdAndCourseId(scoreDO); // 包含某个人某个学科的成绩对象,可能为Null
                BigDecimal score1;
                // 如果成绩表中没有找到这个人的这个科目的成绩,就手动设置为0分
                if (scoreDOWithScore == null) {
                    score1 = BigDecimal.valueOf(0);
                } else {
                    score1 = scoreDOWithScore.getScore();
                }
                // 为了去除成绩后面多余的0,如:转换前:99.00,转换后:99
                String scoreFloat = NumberUtils.getFloatValue(score1.toString());
                scoreMap.put(course.getCourseName(), scoreFloat);
            });
            // 添加到list
            scoreList.add(scoreMap);
        });
        return scoreList;
    }

    @Override
    public void updateScore(ScoreSearchUpdateReqVO updateReqVO) {
        // 1.校验成绩是否在课程满分范围内
        this.checkScoreRange(updateReqVO);
        // TODO 后期检查是否是管理员,如果不是,学生只能修改自己的成绩
        ScoreDO scoreDO = ScoreSearchConvert.INSTANCE.convertToDO(updateReqVO);
        ScoreDO score = this.scoreMapper.selectOneByStudentIdAndExamIdAndCourseId(scoreDO);
        if (score != null) {
            // 不是Null,更新分数
            scoreDO.setScoreId(score.getScoreId());
            this.scoreMapper.updateById(scoreDO);
        } else {
            // null,添加
            this.scoreMapper.insert(scoreDO);
        }
    }

    @Override
    public List<ScoreSearchRespVO> getScoreSearchList(ScoreSearchExportReqVO exportReqVO) {
        return this.scoreMapper.selectScoreSearchList(exportReqVO);
    }

    /**
     * 导出学生成绩表为excel
     *
     * @param scoreList
     * @param fileName  导出的excel名字
     * @param response
     */
    @Override
    public void exportExcel(List<Map<String, Object>> scoreList, String fileName, HttpServletResponse response) {
        try {
            // 第一行的大标题
            String bigTitle = "学生成绩表";
            // 导出excel的详细数据
            List<List<Object>> detail = this.getExportScoreDetail(scoreList);
            // response.reset(); // 这个不能写,写了就报错
            ServletOutputStream os = response.getOutputStream();

            // 设置标题行和内容样式策略
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(ExcelUtil.getHeadStyle(), ExcelUtil.getCellStyle());

            // 第二行excel的列名,标题行
            List<List<String>> header = this.getExcelHeader();
            EasyExcel.write(os)
                // 第一行大标题样式设置
                .registerWriteHandler(new ExcelUtil.ExcelBigTitleStyle(bigTitle, header.size()))
                //设置默认样式及写入头信息开始的行数
                .useDefaultStyle(true).relativeHeadRowIndex(1)
                // 表头、内容样式设置
                .registerWriteHandler(horizontalCellStyleStrategy)
                // 统一设置列宽为25
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(15))
                // 设置自动列宽则
                // .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .head(header)
                // 设置不要自动关闭流,交给 Servlet 自己处理
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelUtil.ExcelBigNumberConvert())
                // 设置sheet名
                .sheet("数据")
                // 写入数据
                .doWrite(detail);

            ExcelUtil.setAttachmentResponseHeader(response, fileName + ".xlsx");
        } catch (IOException e) {
            throw new RuntimeException("学生成绩表导出异常");
        }
    }

    /**
     * 检查分数的范围是否大于课程总分
     *
     * @param updateReqVO
     */
    private void checkScoreRange(ScoreSearchUpdateReqVO updateReqVO) {
        CourseDO course = this.courseService.getCourse(updateReqVO.getCourseId());
        double score = updateReqVO.getScore().doubleValue(); // 前端传递过来的新的成绩
        if (score > course.getCourseFull()) {
            // 分数大于满分,抛出异常,小于0已经校验
            throw exception(SCORE_GT_FULL, course.getCourseName(), course.getCourseFull());
        }
    }

    /**
     * 获取成绩数据中的表格详情数据,用于导出
     *
     * @param mapList
     * @return
     */
    private List<List<Object>> getExportScoreDetail(List<Map<String, Object>> mapList) {
        // 保存所有表格数据
        List<CourseDO> courseList = this.courseService.getCourseList();
        List<List<Object>> list = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            List<Object> objectList = new ArrayList<>();
            objectList.add(map.get("studentName"));
            objectList.add(map.get("studentUid"));
            objectList.add(map.get("examName"));
            for (CourseDO courseDO : courseList) {
                objectList.add(map.get(courseDO.getCourseName()));
            }
            objectList.add(map.get("std"));
            objectList.add(map.get("avg"));
            objectList.add(map.get("sum"));
            list.add(objectList);
        }
        return list;
    }

    /**
     * 获取导出学生成绩表excel的标题行
     */
    private List<List<String>> getExcelHeader() {
        List<List<String>> list = new ArrayList<>();
        List<CourseDO> courseList = this.courseService.getCourseList();
        List<String> header = new ArrayList<>(Arrays.asList("姓名", "学号", "考试"));
        for (CourseDO courseDO : courseList) {
            header.add(courseDO.getCourseName());
        }
        header.addAll(Arrays.asList("标准差", "平均分", "总分"));

        // 组装成excel的表头
        for (String h : header) {
            List<String> head = new ArrayList<>();
            head.add(h);
            list.add(head);
        }
        return list;
    }

    /**
     * 获取学生成绩导入模板中的标题行
     *
     * @return
     */
    private List<List<String>> getExcelTemplateHeader() {
        List<List<String>> list = new ArrayList<>();
        List<CourseDO> courseList = this.courseService.getCourseList();

        List<String> header = new ArrayList<>(Arrays.asList("姓名", "学号", "考试"));
        for (CourseDO courseDO : courseList) {
            header.add(courseDO.getCourseName());
        }
        // 组装成excel的表头
        for (String h : header) {
            List<String> head = new ArrayList<>();
            head.add(h);
            list.add(head);
        }
        return list;
    }


}
