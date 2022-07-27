package cn.iocoder.yudao.module.stucms.convert.course;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseCreateReqVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseExcelVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseRespVO;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.CourseUpdateReqVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 课程 Convert
 *
 * @author hua
 */
@Mapper
public interface CourseConvert {

    CourseConvert INSTANCE = Mappers.getMapper(CourseConvert.class);

    CourseDO convert(CourseCreateReqVO bean);

    CourseDO convert(CourseUpdateReqVO bean);

    CourseRespVO convert(CourseDO bean);

    List<CourseRespVO> convertList(List<CourseDO> list);

    PageResult<CourseRespVO> convertPage(PageResult<CourseDO> page);

    List<CourseExcelVO> convertList02(List<CourseDO> list);

    CourseRespVO.Teacher convert1(TeacherDO teacherDO);
}
