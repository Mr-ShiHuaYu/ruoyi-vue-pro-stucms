package cn.iocoder.yudao.module.stucms.dal.mysql.score;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.*;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart1SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart2SeriesRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart3DataVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal.Chart4DataVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.score.ScoreDO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试 Mapper
 *
 * @author hua
 */
@Mapper
public interface ScoreMapper extends BaseMapperX<ScoreDO> {
    /**
     * 通过xml查询出成绩分页,返回值和第一个参数必须是分页对象/List  IPage<T> 或者继承IPage<T>的类型
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<ScoreSearchRespVO> selectScoreSearchPageByXml(@Param("page") Page<ScoreDO> page, @Param("ew") Wrapper<ScoreDO> wrapper);

    // 通过xml查询出全部列表
    List<ScoreSearchRespVO> selectScoreSearchListByXml(@Param("ew") Wrapper<ScoreDO> wrapper);

    List<ScoreAllRespVo> selectScoreAllListByXml(@Param("ew") Wrapper<ScoreDO> wrapperX);


    List<ScoreAllDetailTipRespVo> selectStudentListByXml(@Param("ew") Wrapper<ScoreDO> wrapperX);


    default QueryWrapperX<ScoreDO> getScoreDOQueryWrapperX(String studentName, String studentUid, Long examId) {
        return new QueryWrapperX<ScoreDO>()
            .likeIfPresent("st.student_name", studentName)
            .likeIfPresent("st.student_uid", studentUid)
            .eqIfPresent("sc.exam_id", examId);
    }

    /**
     * 查询成绩分页对象列表
     *
     * @param reqVO
     * @return
     */
    default PageResult<ScoreSearchRespVO> selectScoreSearchPage(ScoreSearchPageReqVO reqVO) {
        // MyBatis Plus 查询
        Page<ScoreDO> mpPage = MyBatisUtils.buildPage(reqVO);
        Page<ScoreSearchRespVO> respVOPage = this.selectScoreSearchPageByXml(mpPage, this.getScoreDOQueryWrapperX(reqVO.getStudentName(), reqVO.getStudentUid(), reqVO.getExamId()));
        return new PageResult<>(respVOPage.getRecords(), respVOPage.getTotal());
    }

    /**
     * 通过学生ID,考试ID,课程ID 三方联合查询出scoreDo
     *
     * @param score
     * @return
     */
    default ScoreDO selectOneByStudentIdAndExamIdAndCourseId(ScoreDO score) {
        return this.selectOne(new LambdaQueryWrapperX<ScoreDO>()
            .eqIfPresent(ScoreDO::getStudentId, score.getStudentId())
            .eqIfPresent(ScoreDO::getExamId, score.getExamId())
            .eqIfPresent(ScoreDO::getCourseId, score.getCourseId())
        );
    }


    /**
     * 查询出全部成绩列表
     *
     * @param exportReqVO
     * @return
     */
    default List<ScoreSearchRespVO> selectScoreSearchList(ScoreSearchExportReqVO exportReqVO) {
        QueryWrapperX<ScoreDO> wrapperX = this.getScoreDOQueryWrapperX(exportReqVO.getStudentName(), exportReqVO.getStudentUid(), exportReqVO.getExamId());
        return this.selectScoreSearchListByXml(wrapperX);
    }


    default List<ScoreAllRespVo> selectScoreAllList(ScoreSearchAllReqVO reqVO) {
        QueryWrapperX<ScoreDO> wrapperX = new QueryWrapperX<ScoreDO>()
            .eqIfPresent("e.exam_id", reqVO.getExamId());
        return this.selectScoreAllListByXml(wrapperX);
    }


