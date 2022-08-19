package cn.iocoder.yudao.module.stucms.dal.mysql.teacher;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.TeacherPageReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 老师 Mapper
 *
 * @author hua
 */
@Mapper
public interface TeacherMapper extends BaseMapperX<TeacherDO> {

    default PageResult<TeacherDO> selectPage(TeacherPageReqVO reqVO) {
        LambdaQueryWrapperX<TeacherDO> queryWrapperX = new LambdaQueryWrapperX<TeacherDO>()
            .likeIfPresent(TeacherDO::getName, reqVO.getName())
            .likeIfPresent(TeacherDO::getPhone, reqVO.getPhone())
            .eqIfPresent(TeacherDO::getSex, reqVO.getSex())
            .likeIfPresent(TeacherDO::getQq, reqVO.getQq())
            .betweenIfPresent(TeacherDO::getCreateTime, reqVO.getCreateTime());
        return selectPage(reqVO, queryWrapperX);
    }

    default List<TeacherDO> selectList(TeacherExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<TeacherDO>()
            .likeIfPresent(TeacherDO::getName, reqVO.getName())
            .likeIfPresent(TeacherDO::getPhone, reqVO.getPhone())
            .eqIfPresent(TeacherDO::getSex, reqVO.getSex())
            .likeIfPresent(TeacherDO::getQq, reqVO.getQq())
            .betweenIfPresent(TeacherDO::getCreateTime, reqVO.getCreateTime())
            .orderByDesc(TeacherDO::getTeacherId));
    }

    default TeacherDO selectOneByPhone(String phone) {
        return selectOne(TeacherDO::getPhone, phone);
    }
}
