<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>学生管理系统</h2>
        <p>
          本项目是使用ruoyi-vue-pro开源框架进行二次开发
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="danger">&yen;免费开源</el-tag>
        </p>
        <p>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTarget('https://gitee.com/vip_huage/ruoyi-vue-pro-stucms')"
          >访问码云
          </el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h3>后端技术</h3>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis Plus</li>
              <li>Druid</li>
              <li>redisson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h3>前端技术</h3>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider/>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card shadow="always">
          <el-button
            type="warning"
            icon="el-icon-search"
            plain
            @click="openSearchScoreTab"
            class="my-card-button"
          >成绩查询
          </el-button
          >
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card shadow="always">
          <el-button
            type="success"
            icon="el-icon-monitor"
            plain
            @click="openPersonalTab"
            class="my-card-button"
          >个人分析
          </el-button
          >
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card shadow="always">
          <el-button
            type="primary"
            icon="el-icon-odometer"
            plain
            @click="openAllScoreTab"
            class="my-card-button"
          >总体分析
          </el-button
          >
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-user-solid"></i> QQ群：
              <a href="https://jq.qq.com/?_wv=1027&k=fe7fkVHZ" target="_blank"
              > 316915910</a
              >
            </p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="v0.1.0 - 2022-08-07">
              <ol>
                <li>学生管理系统前后端分离系统正式发布</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>捐赠支持</span>
          </div>
          <div class="body" style="text-align: center;">
            <img
              src="https://hua-imgs.oss-cn-hangzhou.aliyuncs.com/img/mm_facetoface.png"
              alt="donate"
              height="100%"
            />
            <span style="display: inline-block; height: 30px; line-height: 30px"
            >你可以请作者喝杯咖啡表示鼓励</span
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "0.1.0",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    openSearchScoreTab() {
      this.$tab.openPage("成绩查询", "/stucms/score/search")
    },
    openPersonalTab() {
      this.$tab.openPage("个人分析", "/stucms/score/personal")
    },
    openAllScoreTab(){
      this.$tab.openPage("总体分析", "/stucms/score/all")
    }
  },
};
</script>

<style scoped lang="scss">
.my-card-button {
  width: 100%;
  height: 100px;
  font-size: 34px;
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

