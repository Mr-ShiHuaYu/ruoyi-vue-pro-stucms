package cn.iocoder.yudao.module.stucms.controller.admin.course;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.stucms.controller.admin.course.vo.*;
import cn.iocoder.yudao.module.stucms.convert.course.CourseConvert;
import cn.iocoder.yudao.module.stucms.dal.dataobject.course.CourseDO;
import cn.iocoder.yudao.module.stucms.dal.dataobject.teacher.TeacherDO;
import cn.iocoder.yudao.module.stucms.service.course.CourseService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Api(tags = "管理后台 - 课程")
@RestController
@RequestMapping("/stucms/course")
@Validated
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/create")
    @ApiOperation("创建课程")
    @PreAuthorize("@ss.hasPermission('stucms:course:create')")
    public CommonResult<Long> createCourse(@Valid @RequestBody CourseCreateReqVO createReqVO) {
        return success(this.courseService.createCourse(createReqVO));
    }

    @PutMapping("/update")
    @ApiOperation("更新课程")
    @PreAuthorize("@ss.hasPermission('stucms:course:update')")
    public CommonResult<Boolean> updateCourse(@Valid @RequestBody CourseUpdateReqVO updateReqVO) {
        this.courseService.updateCourse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除课程")
    @ApiImplicitParam(name = "id", value = "编号", required = true, dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('stucms:course:delete')")
    public CommonResult<Boolean> deleteCourse(@RequestParam("id") Collection<Long> ids) {
        this.courseService.deleteCourse(ids);
        return success(true);
    }

    @GetMapping("/get")
    @ApiOperation("获得课程")
    @ApiImplicitParam(name = "id", value = "编号", required = true, example = "1024", dataTypeClass = Long.class)
    @PreAuthorize("@ss.hasPermission('stucms:course:query')")
    public CommonResult<CourseRespVO> getCourse(@RequestParam("id") Long id) {
        CourseDO course = this.courseService.getCourse(id);
        return success(CourseConvert.INSTANCE.convert(course));
    }

    @GetMapping("/list")
    @ApiOperation("获得课程列表")
    @ApiImplicitParam(name = "ids", value = "编号列表", required = true, example = "1024,2048", dataTypeClass = List.class)
    @PreAuthorize("@ss.hasPermission('stucms:course:query')")
    public CommonResult<List<CourseRespVO>> getCourseList(@RequestParam("ids") Collection<Long> ids) {
        List<CourseDO> list = this.courseService.getCourseList(ids);
        return success(CourseConvert.INSTANCE.convertList(list));
    }

    @Resource
    private TeacherService teacherService;

    @GetMapping("/page")
    @ApiOperation("获得课程分页")
    @PreAuthorize("@ss.hasPermission('stucms:course:query')")
    public CommonResult<PageResult<CourseRespVO>> getCoursePage(@Valid CoursePageReqVO pageVO) {
        PageResult<CourseDO> coursePage = this.courseService.getCoursePage(pageVO);
        if (CollUtil.isEmpty(coursePage.getList())) {
            return success(new PageResult<>(coursePage.getTotal())); // 返回空
        }
        // 1.获取coursePage中的老师id
        List<Long> teacherIds = convertList(coursePage.getList(), CourseDO::getTeacherId);
        // 2.获取这些老师
        List<TeacherDO> teacherList = this.teacherService.getTeacherList(teacherIds);
        Map<Long, TeacherDO> teacherMap = convertMap(teacherList, TeacherDO::getTeacherId);
        // 3.将老师组装到course中
        ArrayList<CourseRespVO> courseList = new ArrayList<>(coursePage.getList().size());
        coursePage.getList().forEach(courseDO -> {
            CourseRespVO courseRespVO = CourseConvert.INSTANCE.convert(courseDO);
            courseRespVO.setTeacher(CourseConvert.INSTANCE.convert1(teacherMap.get(courseDO.getTeacherId())));
            courseList.add(courseRespVO);
        });

        return success(new PageResult<>(courseList, coursePage.getTotal()));
    }

    @GetMapping("/export-excel")
    @ApiOperation("导出课程 Excel")
    @PreAuthorize("@ss.hasPermission('stucms:course:export')")
    @OperateLog(type = EXPORT)
    public void exportCourseExcel(@Valid CourseExportReqVO exportReqVO,
                                  HttpServletResponse response) throws IOException {
        List<CourseDO> list = this.courseService.getCourseList(exportReqVO);
        // 导出 Excel
        List<CourseExcelVO> datas = CourseConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "课程.xls", "数据", CourseExcelVO.class, datas);
    }

}
