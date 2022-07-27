package cn.iocoder.yudao.module.stucms.controller.admin.teacher;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.stucms.controller.admin.teacher.vo.*;
import cn.iocoder.yudao.module.stucms.convert.teacher.TeacherConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.service.teacher.TeacherService;
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
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;


@Api(tags = "管理后台 - 老师")
@RestController
@RequestMapping("/stucms/teacher")
@Validated
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    @PostMapping("/create")
    @ApiOperation("创建老师")
    @PreAuthorize("@ss.hasPermission('stucms:teacher:create')")
    public CommonResult<Long> createTeacher(@Valid @RequestBody TeacherCreateReqVO createReqVO) {
        return success(teacherService.createTeacher(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新老师")
    @PreAuthorize("@ss.hasPermission('stucms:teacher:update')")
    public CommonResult<Boolean> updateTeacher(@Valid @RequestBody TeacherUpdateReqVO updateReqVO) {
        teacherService.updateTeacher(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除老师")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('stucms:teacher:delete')")
    public CommonResult<Boolean> deleteTeacher(@RequestParam("id") Long id) {
        teacherService.deleteTeacher(id);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得老师")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('stucms:teacher:query')")
    public CommonResult<TeacherRespVO> getTeacher(@RequestParam("id") Long id) {
        TeacherDO teacher = teacherService.getTeacher(id);
        return success(TeacherConvert.INSTANCE.convert(teacher));
    }

    // @GetMapping("/list")
    // @ApiOperation("获得老师列表")
    // @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    // @PreAuthorize("@ss.hasPermission('stucms:teacher:query')")
    // public CommonResult<List<TeacherRespVO>> getTeacherList(@RequestParam("ids") Collection<Long> ids) {
    //     List<TeacherDO> list = teacherService.getTeacherList(ids);
    //     return success(TeacherConvert.INSTANCE.convertList(list));
    // }

    @GetMapping("/page")
    @ApiOperation("获得老师分页")
    @PreAuthorize("@ss.hasPermission('stucms:teacher:query')")
    public CommonResult<PageResult<TeacherRespVO>> getTeacherPage(@Valid TeacherPageReqVO pageVO) {
        PageResult<TeacherDO> pageResult = teacherService.getTeacherPage(pageVO);
        return success(TeacherConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出老师 Excel")
    @PreAuthorize("@ss.hasPermission('stucms:teacher:export')")
    @OperateLog(type = EXPORT)
    public void exportTeacherExcel(@Valid TeacherExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<TeacherDO> list = teacherService.getTeacherList(exportReqVO);
        // 导出 Excel
        List<TeacherExcelVO> datas = TeacherConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "老师.xls", "数据", TeacherExcelVO.class, datas);
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获取老师精简信息列表", notes = "只包含被开启的老师，主要用于前端的下拉选项")
    @PreAuthorize("@ss.hasPermission('stucms:teacher:query')")
    public CommonResult<List<TeacherSimpleRespVO>> getSimpleTeachers() {

        List<TeacherDO> list = teacherService.getSimpleTeachers();
        return success(TeacherConvert.INSTANCE.convertSimpleList(list));
    }

}
