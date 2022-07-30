package cn.iocoder.yudao.module.stucms.service.score;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;

import java.util.List;
import java.util.Map;

/**
 * 成绩Service接口
 *
 * @author ysh
 */
public interface ScoreSearchService {

    PageResult<ScoreSearchRespVO> getScoreSearchPage(ScoreSearchPageReqVO pageVO);

    List<Map<String, Object>> convertIncludeCourseScoreListByPage(List<ScoreSearchRespVO> scores);
}
