package cn.iocoder.yudao.module.stucms.controller.admin.student.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@ApiModel("管理后台 - 学生管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentRespVO extends StudentBaseVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "班级ID")
    private Long deptId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "身份证号")
    private String sysid;

    @ApiModelProperty(value = "户口")
    private String hukou;

    @ApiModelProperty(value = "户籍地址")
    private String huji;

    @ApiModelProperty(value = "现住址")
    private String xianzz;

    @ApiModelProperty(value = "毕业学校")
    private String biye;

    @ApiModelProperty(value = "获奖情况")
    private String huojiang;

    /**
     * 所在部门
     */
    private Dept dept;

    @ApiModel("班级")
    @Data
    public static class Dept {

        @ApiModelProperty(value = "班级ID", required = true, example = "1")
        private Long id;

        @ApiModelProperty(value = "班级名称", required = true, example = "高一1班")
        private String name;

    }
}
