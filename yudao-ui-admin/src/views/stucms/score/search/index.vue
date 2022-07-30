<template>
  <div class="app-container">
    <elem-quote text="1. 可按学生姓名,学号和考试模糊搜索<br>
      2. 标准差表示成绩的离散程度,标准差越小，表示成绩越集中于平均成绩"/>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item prop="studentName">
        <el-input
          v-model="queryParams.studentName"
          placeholder="请输入学生姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item prop="studentUid">
        <el-input
          v-model="queryParams.studentUid"
          placeholder="请输入学号"
          clearable
          @keyup.enter.native="handleQuery"
          @clear="handleQuery"
        />
      </el-form-item>

      <el-form-item prop="examId">
        <el-select
          v-model="queryParams.examId"
          placeholder="请选择考试"
          clearable
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="(item, index) in examOptions"
            :key="index"
            :label="item.examName"
            :value="item.examId"/>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:score:import']"
        >导入</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['stucms:score:export']"
        >导出
        </el-button>
      </el-col>

      <score-right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns.sync="columns"/>
    </el-row>

    <!--表格-->
    <el-table border highlight-current-row v-loading="loading" :data="scoreList" :key="key">
      <el-table-column label="序号" type="index" align="center" v-if="isShow('序号')"/>
      <el-table-column label="姓名" align="center" prop="studentName" v-if="isShow('姓名')"/>

      <el-table-column min-width="100" label="学号" align="center" prop="studentUid" v-if="isShow('学号')"/>
      <el-table-column min-width="100" label="考试" align="center" prop="examName" v-if="isShow('考试')"/>
      <!--循环遍历课程-->
      <el-table-column
        width="70"
        v-for="course in coursesList"
        :key="course.courseId"
        :label="course.courseName"
        v-if="isShow(course.courseName)"
        align="center"
        :prop="course.courseName">
        <template slot-scope="{row, $index}">
          <div style="cursor: pointer;"
               @dblclick="clickDiv($event, row)"
               @blur="divBlur($event, row, course, $index)"
          >
            {{ row[course.courseName] }}
          </div>
        </template>
      </el-table-column>

      <el-table-column label="标准差" align="center" prop="std" v-if="isShow('标准差')"/>
      <el-table-column label="平均分" align="center" prop="avg" v-if="isShow('平均分')"/>
      <el-table-column label="总分" align="center" prop="sum" v-if="isShow('总分')"/>

    </el-table>

    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!--<excel-upload-->
    <!--  :title="upload.title"-->
    <!--  :open.sync="upload.open"-->
    <!--  :upload-url="upload.uploadUrl"-->
    <!--  :template-url="upload.templateUrl"-->
    <!--  @success="getList"-->
    <!--/>-->
  </div>
</template>

<script>
import {listScore, updateScore} from '@/api/stucms/score/search'
import ScoreRightToolbar from '@/views/stucms/score/RightToolbar'
import ElemQuote from "@/views/components/elemQuote";
import {exportExamExcel} from "@/api/stucms/exam";
// import ExcelUpload from "@/views/components/excelUpload";

