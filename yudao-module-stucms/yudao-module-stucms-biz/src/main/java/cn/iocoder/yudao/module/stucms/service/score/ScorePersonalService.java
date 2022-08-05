package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart1SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart2SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart3SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart4SeriesRespVO;

import java.util.List;

public interface ScorePersonalService {
    List<Chart1SeriesRespVO> getChart1SeriesById(Long studentId);

    List<Chart2SeriesRespVO> getChart2SeriesBySid(Long studentId);

    Chart3SeriesRespVO getChart3SeriesBySid(Long studentId);

    Chart4SeriesRespVO getChart4SeriesBySid(Long studentId);
}
