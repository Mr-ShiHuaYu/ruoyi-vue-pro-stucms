package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.module.stucms.config.ScoreConfig;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllChartPieRespVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllCoursePieReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllRespVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreSearchAllReqVO;
import cn.iocoder.yudao.module.stucms.dal.mysql.course.CourseMapper;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Service
@Validated
public class ScoreAllServiceImpl implements ScoreAllService {
    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ScoreConfig scoreConfig;

    @Autowired
    private CourseService courseService;

    @Override
    public List<ScoreAllRespVo> getScoreAllList(ScoreSearchAllReqVO reqVO) {
        return this.scoreMapper.selectScoreAllList(reqVO);
    }

    @Override
    public List<ScoreAllChartPieRespVo> getCoursePieData(ScoreAllCoursePieReqVO reqVO) {
        Long youxiuCount = this.getYouXiuCount(reqVO); // 优秀数量
        ScoreAllChartPieRespVo youXiuVO = ScoreAllChartPieRespVo.builder().name("优秀").value(youxiuCount).build();
        Long liangHaoCount = this.getLiangHaoCount(reqVO); // 良好数量
        ScoreAllChartPieRespVo liangHaoVO = ScoreAllChartPieRespVo.builder().name("良好").value(liangHaoCount).build();
        Long jiGeCount = this.getJiGeCount(reqVO); // 及格数量
        ScoreAllChartPieRespVo jigeVO = ScoreAllChartPieRespVo.builder().name("及格").value(jiGeCount).build();
        Long buJiGeCount = this.getBuJiGeCount(reqVO); // 不及格数量
        ScoreAllChartPieRespVo buJigeVO = ScoreAllChartPieRespVo.builder().name("不及格").value(buJiGeCount).build();
        return Arrays.asList(youXiuVO, liangHaoVO, jigeVO, buJigeVO);
    }

    public Long getYouXiuCount(ScoreAllCoursePieReqVO reqVO) {
        Long cid = reqVO.getCid();
        Integer courseFull = this.courseService.getCourseFullById(cid);// 满分,如100
        Float youxiuRate = this.scoreConfig.getYouxiu(); // 优秀比率,如0.9
        return this.scoreMapper.selectYouXiuCount(courseFull * youxiuRate, reqVO);
    }

    public Long getLiangHaoCount(ScoreAllCoursePieReqVO reqVO) {
        Integer courseFull = this.courseService.getCourseFullById(reqVO.getCid());// 满分,如100
        Float youxiuRate = this.scoreConfig.getYouxiu(); // 优秀比率,如0.9
        Float lianghaoRate = this.scoreConfig.getLianghao(); // 优秀比率,如0.9
        return this.scoreMapper.selectLiangHaoCount(courseFull * youxiuRate, courseFull * lianghaoRate, reqVO);
    }

    public Long getJiGeCount(ScoreAllCoursePieReqVO reqVO) {
        Integer courseFull = this.courseService.getCourseFullById(reqVO.getCid());// 满分,如100
        Float lianghaoRate = this.scoreConfig.getLianghao(); // 优秀比率,如0.9
        Float jige = this.scoreConfig.getJige();
        return this.scoreMapper.selectJiGeCount(courseFull * lianghaoRate, courseFull * jige, reqVO);

    }

    private Long getBuJiGeCount(ScoreAllCoursePieReqVO reqVO) {
        Integer courseFull = this.courseService.getCourseFullById(reqVO.getCid());// 满分,如100
        Float jige = this.scoreConfig.getJige();
        return this.scoreMapper.selectBuJiGeCount(courseFull * jige, reqVO);
    }


}
