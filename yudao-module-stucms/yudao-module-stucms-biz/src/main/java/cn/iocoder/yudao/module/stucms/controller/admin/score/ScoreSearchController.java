package cn.iocoder.yudao.module.stucms.controller.admin.score;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseSimpleRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamSimpleVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchUpdateReqVO;
import cn.iocoder.yudao.module.stucms.convert.course.CourseConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.listener.ScoreImportListener;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
import cn.iocoder.yudao.module.stucms.service.exam.ExamService;
import cn.iocoder.yudao.module.stucms.service.score.ScoreSearchService;
import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;


@Validated
@Api(tags = {"管理后台 - 成绩查询"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/stucms/score")
public class ScoreSearchController {

    @Resource
    private ScoreSearchService scoreSearchService;

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

        PageResult<ScoreSearchRespVO> scores = this.scoreSearchService.getScoreSearchPage(pageVO);
        List<Map<String, Object>> scoreList = this.scoreSearchService.convertIncludeCourseScoreList(scores.getList());
        hashMap.put("scores", new PageResult<>(scoreList, scores.getTotal()));

        // 获取课程列表
        List<CourseDO> courseList = this.courseService.getCourseList();
        List<CourseSimpleRespVO> courseSimpleList = CourseConvert.INSTANCE.convertSimpleList(courseList);
        hashMap.put("courses", courseSimpleList);

        // 获取考试列表
        List<ExamSimpleVO> examList = this.examService.getSimpleExamList();
        hashMap.put("exams", examList);

        return success(hashMap);
    }

    @PutMapping("/update")
    @ApiOperation("更新成绩")
    @PreAuthorize("@ss.hasPermission('stucms:score:update')")
    public CommonResult<Boolean> updateScore(@Valid @RequestBody ScoreSearchUpdateReqVO updateReqVO) {
        this.scoreSearchService.updateScore(updateReqVO);
        return success(true);
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出成绩 Excel")
    @PreAuthorize("@ss.hasPermission('stucms:score:export')")
    @OperateLog(type = EXPORT)
    public void exportScoreExcel(@Valid ScoreSearchExportReqVO exportReqVO,
                                 HttpServletResponse response) throws IOException {
        List<ScoreSearchRespVO> scoreSearchList = this.scoreSearchService.getScoreSearchList(exportReqVO);
        List<Map<String, Object>> scoreList = this.scoreSearchService.convertIncludeCourseScoreList(scoreSearchList);

        // 导出 Excel
        // 不能使用定义好class的方式导出excel,因为,excel中的课程是动态的,这里只能动态的导出
        this.scoreSearchService.exportExcel(scoreList, "学生成绩", response);
    }

    @GetMapping("/get-import-template")
    @ApiOperation("获得导入成绩模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        this.scoreSearchService.getImportTemplate("成绩", response);
    }

    @PostMapping("/import")
    @ApiOperation("导入成绩")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "file", value = "Excel 文件", required = true, dataTypeClass = MultipartFile.class),
        @ApiImplicitParam(name = "updateSupport", value = "是否支持更新，默认为 false", example = "true", dataTypeClass = Boolean.class)
    })
    @PreAuthorize("@ss.hasPermission('stucms:score:import')")
    public CommonResult<String> importExcel(@RequestParam("file") MultipartFile file,
                                            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        ScoreImportListener scoreImportListener = new ScoreImportListener(updateSupport);
        EasyExcel.read(file.getInputStream(), scoreImportListener)
            .headRowNumber(2)
            .sheet().doRead();
        return success(scoreImportListener.getMessage());
    }
}
