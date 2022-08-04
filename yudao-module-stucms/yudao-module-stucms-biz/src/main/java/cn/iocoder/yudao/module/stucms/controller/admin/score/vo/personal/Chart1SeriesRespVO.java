package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 图1的序列数据
 */
@Data
public class Chart1SeriesRespVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程名称")
    private String name;

    @ApiModelProperty(",连接的课程分数")
    @JsonIgnore
    private String scores;

    @ApiModelProperty(",连接的课程排名")
    @JsonIgnore
    private String ranks;

    @ApiModelProperty("图表类型")
    private String type = "line";

    @ApiModelProperty("线条平滑")
    private Boolean smooth = true;

    @ApiModelProperty("每个点的数据,echarts默认会取value的值,在里面存了rank")
    private List<Map<String, Object>> data;
}
