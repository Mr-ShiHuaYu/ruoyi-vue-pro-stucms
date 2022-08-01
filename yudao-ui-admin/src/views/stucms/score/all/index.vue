<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px" v-show="showSearch">
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
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sourceList">
      <el-table-column label="序号" type="index" align="center"/>
      <el-table-column label="考试" align="center" prop="exam"/>
      <el-table-column label="课程" align="center" prop="course">
        <template slot-scope="{row}">
          <el-tag
            @click="handleShowCourseChart(row)"
            size="mini"
            effect="dark">
            {{ row.course }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="满分" align="center" prop="full"/>
      <el-table-column label="参考人数" align="center" prop="joinNum"/>
      <el-table-column label="优秀" align="center" prop="youxiu">
        <template slot-scope="{row,column}">
          <detail-tip :row="row" :column="column"/>
        </template>
      </el-table-column>
      <el-table-column label="良好" align="center" prop="lianghao">
        <template slot-scope="{row,column}">
          <detail-tip :row="row" :column="column"/>
        </template>
      </el-table-column>
      <el-table-column label="及格" align="center" prop="jige">
        <template slot-scope="{row,column}">
          <detail-tip :row="row" :column="column"/>
        </template>
      </el-table-column>
      <el-table-column label="不及格" align="center" prop="bujige">
        <template slot-scope="{row,column}">
          <detail-tip :row="row" :column="column" type="danger"/>
        </template>
      </el-table-column>
      <el-table-column label="最高分" align="center" prop="max">
        <template slot-scope="{row,column}">
          <detail-tip :row="row" :column="column" type="success"/>
        </template>
      </el-table-column>
      <el-table-column label="最低分" align="center" prop="min">
        <template slot-scope="{row,column}">
          <detail-tip :row="row" :column="column" type="danger"/>
        </template>
      </el-table-column>
      <el-table-column label="平均分" align="center" prop="avg"/>
      <el-table-column label="标准差" align="center" prop="std"/>
    </el-table>

    <!--显示每个课程分析的对话框-->
    <el-dialog v-dialogDrag :title="title" :visible.sync="open" width="500px" append-to-body>
      <chart-pie :row="row"/>
    </el-dialog>

  </div>
</template>

<script>
import {listAllScore} from "@/api/stucms/score/all";
import DetailTip from "@/views/stucms/score/all/DetailTip";
import ChartPie from "@/views/stucms/score/all/ChartPie";
import {getExamList} from "@/api/stucms/exam";

export default {
  name: 'ScoreAll',
  components: {ChartPie, DetailTip},
  data() {
    return {
      open: false,
      title: "",
      // 显示搜索条件
      showSearch: true,
      // 遮罩层
      loading: true,
      row: {},
      // 查询参数
      queryParams: {
        examId: 1
      },
      sourceList: [],
      examOptions: [],
    }
  },
  created() {
    this.getList()
    this.getExamList()
  },
  methods: {
    /** 查询成绩列表 */
    getList() {
      this.loading = true
      listAllScore(this.queryParams).then(response => {
        this.sourceList = response.data.map(item => {
          item.content = ""
          return item
        })
        this.loading = false
      })
    },
    getExamList() {
      getExamList().then(response => {
        this.examOptions = response.data
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    handleShowCourseChart(row) {
      this.row = row
      this.title = `${row.exam}${row.course}课程分析`
      this.open = true
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .cell .el-tag {
  cursor: pointer !important;
}
</style>
