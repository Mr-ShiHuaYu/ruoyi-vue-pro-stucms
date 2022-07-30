package cn.iocoder.yudao.module.stucms.dal.mysql.score;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchPageReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.score.ScoreDO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 考试 Mapper
 *
 * @author hua
 */
@Mapper
public interface ScoreMapper extends BaseMapperX<ScoreDO> {

    default PageResult<ScoreSearchRespVO> selectScoreSearchPage(ScoreSearchPageReqVO reqVO) {
        QueryWrapperX<ScoreDO> wrapperX = new QueryWrapperX<ScoreDO>()
            .likeIfPresent("st.student_name", reqVO.getStudentName())
            .likeIfPresent("st.student_uid", reqVO.getStudentUid())
            .eqIfPresent("sc.exam_id", reqVO.getExamId());
        // MyBatis Plus 查询
        Page<ScoreDO> mpPage = MyBatisUtils.buildPage(reqVO);
        Page<ScoreSearchRespVO> respVOPage = selectScoreSearchPageByXml(mpPage, wrapperX);
        return new PageResult<>(respVOPage.getRecords(), respVOPage.getTotal());
    }

    // 通过xml查询,返回值和第一个参数必须是分页对象/List  IPage<T> 或者继承IPage<T>的类型
    Page<ScoreSearchRespVO> selectScoreSearchPageByXml(@Param("page") Page<ScoreDO> page, @Param("ew") Wrapper<ScoreDO> wrapper);

    default ScoreDO selectOneByStudentIdAndExamIdAndCourseId(ScoreDO score) {
        return selectOne(new LambdaQueryWrapperX<ScoreDO>()
            .eqIfPresent(ScoreDO::getStudentId, score.getStudentId())
            .eqIfPresent(ScoreDO::getExamId, score.getExamId())
            .eqIfPresent(ScoreDO::getCourseId, score.getCourseId())
        );
    }
}
