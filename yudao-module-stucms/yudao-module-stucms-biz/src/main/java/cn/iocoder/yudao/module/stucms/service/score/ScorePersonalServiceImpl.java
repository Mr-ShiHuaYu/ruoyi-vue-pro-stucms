package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart1SeriesRespVO;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
