package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart1SeriesRespVO;

import java.util.List;

public interface ScorePersonalService {
    List<Chart1SeriesRespVO> getChart1SeriesById(Long studentId);
}
