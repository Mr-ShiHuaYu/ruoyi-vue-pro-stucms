package cn.iocoder.yudao.module.stucms.convert.score;

import cn.iocoder.yudao.module.stucms.controller.admin.score.vo.search.ScoreSearchRespVO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.score.ScoreDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 成绩查询 Convert
 *
 * @author hua
 */
@Mapper
public interface ScoreSearchConvert {

    ScoreSearchConvert INSTANCE = Mappers.getMapper(ScoreSearchConvert.class);

    ScoreDO convertToDO(ScoreSearchRespVO score);
}
