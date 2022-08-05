package cn.iocoder.yudao.module.stucms.controller.admin.score.vo.personal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 图2的序列数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chart2SeriesRespVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程名称")
    private String name;

    @ApiModelProperty(",连接的课程排名")
    @JsonIgnore
    private String ranks;

    @ApiModelProperty("图表类型")
    private String type = "line";

    @ApiModelProperty("线条平滑")
    private Boolean smooth = true;

    @ApiModelProperty("每个课程的排名数组,对上面的ranks属性用,分割")
    private List<String> data;
}
