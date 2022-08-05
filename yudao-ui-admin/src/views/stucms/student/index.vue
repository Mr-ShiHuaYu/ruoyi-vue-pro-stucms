<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学号" prop="studentUid">
        <el-input v-model="queryParams.studentUid" placeholder="请输入学号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="手机" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="身份证号" prop="sysid">
        <el-input v-model="queryParams.sysid" placeholder="请输入身份证号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="寄宿" prop="jishu">
        <el-select v-model="queryParams.jishu" placeholder="请选择寄宿" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.STUCMS_JISHU_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="留守儿童" prop="liushou">
        <el-select v-model="queryParams.liushou" placeholder="请选择留守儿童" clearable size="small">
          <el-option v-for="dict in this.getDictDatas(DICT_TYPE.STUCMS_LIUSHOU_TYPE)"
                     :key="dict.value" :label="dict.label" :value="dict.value"/>
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
                   v-hasPermi="['stucms:student:create']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :loading="exportLoading"
                   v-hasPermi="['stucms:student:export']">导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="学号" align="center" prop="studentUid"/>
      <el-table-column label="姓名" align="center" prop="studentName"/>
      <el-table-column label="性别" align="center" prop="sex">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="scope.row.sex"/>
        </template>
      </el-table-column>
      <el-table-column label="身份证号" align="center" prop="sysid" min-width="100"/>
      <el-table-column label="手机" align="center" prop="phone"/>
      <el-table-column label="出生日期" align="center" prop="birth"/>
      <el-table-column label="民族" align="center" prop="minzu"/>
      <el-table-column label="寄宿" align="center" prop="jishu">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.STUCMS_JISHU_TYPE" :value="scope.row.jishu"/>
        </template>
      </el-table-column>
      <el-table-column label="留守儿童" align="center" prop="liushou">
        <template slot-scope="scope">
          <dict-tag :type="DICT_TYPE.STUCMS_LIUSHOU_TYPE" :value="scope.row.liushou"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['stucms:student:update']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['stucms:student:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 对话框(添加 / 修改) -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="studentUid">
              <el-input v-model="form.studentUid" placeholder="请输入学号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="form.studentName" placeholder="请输入姓名"/>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-radio-group v-model="form.sex">
                <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_USER_SEX)"
                          :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机"/>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="sysid">
              <el-input v-model="form.sysid" placeholder="请输入身份证号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birth">
              <el-date-picker clearable
                              v-model="form.birth"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择出生日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="民族" prop="minzu">
              <el-input v-model="form.minzu" placeholder="请输入民族"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="留守儿童" prop="liushou">
              <el-radio-group v-model="form.liushou">
                <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.STUCMS_LIUSHOU_TYPE)"
                          :key="dict.value" :label="dict.value*1">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="寄宿" prop="jishu">
              <el-radio-group v-model="form.jishu">
                <el-radio v-for="dict in this.getDictDatas(DICT_TYPE.STUCMS_JISHU_TYPE)"
                          :key="dict.value" :label="dict.value">{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业学校" prop="biye">
              <el-input v-model="form.biye" placeholder="请输入毕业学校"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="户籍地址" prop="huji">
          <el-input v-model="form.huji" type="textarea" placeholder="请输入内容"/>
        </el-form-item>

        <el-form-item label="现住址" prop="xianzz">
          <el-input v-model="form.xianzz" type="textarea" placeholder="请输入内容"/>
        </el-form-item>


        <el-form-item label="户口" prop="hukou">
          <el-input v-model="form.hukou" type="textarea" placeholder="请输入内容"/>
        </el-form-item>


        <el-form-item label="获奖情况" prop="huojiang">
          <el-input v-model="form.huojiang" type="textarea" placeholder="请输入内容"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  createStudent,
  deleteStudent,
  exportStudentExcel,
  getStudent,
  getStudentPage,
  updateStudent
} from "@/api/stucms/student";

export default {
  name: "Student",
  components: {},
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
      // 学生管理列表
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        studentUid: null,
        studentName: null,
        sex: null,
        phone: null,
        sysid: null,
        jishu: null,
        liushou: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentUid: [{required: true, message: "学号不能为空", trigger: "blur"}],
        studentName: [{required: true, message: "姓名不能为空", trigger: "blur"}],
        sex: [{required: true, message: "性别不能为空", trigger: "blur"}],
        phone: [{required: true, message: "手机不能为空", trigger: "blur"}],
        sysid: [{required: true, message: "身份证号不能为空", trigger: "blur"}],
        birth: [{required: true, message: "出生日期不能为空", trigger: "blur"}],
        minzu: [{required: true, message: "民族不能为空", trigger: "blur"}],
        jishu: [{required: true, message: "寄宿不能为空", trigger: "blur"}],
        liushou: [{required: true, message: "留守儿童不能为空", trigger: "blur"}],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      // 执行查询
      getStudentPage(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        studentUid: undefined,
        studentName: undefined,
        sex: undefined,
        phone: undefined,
        sysid: undefined,
        birth: undefined,
        minzu: undefined,
        hukou: undefined,
        jishu: undefined,
        huji: undefined,
        xianzz: undefined,
        liushou: undefined,
        biye: undefined,
        huojiang: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加学生管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getStudent(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改" + response.data.studentName;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return;
        }
        // 修改的提交
        if (this.form.id != null) {
          updateStudent(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
          return;
        }
        // 添加的提交
        createStudent(this.form).then(response => {
          this.$modal.msgSuccess("新增成功");
          this.open = false;
          this.getList();
        });
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      this.$modal.confirm('是否确认删除学生管理编号为"' + id + '"的数据项?').then(function () {
        return deleteStudent(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      // 处理查询参数
      let params = {...this.queryParams};
      params.pageNo = undefined;
      params.pageSize = undefined;
      this.$modal.confirm('是否确认导出所有学生管理数据项?').then(() => {
        this.exportLoading = true;
        return exportStudentExcel(params);
      }).then(response => {
        this.$download.excel(response, '学生管理.xls');
        this.exportLoading = false;
      }).catch(() => {
      });
    }
  }
};
</script>
