package cn.iocoder.yudao.module.stucms.service.score;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.convert.score.ScoreSearchConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.score.ScoreDO;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
import cn.iocoder.yudao.module.stucms.utils.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class ScoreSearchServiceImpl implements ScoreSearchService {
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private CourseService courseService;

    @Override
    public PageResult<ScoreSearchRespVO> getScoreSearchPage(ScoreSearchPageReqVO pageVO) {
        return scoreMapper.selectScoreSearchPage(pageVO);
    }

    /**
     * 在里面添加每一科的具体成绩,转为Map
     * 目标:要构建一个类似这样的map: 学号:xx,姓名:xx,数学:99,语文:89,
     *
     * @param scores 数据库查询出来的没有每一科成绩
     * @return
     */
    @Override
    public List<Map<String, Object>> convertIncludeCourseScoreListByPage(List<ScoreSearchRespVO> scores) {
        List<Map<String, Object>> scoreList = new ArrayList<>(); // 保存添加了课程后的成绩列表
        List<CourseDO> courseList = courseService.getCourseList(); // 所有课程
        // 1.遍历每一个成绩
        scores.forEach(score -> {
            Map<String, Object> scoreMap = BeanUtil.beanToMap(score);
            ScoreDO scoreDO = ScoreSearchConvert.INSTANCE.convertToDO(score);
            // 2.在每个成绩中遍历所有课程,添加这个课程的成绩
            courseList.forEach(course -> {
                // scoreDO中是没有courseId的,手动添加上
                scoreDO.setCourseId(course.getCourseId());
                ScoreDO scoreDOWithScore = scoreMapper.selectOneByStudentIdAndExamIdAndCourseId(scoreDO); // 包含某个人某个学科的成绩对象,可能为Null
                BigDecimal score1;
                // 如果成绩表中没有找到这个人的这个科目的成绩,就手动设置为0分
                if (scoreDOWithScore == null) {
                    score1 = BigDecimal.valueOf(0);
                }else {
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
}
