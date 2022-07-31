package cn.iocoder.yudao.module.stucms.dal.mysql.exam;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.ExamPageReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 考试 Mapper
 *
 * @author hua
 */
@Mapper
public interface ExamMapper extends BaseMapperX<ExamDO> {

    default PageResult<ExamDO> selectPage(ExamPageReqVO reqVO) {
        return this.selectPage(reqVO, new LambdaQueryWrapperX<ExamDO>()
            .likeIfPresent(ExamDO::getExamName, reqVO.getExamName())
            .betweenIfPresent(ExamDO::getExamTime, reqVO.getExamTime())
            .orderByDesc(ExamDO::getExamId));
    }

    default List<ExamDO> selectList(ExamExportReqVO reqVO) {
        return this.selectList(new LambdaQueryWrapperX<ExamDO>()
            .likeIfPresent(ExamDO::getExamName, reqVO.getExamName())
            .betweenIfPresent(ExamDO::getExamTime, reqVO.getExamTime())
            .orderByDesc(ExamDO::getExamId));
    }

    default ExamDO selectOneByExamName(String examName) {
        return this.selectOne(new LambdaQueryWrapperX<ExamDO>()
            .eqIfPresent(ExamDO::getExamName, examName));
    }
}
