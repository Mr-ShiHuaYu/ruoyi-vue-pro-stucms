<!--自定义的组件,辅助说明文字用-->
<template>
  <blockquote class="elem-quote">
    <div v-html="text"></div>
  </blockquote>
</template>

<script>
export default {
  name: "ElemQuote",
  props:{
    text: {
      type: String,
      required: true,
    }
  }
}
</script>

<style scoped>
.elem-quote {
  color: #606266;
  margin: 0 0 10px;
  padding: 10px;
  line-height: 22px;
  border-left: 5px solid #1890ff;
  border-radius: 0 2px 2px 0;
  background-color: #f0f0f0;
  font-size: 13px;
}
</style>
