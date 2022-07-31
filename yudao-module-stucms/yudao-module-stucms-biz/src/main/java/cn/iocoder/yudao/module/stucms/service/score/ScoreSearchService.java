package cn.iocoder.yudao.module.stucms.service.score;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchUpdateReqVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成绩Service接口
 *
 * @author ysh
 */
public interface ScoreSearchService {

    PageResult<ScoreSearchRespVO> getScoreSearchPage(ScoreSearchPageReqVO pageVO);

    List<Map<String, Object>> convertIncludeCourseScoreList(List<ScoreSearchRespVO> scores);

    void updateScore(ScoreSearchUpdateReqVO updateReqVO);

    List<ScoreSearchRespVO> getScoreSearchList(ScoreSearchExportReqVO exportReqVO);

    void exportExcel(List<Map<String, Object>> scoreList, String fileName, HttpServletResponse response);

    void getImportTemplate(String sheetName, HttpServletResponse response);
}
