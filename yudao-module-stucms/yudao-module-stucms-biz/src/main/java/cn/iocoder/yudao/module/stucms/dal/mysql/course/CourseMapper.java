package cn.iocoder.yudao.module.stucms.dal.mysql.course;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseExportReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CoursePageReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 课程 Mapper
 *
 * @author hua
 */
@Mapper
public interface CourseMapper extends BaseMapperX<CourseDO> {

    default PageResult<CourseDO> selectPage(CoursePageReqVO reqVO) {
        return this.selectPage(reqVO, new LambdaQueryWrapperX<CourseDO>()
            .likeIfPresent(CourseDO::getCourseName, reqVO.getCourseName())
            .eqIfPresent(CourseDO::getCourseFull, reqVO.getCourseFull())
            .eqIfPresent(CourseDO::getTeacherId, reqVO.getTeacherId())
        );
    }


    default List<CourseDO> selectList(CourseExportReqVO reqVO) {
        return this.selectList(new LambdaQueryWrapperX<CourseDO>()
            .likeIfPresent(CourseDO::getCourseName, reqVO.getCourseName())
            .eqIfPresent(CourseDO::getCourseFull, reqVO.getCourseFull())
            .eqIfPresent(CourseDO::getTeacherId, reqVO.getTeacherId())
            .orderByDesc(CourseDO::getCourseId));
    }

    default CourseDO selectOneByCourseName(String CourseName) {
        return this.selectOne(CourseDO::getCourseName, CourseName);
    }
}
