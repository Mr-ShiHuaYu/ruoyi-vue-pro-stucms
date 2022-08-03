package cn.iocoder.yudao.module.stucms.service.score;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.module.stucms.config.ScoreConfig;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.*;
import cn.iocoder.yudao.module.stucms.dal.mysql.course.CourseMapper;
import cn.iocoder.yudao.module.stucms.dal.mysql.score.ScoreMapper;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
import cn.iocoder.yudao.module.stucms.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

import static cn.iocoder.yudao.module.stucms.enums.ErrorCodeConstants.SCORE_FIELD_NOT_EXISTS;

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

    @Override
    public List<ScoreAllDetailTipRespVo> getDetailTips(ScoreAllDetailTipReqVO reqVO) {
        String field = reqVO.getField();
        List<ScoreAllDetailTipRespVo> detailTipList;

        Integer courseFull = this.courseService.getCourseFullById(reqVO.getCid());// 满分,如100
        Float youxiu = this.scoreConfig.getYouxiu(); // 优秀比率,如 0.9
        Float youxiuScore = youxiu * courseFull;// 满分*比率 =优秀具体的分数

        Float lianghao = this.scoreConfig.getLianghao();
        Float lianghaoScore = lianghao * courseFull;

        Float jige = this.scoreConfig.getJige();
        Float jigeScore = jige * courseFull;

        switch (field) {
            case "youxiu":
                detailTipList = this.scoreMapper.selectYouXiuStudentList(youxiuScore, reqVO);
                break;
            case "lianghao":
                detailTipList = this.scoreMapper.selectLiangHaoStudentList(youxiuScore, lianghaoScore, reqVO);
                break;
            case "jige":
                detailTipList = this.scoreMapper.selectJiGeStudentList(lianghaoScore, jigeScore, reqVO);
                break;
            case "bujige":
                detailTipList = this.scoreMapper.selectBuJiGeStudentList(jigeScore, reqVO);
                break;
            case "max":
                detailTipList = this.scoreMapper.selectMaxScoreStudentList(reqVO);
                break;
            case "min":
                detailTipList = this.scoreMapper.selectMinScoreStudentList(reqVO);
                break;
            default:
                throw ServiceExceptionUtil.exception(SCORE_FIELD_NOT_EXISTS, field);
        }
        detailTipList.forEach(scoreVo -> {
            // 去除分数中末尾的0,如:89.0转为89
            String score = NumberUtils.getFloatValue(scoreVo.getScore());
            scoreVo.setScore(score);
        });
        return detailTipList;
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
