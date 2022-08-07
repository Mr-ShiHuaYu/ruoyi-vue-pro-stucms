package cn.iocoder.yudao.module.stucms.convert.student;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.stucms.api.student.dto.StudentRespDTO;
import cn.iocoder.yudao.module.stucms.controller.admin.student.vo.*;
import cn.iocoder.yudao.module.stucms.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 学生管理 Convert
 *
 * @author 华
 */
@Mapper
public interface StudentConvert {

    StudentConvert INSTANCE = Mappers.getMapper(StudentConvert.class);

    StudentDO convert(StudentCreateReqVO bean);

    StudentDO convert(StudentUpdateReqVO bean);

    StudentRespVO convert(StudentDO bean);

    List<StudentRespVO> convertList(List<StudentDO> list);

    PageResult<StudentRespVO> convertPage(PageResult<StudentDO> page);

    List<StudentExcelVO> convertList02(List<StudentDO> list);

    PageResult<StudentSimpleRespVO> convertSimplePage(PageResult<StudentDO> list);

    StudentRespDTO convertDto(StudentDO studentDO);

    StudentRespVO.Dept convert01(DeptRespDTO dept);
}
