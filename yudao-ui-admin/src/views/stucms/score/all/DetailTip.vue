<template>
  <el-popover
    v-if="row[column.property]>0"
    placement="right-start"
    popper-class="table-score-all show-tip"
    trigger="click">
    <div v-html="row.content" v-if="row.content"></div>
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
      // loading: false
    }
  },
  methods: {
    handleShowDetail(row, column) {
      row.content = ""
      const formData = {
        cid: row.cid, eid: row.eid, field: column.property
      }
      // console.log(formData);
      showTip(formData).then(response => {
          let tip = '';
          response.data.forEach(function (item) {
            tip += item.name + ':' + item.score + '<br>';
          });
          row.content = tip
        }
      ).catch(() => {
      })

    },
  }
}
</script>

<style scoped>

</style>
