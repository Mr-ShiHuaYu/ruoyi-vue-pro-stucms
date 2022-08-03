<template>
  <el-popover
    v-if="row[column.property]>0"
    placement="right-start"
    :popper-class="'table-score-all show-tip '+type"
    trigger="click">
    <div v-html="row.content" v-if="show"></div>
    <el-tag
      slot="reference"
      @click="handleShowDetail(row, column)"
      size="mini"
      :type="type"
      effect="dark">
      {{ row[column.property] }}
    </el-tag>
  </el-popover>
  <span v-else>{{ row[column.property] }}</span>
</template>

<script>
import {showTip} from "@/api/stucms/score/all";

export default {
  name: "DetailTip",
  props: {
    row: {
      type: Object,
      required: true
    },
    column: {
      // 每一列的数据,其中部分数据如下,property为每一列的字段名
      /*
      * label: "优秀"
        property: "youxiu"
      * */
      type: Object,
      required: true
    },
    type: {
      type: String,
      required: false,
      default: "—"
    }
  },
  data() {
    return {
      show: false
    }
  },
  methods: {
    handleShowDetail(row, column) {
      this.show = false
      // property为点击这一列的字段名,如 youxiu
      const formData = {
        cid: row.cid, eid: row.eid, field: column.property
      }
      showTip(formData).then(response => {
          let tip = '';
          response.data.forEach(function (item) {
            tip += item.name + ':' + item.score + '<br>';
          });
          row.content = tip
          this.show = true
        }
      ).catch(() => {
      })
    },
  }
}
</script>

<style scoped>


</style>
