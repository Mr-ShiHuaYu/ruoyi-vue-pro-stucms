package cn.iocoder.yudao.module.stucms.controller.admin.exam;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.stucms.controller.admin.exam.vo.*;
import cn.iocoder.yudao.module.stucms.convert.exam.ExamConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.exam.ExamDO;
import cn.iocoder.yudao.module.stucms.service.exam.ExamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Api(tags = "管理后台 - 考试")
@RestController
@RequestMapping("/stucms/exam")
@Validated
public class ExamController {

    @Resource
    private ExamService examService;

    @PostMapping("/create")
    @ApiOperation("创建考试")
    @PreAuthorize("@ss.hasPermission('stucms:exam:create')")
    public CommonResult<Long> createExam(@Valid @RequestBody ExamCreateReqVO createReqVO) {
        return success(this.examService.createExam(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新考试")
    @PreAuthorize("@ss.hasPermission('stucms:exam:update')")
    public CommonResult<Boolean> updateExam(@Valid @RequestBody ExamUpdateReqVO updateReqVO) {
        this.examService.updateExam(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除考试")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('stucms:exam:delete')")
    public CommonResult<Boolean> deleteExam(@RequestParam("id") Collection<Long> ids) {
        this.examService.deleteExam(ids);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得考试")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('stucms:exam:query')")
    public CommonResult<ExamRespVO> getExam(@RequestParam("id") Long id) {
        ExamDO exam = this.examService.getExam(id);
        return success(ExamConvert.INSTANCE.convert(exam));
    }

    @GetMapping("/list")
    @ApiOperation("获得考试列表")
    @PreAuthorize("@ss.hasPermission('stucms:exam:query')")
    public CommonResult<List<ExamSimpleVO>> getExamList() {
        List<ExamSimpleVO> examList = this.examService.getSimpleExamList();

        return success(examList);
    }

    @GetMapping("/name-list")
    @ApiOperation("获得考试名称列表")
    @PreAuthorize("@ss.hasPermission('stucms:exam:query')")
    public CommonResult<List<String>> getExamNameList() {
        List<ExamSimpleVO> examList = this.examService.getSimpleExamList();
        List<String> nameList = CollectionUtils.convertList(examList, ExamSimpleVO::getExamName);
        return success(nameList);
    }

    @GetMapping("/page")
    @ApiOperation("获得考试分页")
    @PreAuthorize("@ss.hasPermission('stucms:exam:query')")
    public CommonResult<PageResult<ExamRespVO>> getExamPage(@Valid ExamPageReqVO pageVO) {
        PageResult<ExamDO> pageResult = this.examService.getExamPage(pageVO);
        return success(ExamConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出考试 Excel")
    @PreAuthorize("@ss.hasPermission('stucms:exam:export')")
    @OperateLog(type = EXPORT)
    public void exportExamExcel(@Valid ExamExportReqVO exportReqVO,
                                HttpServletResponse response) throws IOException {
        List<ExamDO> list = this.examService.getExamList(exportReqVO);
        // 导出 Excel
        List<ExamExcelVO> datas = ExamConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "考试.xls", "数据", ExamExcelVO.class, datas);
    }

}
