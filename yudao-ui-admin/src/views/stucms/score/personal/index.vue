<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <elem-quote text="1. 表中为至少有一门成绩的人,没成绩则不显示<br>
          2. 单击某个学生来具体查看成绩变化图"/>

        <!--搜索-->
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          @submit.native.prevent>
          <!--@submit.native.prevent解决当只有一个input框时,回车会刷新页面的问题-->
          <el-form-item prop="studentName">
            <el-input
              v-model="queryParams.studentName"
              placeholder="请输入学生姓名"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <!--表格-->
        <el-table v-loading="loading" :data="studentList" :key="key"
                  @row-click="rowClick"
                  row-class-name="row-cursor"
        >
          <el-table-column label="序号" type="index" align="center"/>
          <el-table-column label="姓名" align="center" prop="studentName"/>
          <el-table-column label="学号" align="center" prop="studentUid"/>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          layout="sizes, prev, pager, next, total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getSimpleStudentList"
        />
      </el-col>

      <el-col :span="18">
        <el-row :gutter="20">
          <el-col :span="12">
            <chart1
              :exams="examList"
              :courses="courseList"
              :student-id="studentId"
              :student-name="studentName"
            />
          </el-col>

          <!--<el-col :span="12">-->
          <!--  <chart2-->
          <!--    :exams="examList"-->
          <!--    :courses="courseList"-->
          <!--    :student-id="studentId"/>-->
          <!--</el-col>-->

          <!--<el-col :span="12">-->
          <!--  <chart3-->
          <!--    :exams="examList"-->
          <!--    :courses="courseList"-->
          <!--    :student-id="studentId"/>-->
          <!--</el-col>-->

          <!--<el-col :span="12">-->
          <!--  <chart4-->
          <!--    :exams="examList"-->
          <!--    :courses="courseList"-->
          <!--    :student-id="studentId"/>-->
          <!--</el-col>-->

        </el-row>

      </el-col>
    </el-row>
  </div>
</template>

<script>
import Chart1 from "@/views/stucms/score/personal/Chart1";
import Chart2 from "@/views/stucms/score/personal/Chart2";
import Chart3 from "@/views/stucms/score/personal/Chart3";
import Chart4 from "@/views/stucms/score/personal/Chart4";
import {getSimpleStudentList} from "@/api/stucms/student";
import ElemQuote from "@/views/components/elemQuote";
import {getExamNameList} from "@/api/stucms/exam";
import {getCourseNameList} from "@/api/stucms/course";

export default {
  name: 'Personal',
  components: {ElemQuote, Chart1, Chart2, Chart3, Chart4},
  data() {
    return {
      // 遮罩层
      loading: true,
      key: 0,
      // 总条数
      total: 0,
      // 成绩表格数据
      studentList: [],
      examList: [],
      courseList: [],
      studentId: 0,
      studentName: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentName: undefined,
      }
    }
  },
  created() {
    this.getSimpleStudentList()
    this.getNameExamList()
    this.getCourseNameList()
  },
  methods: {
    /** 查询成绩列表 */
    getSimpleStudentList() {
      this.loading = true
      getSimpleStudentList(this.queryParams).then(response => {
        this.studentList = response.data.list // 学生列表
        this.total = response.data.total
        this.loading = false
        this.$nextTick(() => {
          if (this.studentList.length) {
            this.studentId = this.studentList[0].id
            this.studentName = this.studentList[0].studentName
          }
        })
      })
    },
    getNameExamList() {
      getExamNameList().then(response => {
        this.examList = response.data
      })
    },
    getCourseNameList() {
      getCourseNameList().then(response => {
        this.courseList = response.data
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getSimpleStudentList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    rowClick(row) {
      this.studentId = row.id
      this.studentName = row.studentName
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .row-cursor {
  cursor: pointer;
}
</style>
