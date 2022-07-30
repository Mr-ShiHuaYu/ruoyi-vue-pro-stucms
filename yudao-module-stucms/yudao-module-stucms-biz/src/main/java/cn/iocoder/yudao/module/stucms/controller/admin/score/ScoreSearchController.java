package cn.iocoder.yudao.module.stucms.controller.admin.score;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseSimpleRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamSimpleVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.convert.course.CourseConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
import cn.iocoder.yudao.module.stucms.service.exam.ExamService;
import cn.iocoder.yudao.module.stucms.service.score.ScoreSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Validated
@Api(tags = {"管理后台 - 成绩查询"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/stucms/score")
public class ScoreSearchController {

    @Resource
    private ScoreSearchService searchService;

    @Resource
    private CourseService courseService;

    @Resource
    private ExamService examService;

    @GetMapping("/page")
    @ApiOperation("获得成绩查询分页")
    @PreAuthorize("@ss.hasPermission('stucms:score:search:query')")
    public CommonResult<HashMap<String, Object>> getSearchPage(@Valid ScoreSearchPageReqVO pageVO) {
        // 创建一个hashMap来保存最后的结果,因为里面要保存多个数组
        HashMap<String, Object> hashMap = new HashMap<>();

        PageResult<ScoreSearchRespVO> scores = searchService.getScoreSearchPage(pageVO);
        List<Map<String, Object>> scoreList = searchService.convertIncludeCourseScoreListByPage(scores.getList());
        hashMap.put("scores", new PageResult<>(scoreList, scores.getTotal()));

        // 获取课程列表
        List<CourseDO> courseList = courseService.getCourseList();
        List<CourseSimpleRespVO> courseSimpleList = CourseConvert.INSTANCE.convertSimpleList(courseList);
        hashMap.put("courses", courseSimpleList);

        // 获取考试列表
        List<ExamSimpleVO> examList = examService.getSimpleExamList();
        hashMap.put("exams", examList);

        return success(hashMap);
    }

}
