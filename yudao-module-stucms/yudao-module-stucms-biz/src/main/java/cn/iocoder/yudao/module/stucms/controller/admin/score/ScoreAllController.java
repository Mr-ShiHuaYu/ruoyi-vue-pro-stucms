package cn.iocoder.yudao.module.stucms.controller.admin.score;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.*;
import cn.iocoder.yudao.module.stucms.service.score.ScoreAllService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Validated
@Api(tags = {"管理后台 - 总体分析"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/stucms/score/all")
public class ScoreAllController {

    @Autowired
    private ScoreAllService scoreAllService;

    @GetMapping("/list")
    @ApiOperation("获得总体成绩")
    @PreAuthorize("@ss.hasPermission('stucms:score:all:query')")
    public CommonResult<List<ScoreAllRespVo>> getSearchAllList(@Valid ScoreSearchAllReqVO reqVO) {
        List<ScoreAllRespVo> list = this.scoreAllService.getScoreAllList(reqVO);
        return success(list);
    }

    @GetMapping("/showCoursePie")
    @ApiOperation("获得某个课程的优秀率占比图")
    @PreAuthorize("@ss.hasPermission('stucms:score:all:query')")
    public CommonResult<List<ScoreAllChartPieRespVo>> showCoursePie(@Valid ScoreAllCoursePieReqVO reqVO) {
        List<ScoreAllChartPieRespVo> list = this.scoreAllService.getCoursePieData(reqVO);
        return success(list);
    }

    @GetMapping("/showDetailTips")
    @ApiOperation("显示总体分析的某个单位格的具体名单")
    @PreAuthorize("@ss.hasPermission('stucms:score:all:query')")
    public CommonResult<List<ScoreAllDetailTipRespVo>> showDetailTips(@Valid ScoreAllDetailTipReqVO reqVO) {
        List<ScoreAllDetailTipRespVo> list = this.scoreAllService.getDetailTips(reqVO);
        return success(list);
    }
}
