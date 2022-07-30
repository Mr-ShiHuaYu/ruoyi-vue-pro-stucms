package cn.iocoder.yudao.module.stucms.dal.mysql.score;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
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

}