export default {
  name: 'Score',
  components: {ElemQuote, ScoreRightToolbar},
  data() {
    return {
      // 遮罩层
      loading: true,
      key: 0,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 成绩表格数据
      scoreList: [],
      coursesList: [],
      examOptions: [],
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "成绩导入",
        // 上传的地址
        uploadUrl: "stucms/score/importData",
        // 下载模板地址
        templateUrl: "stucms/score/importTemplate"
      },
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        studentName: undefined,
        studentUid: undefined,
        examId: undefined
      },
      // 表单参数
      form: {},
      row: {},
      beforeCols: [
        {label: `序号`, visible: true},
        {label: `姓名`, visible: true},
        {label: `学号`, visible: true},
        {label: `考试`, visible: true}
      ],
      lastCols: [
        {label: `标准差`, visible: true},
        {label: `平均分`, visible: true},
        {label: `总分`, visible: true}
      ],
      cachedColumns: []
    }
  },
  created() {
    this.getList()
    this.getCachedColumns()
  },
  computed: {
    columns: {
      get() {
        if (!this.cachedColumns) {
          // 如果缓存列中没有值,则从数据中获取
          let columns = []
          let key = 0
          this.beforeCols.forEach(c => {
            columns.push({
              key: key++,
              label: c.label,
              visible: true
            })
          })

          this.coursesList.forEach(c => {
            columns.push({
              key: key++,
              label: c.courseName,
              visible: true
            })
          })

          this.lastCols.forEach(c => {
            columns.push({
              key: key++,
              label: c.label,
              visible: true
            })
          })
          return columns
        } else {
          // 否则,直接返回缓存列中的数据
          return this.cachedColumns
        }
      },
      set(newVal) {
        // 当右侧列更新时
        newVal.forEach(col => {
          this.beforeCols.forEach((b, index) => {
            if (b.label === col.label) {
              b.visible = col.visible
            }
          })
          this.coursesList.forEach(c => {
            if (c.label === col.label) {
              c.visible = col.visible
            }
          })
          this.lastCols.forEach(b => {
            if (b.label === col.label) {
              b.visible = col.visible
            }
          })
        })
        // 触发表格的刷新
        this.key++
        // 设置到缓存中
        this.$cache.local.setJSON(this.$route.path, newVal)
      }
    }
  },
  methods: {
    /** 查询成绩列表 */
    getList() {
      this.loading = true
      listScore(this.queryParams).then(response => {
        this.scoreList = response.data.scores.list
        this.total = response.data.scores.total
        this.coursesList = response.data.courses
        this.examOptions = response.data.exams
        this.loading = false
      })
    },
    isShow(labelName) {
      let isShow = true
      for (let i = 0; i < this.columns.length; i++) {
        const col = this.columns[i]
        if (col.label === labelName) {
          isShow = col.visible
          break
        }
      }
      return isShow
    },
    // 表单重置
    reset() {
      this.form = {
        examId: undefined,
        courseId: undefined,
        studentId: undefined,
        oldScore: undefined
      }
      this.row = {}
    },
    // 回滚原来的成绩
    rollbackScore(index) {
      this.scoreList.splice(index, 1, this.row)
      // 让key变更,使得表格重新渲染
      this.key++
      this.reset()
      this.loading = false
    },
    // div双击事件,给元素添加可编辑属性contenteditable
    clickDiv(e, row) {
      this.row = row
      e.target.setAttribute('contenteditable', true)
      e.target.style.border = '1px solid #ccc'
      e.target.focus()
      this.form.oldScore = e.target.innerText
    },
    checkNewScore(course, newScore) {
      const regPos = /^\d+$/ //判断是否是数字。
      if (!regPos.test(newScore)) {
        this.$throw(new Error(`${newScore}不是纯数字`))
        return false
      }
      const courseFull = course.courseFull
      // 2.如果修改的成绩不在满分的范围内,则抛出异常
      if ((newScore < 0 || newScore > courseFull)) {
        this.$throw(new Error(`${course.courseName}成绩必须在0-${courseFull}之间`))
        return false
      } else {
        // 检查通过
        return true
      }
    },
    // 鼠标失去焦点事件
    divBlur(e, row, course, index) {
      // index为第几行的表格
      // row 为第几行的数据
      // course 为当前列的课程
      const courseName = course.courseName
      e.target.setAttribute('contenteditable', false)
      e.target.style.border = '0 none'
      let newScore = e.target.innerText.trim()
      if (newScore === this.form.oldScore) return
      // 检查要修改的成绩是否正常,是否符合0-满分的范围
      const booleanCheckResult = this.checkNewScore(course, newScore)
      if (!booleanCheckResult) {
        // 回滚页面的成绩
        this.rollbackScore(index)
        return
      }
      // 要修改的分数正常,提示要不要修改
      this.$modal.confirm(`确定将<span style="color: red;">${row.studentName}</span>的<span  style="color: red;">${courseName}</span>成绩改为<span style="color: red;">${newScore}</span>?`).then(() => {
        this.loading = true
        this.form = {
          examId: row.examId,
          courseId: course.courseId,
          studentId: row.studentId,
          score: newScore
        }
        return updateScore(this.form)
      }).then(() => {
        // 修改成功,重新获取数据
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('修改成功')
      }).catch(() => {
        // 点击取消,恢复之前的成绩
        this.rollbackScore(index)
      })
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
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有成绩?').then(() => {
        this.exportLoading = true;
        return exportExamExcel(params);
      }).then(response => {
        this.$download.excel(response, '成绩.xls');
        this.exportLoading = false;
      }).catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true;
    },
    getCachedColumns() {
      // 尝试从缓存中拿列数据
      const columns = this.$cache.local.getJSON(this.$route.path)
      if (columns) {
        this.cachedColumns = columns
      }
    }
  }
}
</script>
