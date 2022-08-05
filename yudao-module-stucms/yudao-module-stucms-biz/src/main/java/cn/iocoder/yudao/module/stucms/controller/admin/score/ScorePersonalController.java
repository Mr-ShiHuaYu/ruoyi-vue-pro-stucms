package cn.iocoder.yudao.module.stucms.controller.admin.score;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart1SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart2SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart3SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart4SeriesRespVO;
import cn.iocoder.yudao.module.stucms.service.score.ScorePersonalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Validated
@Api(tags = {"管理后台 - 个人分析"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/stucms/score/personal")
public class ScorePersonalController {
    @Resource
    private ScorePersonalService scorePersonalService;

    @ApiOperation("个人成绩-图1-各科分数折线图")
    @GetMapping("/chart1/{studentId}")
    @PreAuthorize("@ss.hasPermission('stucms:score:personal:query')")
    public CommonResult<List<Chart1SeriesRespVO>> getChart1(@ApiParam("学生ID")
                                                            @NotNull(message = "学生ID不能为空")
                                                            @PathVariable("studentId") Long studentId) {
        List<Chart1SeriesRespVO> seriesRespVOList = this.scorePersonalService.getChart1SeriesById(studentId);
        return success(seriesRespVOList);
    }

    @ApiOperation("个人成绩-图2-各科排名折线图")
    @PreAuthorize("@ss.hasPermission('stucms:score:personal:query')")
    @GetMapping("/chart2/{studentId}")
    public CommonResult<List<Chart2SeriesRespVO>> getChart2(
        @ApiParam("学生ID")
        @NotNull(message = "学生ID不能为空")
        @PathVariable("studentId") Long studentId){
        List<Chart2SeriesRespVO> seriesRespVOList = this.scorePersonalService.getChart2SeriesBySid(studentId);
        return success(seriesRespVOList);
    }

    @ApiOperation("个人成绩-图3-各科排名折线图")
    @PreAuthorize("@ss.hasPermission('stucms:score:personal:query')")
    @GetMapping("/chart3/{studentId}")
    public CommonResult<Chart3SeriesRespVO> getChart3(
        @ApiParam("学生ID")
        @NotNull(message = "学生ID不能为空")
        @PathVariable("studentId") Long studentId){
        Chart3SeriesRespVO seriesRespVO = this.scorePersonalService.getChart3SeriesBySid(studentId);

        return success(seriesRespVO);
    }

    @ApiOperation("个人成绩-图4-总分排名折线图")
    @PreAuthorize("@ss.hasPermission('stucms:score:personal:query')")
    @GetMapping("/chart4/{studentId}")
    public CommonResult<Chart4SeriesRespVO> getChart4(
        @ApiParam("学生ID")
        @NotNull(message = "学生ID不能为空")
        @PathVariable("studentId") Long studentId){
        Chart4SeriesRespVO seriesRespVO = this.scorePersonalService.getChart4SeriesBySid(studentId);

        return success(seriesRespVO);
    }
}
