<template>
  <div :style="{height:height,width:width}"/>
</template>

<script>
import echarts from 'echarts'
import resize from '@/views/dashboard/mixins/resize'
import {showCoursePie} from "@/api/stucms/score/all";

require('echarts/theme/macarons') // echarts theme

export default {
  mixins: [resize],
  props: {
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    row: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      chart: null,
      options: {
        tooltip: {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)"
        },
        legend: {
          orient: 'vertical',
          left: 'right',
          data: ['优秀', '良好', '及格', '不及格']
        },
        series: {
          type: "pie",
          radius: "55%",
          center: ["45%", "50%"],
          data: [],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)"
            }
          }
        }
      }
    }
  },
  watch: {
    "row.cid": {
      deep: true,
      immediate: true,
      handler(val) {
        this.setOptions()
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
    },
    setOptions() {
      this.loading = true
      const formData = {
        cid: this.row.cid, eid: this.row.eid
      }
      showCoursePie(formData).then(response => {
        this.options.series.data = response.data;
        this.chart.setOption(this.options)
        this.loading = false
      }).catch(() => {
      })
    }
  }
}
</script>
