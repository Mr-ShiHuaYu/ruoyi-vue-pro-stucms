package cn.iocoder.yudao.module.stucms.dal.mysql.score;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllCoursePieReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreAllRespVo;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.all.ScoreSearchAllReqVO;
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
     * 通过xml查询出成绩分页,返回值和第一个参数必须是分页对象/List  IPage<T> 或者继承IPage<T>的类型
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<ScoreSearchRespVO> selectScoreSearchPageByXml(@Param("page") Page<ScoreDO> page, @Param("ew") Wrapper<ScoreDO> wrapper);


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


    // 通过xml查询出全部列表
    List<ScoreSearchRespVO> selectScoreSearchListByXml(@Param("ew") Wrapper<ScoreDO> wrapper);

    default List<ScoreAllRespVo> selectScoreAllList(ScoreSearchAllReqVO reqVO) {
        QueryWrapperX<ScoreDO> wrapperX = new QueryWrapperX<ScoreDO>()
            .eqIfPresent("e.exam_id", reqVO.getExamId());
        return this.selectScoreAllListByXml(wrapperX);
    }

    List<ScoreAllRespVo> selectScoreAllListByXml(@Param("ew") Wrapper<ScoreDO> wrapperX);

    default Long selectYouXiuCount(float youXiuMin, ScoreAllCoursePieReqVO reqVO) {
        return this.selectCount(new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, reqVO.getCid())
            .eq(ScoreDO::getExamId, reqVO.getEid())
            .geIfPresent(ScoreDO::getScore, youXiuMin)
        );
    }

    default Long selectLiangHaoCount(float youXiuScore, float lianghaoScore, ScoreAllCoursePieReqVO reqVO) {
        // 良好<=良好数量 < 优秀
        return this.selectCount(new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, reqVO.getCid())
            .eq(ScoreDO::getExamId, reqVO.getEid())
            .ltIfPresent(ScoreDO::getScore, youXiuScore)
            .geIfPresent(ScoreDO::getScore, lianghaoScore)
        );
    }

    default Long selectJiGeCount(float lianghaoScore, float jiGeScore, ScoreAllCoursePieReqVO reqVO) {
        // 及格<=及格数量 < 良好
        return this.selectCount(new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, reqVO.getCid())
            .eq(ScoreDO::getExamId, reqVO.getEid())
            .ltIfPresent(ScoreDO::getScore, lianghaoScore)
            .geIfPresent(ScoreDO::getScore, jiGeScore)
        );
    }

    default Long selectBuJiGeCount(float jigeScore, ScoreAllCoursePieReqVO reqVO) {
        // 不及格数量<及格
        return this.selectCount(new LambdaQueryWrapperX<ScoreDO>()
            .eq(ScoreDO::getCourseId, reqVO.getCid())
            .eq(ScoreDO::getExamId, reqVO.getEid())
            .ltIfPresent(ScoreDO::getScore, jigeScore)
        );
    }
}
