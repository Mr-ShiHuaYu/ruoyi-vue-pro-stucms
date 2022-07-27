<template>
  <el-dialog
    v-dialogDrag
    :loading="loading"
    :title="form.name+'老师信息'"
    :visible.sync="dialogVisible"
    id="showTeacher"
    width="30%">

    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="老师姓名">
        <el-input v-model="form.name" disabled></el-input>
      </el-form-item>

      <el-form-item label="电话">
        <el-input type="text" v-model="form.phone" disabled></el-input>
      </el-form-item>

      <el-form-item label="性别">
        <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="form.sex"/>
      </el-form-item>

      <el-form-item label="QQ">
        <el-input type="text" v-model="form.qq" disabled></el-input>
      </el-form-item>

    </el-form>
  </el-dialog>
</template>

<script>
import { getTeacher } from '@/api/stucms/teacher'

export default {
  name: 'showTeacher',
  props: {
    // 角色编号
    teacherId: {
      type: [Number, String]
    }
  },
  data() {
    return {
      loading: false,
      dialogVisible: false,
      form: {}
    }
  },
  methods: {
    show() {
      this.form = {}
      this.getTeacherById()
    },
    getTeacherById() {
      this.loading = true
      this.$nextTick(() => {
        getTeacher(this.teacherId).then(response => {
          this.form = response.data
          this.loading = false
          this.dialogVisible = true
        }).catch(err => this.$modal.alertError(err))
      })
    }
  }
}
</script>

<style scoped lang="scss">
/*
::v-deep 当父组件想要改子组件中的样式时,需要加上,这里,showTeacher组件为父组件,也就是当前为父组件,想要改el-input组件中的样式
因为使用了scss ,所以只能使用 ::v-deep
需要加上scoped,这样样式不会污染到其他的组件
会生成这样的样式
[data-v-b583647e] .el-input.is-disabled .el-input__inner {
    background-color: #ffffff;
    color: #606266;
}
*/
::v-deep .el-input.is-disabled .el-input__inner {
  background-color: #ffffff;
  color: #606266;
}
</style>
