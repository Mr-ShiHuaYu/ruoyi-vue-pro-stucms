<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="总分" prop="courseFull">
        <el-input v-model="queryParams.courseFull" placeholder="请输入总分" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="授课老师" prop="teacherId">
        <el-select v-model="queryParams.teacherId" placeholder="请选择授课老师" clearable @keyup.enter.native="handleQuery">
          <el-option
            v-for="item in teacherOptions"
            :key="item.teacherId"
            :label="item.name"
            :value="item.teacherId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['stucms:course:create']"
        >新增
        </el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stucms:course:delete']"
        >删除
        </el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['stucms:course:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <!--<el-table-column label="主键" align="center" prop="courseId" />-->
      <el-table-column v-hasPermi="['stucms:course:delete']" type="selection" width="55" align="center"/>
      <el-table-column label="序号" type="index" align="center"/>

      <el-table-column label="名称" align="center" prop="courseName"/>
      <el-table-column label="总分" align="center" prop="courseFull"/>
      <!--<el-table-column label="授课老师" align="center" prop="teacherId" />-->
      <el-table-column label="授课老师" align="center">
        <template slot-scope="{row}">
          <el-tag
            style="cursor: pointer;"
            effect="dark"
            size="small"
            @click="handleShowTeacher(row)"
            v-if="row.teacher"
          >
            {{ row.teacher.name }}
          </el-tag>
          <el-tag
            v-else
            type="info"
            effect="dark"
            size="small"
          >
            无
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" v-if="checkPermi(['stucms:course:update','stucms:course:delete'])">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['stucms:course:update']"
          >修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['stucms:course:delete']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"
    />

    <!-- 对话框(添加 / 修改) -->
    <el-dialog v-dialogDrag :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="总分" prop="courseFull">
          <el-input v-model="form.courseFull" placeholder="请输入总分"/>
        </el-form-item>
        <el-form-item label="授课老师" prop="teacherId">
          <el-select v-model="form.teacherId" placeholder="请选择授课老师">
            <el-option
              v-for="item in teacherOptions"
              :key="item.teacherId"
              :label="item.name"
              :value="item.teacherId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <show-teacher ref="showTeacherForm" :teacher-id="showTecherId"/>
  </div>
</template>

<script>
import {
  createCourse,
  deleteCourse,
  exportCourseExcel,
  getCourse,
  getCoursePage,
  updateCourse
} from '@/api/stucms/course'
import ShowTeacher from '@/views/stucms/course/showTeacher'
import {getTeacherList} from '@/api/stucms/teacher'

export default {
  name: 'Course',
  components: {
    ShowTeacher
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 课程列表
      list: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        courseName: null,
        courseFull: null,
        teacherId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        courseName: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
        courseFull: [{ required: true, message: '总分不能为空', trigger: 'blur' }],
        teacherId: [{ required: true, message: '授课老师不能为空', trigger: 'blur' }]
      },
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      showTecherId: null,
      teacherOptions: []
    }
  },
  created() {
    this.getList()
    this.getTeacherList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      // 处理查询参数
      let params = { ...this.queryParams }
      // 执行查询
      getCoursePage(params).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.loading = false
      })
    },
    getTeacherList() {
      getTeacherList().then(response => {
        this.teacherOptions = response.data
      }).catch(() => {
      })
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        courseId: undefined,
        courseName: undefined,
        courseFull: undefined,
        teacherId: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加课程'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const courseId = row.courseId
      getCourse(courseId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改课程'
      }).catch(() => {
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        // 修改的提交
        if (this.form.courseId != null) {
          updateCourse(this.form).then(response => {
            this.$modal.msgSuccess('修改成功')
            this.open = false
            this.getList()
          })
          return
        }
        // 添加的提交
        createCourse(this.form).then(response => {
          this.$modal.msgSuccess('新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const courseId = row.courseId || this.ids;
      let courseNames = "";
      if (Array.isArray(courseId)) {
        courseNames = this.list.filter(item => {
          return courseId.includes(item.courseId)
        })
          .map(c => c.courseName)
      } else {
        courseNames = row.courseName
      }

      this.$modal.confirm('是否确认删除课程为"' + courseNames + '"的数据项?').then(function() {
        return deleteCourse(courseId.toString())
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = { ...this.queryParams }
      params.pageNo = undefined
      params.pageSize = undefined
      // 执行导出
      this.$modal.confirm('是否确认导出所有课程数据项?').then(() => {
        this.exportLoading = true
        return exportCourseExcel(params)
      }).then(response => {
        this.$download.excel(response, '课程.xls')
        this.exportLoading = false
      }).catch(() => {
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.courseId)
      this.multiple = !selection.length
    },
    /**
     * 显示点击的老师信息
     * @param row
     */
    handleShowTeacher(row) {
      this.showTecherId = row.teacher.teacherId
      this.$refs.showTeacherForm.show()
    }

  }
}
</script>