    default LambdaQueryWrapperX<ScoreDO> getYouXiuWrapper(Float score, Long cid, Long eid) {
        return new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, cid)
            .eq(ScoreDO::getExamId, eid)
            .geIfPresent(ScoreDO::getScore, score);
    }


    default Wrapper<ScoreDO> getLiangHaoWrapper(float youXiuScore, float liangHaoScore, Long cid, Long eid) {
        return new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, cid)
            .eq(ScoreDO::getExamId, eid)
            .ltIfPresent(ScoreDO::getScore, youXiuScore)
            .geIfPresent(ScoreDO::getScore, liangHaoScore);
    }

    default Wrapper<ScoreDO> getJiGeWrapper(float lianghaoScore, float jiGeScore, Long cid, Long eid) {
        return new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, cid)
            .eq(ScoreDO::getExamId, eid)
            .ltIfPresent(ScoreDO::getScore, lianghaoScore)
            .geIfPresent(ScoreDO::getScore, jiGeScore);
    }

    default Wrapper<ScoreDO> getBuJiGeWrapper(float jiGeScore, Long cid, Long eid) {
        return new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, cid)
            .eq(ScoreDO::getExamId, eid)
            .ltIfPresent(ScoreDO::getScore, jiGeScore);
    }

    // 选择优秀学生数量
    default Long selectYouXiuCount(float youXiuMin, ScoreAllCoursePieReqVO reqVO) {
        return this.selectCount(this.getYouXiuWrapper(youXiuMin, reqVO.getCid(), reqVO.getEid()));
    }

    // 选择良好学生数量
    default Long selectLiangHaoCount(float youXiuScore, float lianghaoScore, ScoreAllCoursePieReqVO reqVO) {
        // 良好<=良好数量 < 优秀
        return this.selectCount(this.getLiangHaoWrapper(youXiuScore, lianghaoScore, reqVO.getCid(), reqVO.getEid()));
    }

    // 及格学生数量
    default Long selectJiGeCount(float lianghaoScore, float jiGeScore, ScoreAllCoursePieReqVO reqVO) {
        // 及格<=及格数量 < 良好
        return this.selectCount(this.getJiGeWrapper(lianghaoScore, jiGeScore, reqVO.getCid(), reqVO.getEid()));
    }

    // 不及格学生数量
    default Long selectBuJiGeCount(float jigeScore, ScoreAllCoursePieReqVO reqVO) {
        // 不及格数量<及格
        return this.selectCount(this.getBuJiGeWrapper(jigeScore, reqVO.getCid(), reqVO.getEid()));
    }

    // 获取优秀学生列表
    default List<ScoreAllDetailTipRespVo> selectYouXiuStudentList(Float youxiuScore, ScoreAllDetailTipReqVO reqVO) {
        LambdaQueryWrapperX<ScoreDO> wrapperX = this.getYouXiuWrapper(youxiuScore, reqVO.getCid(), reqVO.getEid());
        return this.selectStudentListByXml(wrapperX);
    }

    // 获取良好学生列表
    default List<ScoreAllDetailTipRespVo> selectLiangHaoStudentList(Float youxiuScore, Float lianghaoScore, ScoreAllDetailTipReqVO reqVO) {
        return this.selectStudentListByXml(this.getLiangHaoWrapper(youxiuScore, lianghaoScore, reqVO.getCid(), reqVO.getEid()));
    }

    // 获取及格学生列表
    default List<ScoreAllDetailTipRespVo> selectJiGeStudentList(Float lianghaoScore, Float jiGeScore, ScoreAllDetailTipReqVO reqVO) {
        return this.selectStudentListByXml(this.getJiGeWrapper(lianghaoScore, jiGeScore, reqVO.getCid(), reqVO.getEid()));
    }

    // 获取不及格学生列表
    default List<ScoreAllDetailTipRespVo> selectBuJiGeStudentList(Float jiGeScore, ScoreAllDetailTipReqVO reqVO) {
        return this.selectStudentListByXml(this.getBuJiGeWrapper(jiGeScore, reqVO.getCid(), reqVO.getEid()));
    }

    List<ScoreAllDetailTipRespVo> selectMaxScoreStudentList(ScoreAllDetailTipReqVO reqVO);

    List<ScoreAllDetailTipRespVo> selectMinScoreStudentList(ScoreAllDetailTipReqVO reqVO);

    // 获取个人分析-图表1的数据
    default List<Chart1SeriesRespVO> getChart1SeriesById(Long studentId) {
        return this.getChart1SeriesByXml(this.getChartWrapper(studentId));
    }

    default QueryWrapperX<ScoreDO> getChartWrapper(Long studentId) {
        return new QueryWrapperX<ScoreDO>()
            .eqIfPresent("t.sid", studentId);
    }

    List<Chart1SeriesRespVO> getChart1SeriesByXml(@Param("ew") Wrapper<ScoreDO> wrapperX);

    // 获取各科排名折线图-图表2的数据
    default List<Chart2SeriesRespVO> getChart2SeriesBySid(Long studentId) {
        return this.getChart2SeriesByXml(this.getChartWrapper(studentId));
    }

    List<Chart2SeriesRespVO> getChart2SeriesByXml(@Param("ew") Wrapper<ScoreDO> chartWrapper);

    // 获取总分折线图-图表3的数据
    default List<Chart3DataVo> getChart3DataBySid(Long studentId) {
        return this.getChart3DataByXml(this.getChartWrapper(studentId));
    }

    List<Chart3DataVo> getChart3DataByXml(@Param("ew") Wrapper<ScoreDO> chartWrapper);

    default List<Chart4DataVo> getChart4DataBySid(Long studentId) {
        return this.getChart4DataByXml(this.getChartWrapper(studentId));
    }

    List<Chart4DataVo> getChart4DataByXml(@Param("ew") Wrapper<ScoreDO> wrapper);

}
