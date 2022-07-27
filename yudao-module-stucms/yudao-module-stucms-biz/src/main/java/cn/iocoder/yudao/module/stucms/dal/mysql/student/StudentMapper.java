package cn.iocoder.yudao.module.stucms.dal.mysql.student;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.StudentExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.StudentPageReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 学生管理 Mapper
 *
 * @author 华
 */
@Mapper
public interface StudentMapper extends BaseMapperX<StudentDO> {

    default PageResult<StudentDO> selectPage(StudentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getStudentUid, reqVO.getStudentUid())
                .likeIfPresent(StudentDO::getStudentName, reqVO.getStudentName())
                .eqIfPresent(StudentDO::getSex, reqVO.getSex())
                .likeIfPresent(StudentDO::getPhone, reqVO.getPhone())
                .likeIfPresent(StudentDO::getSysid, reqVO.getSysid())
                .eqIfPresent(StudentDO::getJishu, reqVO.getJishu())
                .eqIfPresent(StudentDO::getLiushou, reqVO.getLiushou()));
    }

    default List<StudentDO> selectList(StudentExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getStudentUid, reqVO.getStudentUid())
                .likeIfPresent(StudentDO::getStudentName, reqVO.getStudentName())
                .eqIfPresent(StudentDO::getSex, reqVO.getSex())
                .likeIfPresent(StudentDO::getPhone, reqVO.getPhone())
                .likeIfPresent(StudentDO::getSysid, reqVO.getSysid())
                .eqIfPresent(StudentDO::getJishu, reqVO.getJishu())
                .eqIfPresent(StudentDO::getLiushou, reqVO.getLiushou())
                .betweenIfPresent(StudentDO::getCreateTime, reqVO.getCreateTime()));
    }

}
