package cn.iocoder.yudao.module.stucms.convert.exam;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.*;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;

/**
 * 考试 Convert
 *
 * @author hua
 */
@Mapper
public interface ExamConvert {

    ExamConvert INSTANCE = Mappers.getMapper(ExamConvert.class);

    ExamDO convert(ExamCreateReqVO bean);

    ExamDO convert(ExamUpdateReqVO bean);

    ExamRespVO convert(ExamDO bean);

    List<ExamRespVO> convertList(List<ExamDO> list);

    PageResult<ExamRespVO> convertPage(PageResult<ExamDO> page);

    List<ExamExcelVO> convertList02(List<ExamDO> list);

}
