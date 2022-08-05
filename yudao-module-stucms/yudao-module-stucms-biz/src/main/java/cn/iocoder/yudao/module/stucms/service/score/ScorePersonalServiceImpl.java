package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.*;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ScorePersonalServiceImpl implements ScorePersonalService {
    @Resource
    private ScoreMapper scoreMapper;

    @Override
    public List<Chart1SeriesRespVO> getChart1SeriesById(Long studentId) {
        List<Chart1SeriesRespVO> chart1SeriesList = this.scoreMapper.getChart1SeriesById(studentId); // 只含有name,scores,ranks
        // 将scores和ranks按,分割,组装成data
        for (Chart1SeriesRespVO chart1SeriesVo : chart1SeriesList) {
            String[] ranks = chart1SeriesVo.getRanks().split(",");
            String[] scores = chart1SeriesVo.getScores().split(",");
            List<Map<String, Object>> data = new ArrayList<>();
            for (int i = 0; i < ranks.length; i++) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("value", scores[i]); // echarts默认会取纵轴的值为value的
                hashMap.put("rank", ranks[i]); // 额外给添加进去的数据,用于鼠标放上面显示排名的
                data.add(hashMap);
            }
            chart1SeriesVo.setData(data);
            // chart1SeriesVo.setRanks(null);
            // chart1SeriesVo.setScores(null);
        }
        return chart1SeriesList;
    }

    @Override
    public List<Chart2SeriesRespVO> getChart2SeriesBySid(Long studentId) {
        List<Chart2SeriesRespVO> seriesRespVOList = this.scoreMapper.getChart2SeriesBySid(studentId);
        for (Chart2SeriesRespVO chart2SeriesVo : seriesRespVOList) {
            String[] ranks = chart2SeriesVo.getRanks().split(",");
            chart2SeriesVo.setData(Arrays.asList(ranks));
        }
        return seriesRespVOList;
    }

    @Override
    public Chart3SeriesRespVO getChart3SeriesBySid(Long studentId) {
        List<Chart3DataVo> chart3DataList = this.scoreMapper.getChart3DataBySid(studentId);
        Chart3SeriesRespVO respVO = new Chart3SeriesRespVO();
        respVO.setData(chart3DataList);
        return respVO;
    }

    @Override
    public Chart4SeriesRespVO getChart4SeriesBySid(Long studentId) {
        // 获取图表数据,每次考试的总分排名列表
        List<Chart4DataVo> rankList = this.scoreMapper.getChart4DataBySid(studentId);
        Chart4SeriesRespVO respVO = new Chart4SeriesRespVO();
        respVO.setData(rankList);
        return respVO;
    }
}
