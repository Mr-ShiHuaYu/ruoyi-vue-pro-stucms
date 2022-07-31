<template>
  <!-- 用户导入对话框 -->
  <el-dialog v-dialogDrag :title="title" :visible.sync="visible" width="400px" append-to-body>
    <el-upload
      ref="upload"
      :limit="1"
      accept=".xlsx, .xls"
      :headers="upload.headers"
      :action="uploadUrl + '?updateSupport=' + upload.updateSupport"
      :disabled="upload.isUploading"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
      drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip text-center" slot="tip">
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport"/>
          是否更新已经存在的数据
        </div>
        <span>仅允许导入xls、xlsx格式文件。</span>
        <el-link type="primary" :underline="false"
                 style="font-size:12px;vertical-align: baseline;"
                 @click="importTemplate">下载模板
        </el-link>
      </div>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitFileForm">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {getBaseHeader} from "@/utils/request";

export default {
  name: "ExcelUpload",
  props: {
    title: {
      type: String,
      required: true
    },
    open: {
      type: Boolean,
      required: true
    },
    uploadUrl: {
      type: String,
      required: true
    },
    importRequest: {
      type: Function,
      required: true
    }
  },
  computed: {
    visible: {
      get() {
        return this.open
      },
      set(val) {
        this.$emit("update:open", val)
      }
    }
  },
  data() {
    return {
      // 用户导入参数
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: getBaseHeader(),
      },
    }
  },
  methods: {
    /** 下载模板操作 */
    importTemplate() {
      this.importRequest().then(response => {
        this.$download.excel(response, `${this.title}_${new Date().getTime()}.xlsx`);
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.visible = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
      this.$emit("success")
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
}
</script>

<style scoped>

</style>
