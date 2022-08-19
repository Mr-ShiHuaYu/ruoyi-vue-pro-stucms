package cn.iocoder.yudao.module.stucms.convert.teacher;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.api.teacher.dto.TeacherRespDTO;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.*;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 老师 Convert
 *
 * @author hua
 */
@Mapper
public interface TeacherConvert {

    TeacherConvert INSTANCE = Mappers.getMapper(TeacherConvert.class);

    TeacherDO convert(TeacherCreateReqVO bean);

    TeacherDO convert(TeacherUpdateReqVO bean);

    TeacherRespVO convert(TeacherDO bean);

    List<TeacherRespVO> convertList(List<TeacherDO> list);
    List<TeacherSimpleRespVO> convertSimpleList(List<TeacherDO> list);

    PageResult<TeacherRespVO> convertPage(PageResult<TeacherDO> page);

    List<TeacherExcelVO> convertList02(List<TeacherDO> list);

    TeacherRespDTO convertDTO(TeacherDO teacherDO);
}
