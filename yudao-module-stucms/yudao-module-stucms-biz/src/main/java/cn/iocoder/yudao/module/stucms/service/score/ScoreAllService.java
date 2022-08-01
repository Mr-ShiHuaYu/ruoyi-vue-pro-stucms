package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllChartPieRespVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllCoursePieReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllRespVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreSearchAllReqVO;

import java.util.List;

public interface ScoreAllService {

    List<ScoreAllRespVo> getScoreAllList(ScoreSearchAllReqVO reqVO);

    List<ScoreAllChartPieRespVo> getCoursePieData(ScoreAllCoursePieReqVO reqVO);

    // Long getYouXiuCount(ScoreAllCoursePieReqVO reqVO);
    //
    // Long getLiangHaoCount(ScoreAllCoursePieReqVO reqVO);
    //
    // Long getJiGeCount(ScoreAllCoursePieReqVO reqVO);
}
