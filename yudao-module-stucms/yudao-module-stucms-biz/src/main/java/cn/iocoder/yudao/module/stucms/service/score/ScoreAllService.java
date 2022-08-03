package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.*;

import java.util.List;

public interface ScoreAllService {

    List<ScoreAllRespVo> getScoreAllList(ScoreSearchAllReqVO reqVO);

    List<ScoreAllChartPieRespVo> getCoursePieData(ScoreAllCoursePieReqVO reqVO);

    List<ScoreAllDetailTipRespVo> getDetailTips(ScoreAllDetailTipReqVO reqVO);
}
