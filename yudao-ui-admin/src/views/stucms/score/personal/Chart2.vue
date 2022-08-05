<template>
  <el-card class="box-card" v-loading="loading">
    <div slot="header">
      各科排名折线图<span style="color: red;">{{ sName }}</span>
    </div>
    <div ref="chart2" :style="{height:height,width:width}"/>
  </el-card>

</template>

<script>
import echarts from 'echarts'
import resize from '@/views/dashboard/mixins/resize'
import {getPersonalScoreChart2} from "@/api/stucms/score/personal";

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
    studentId: {
      type: Number,
      required: true
    },
    studentName: {
      type: String,
      required: true
    },
    exams: {
      type: Array,
      required: true
    },
    courses: {
      type: Array,
      required: true
    },
  },
  data() {
    return {
      loading: false,
      chart: null,
      options: {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          // 只选择一门课程,如,点击语文
          selectedMode: 'single',
          data: []
        },
        grid: {
          left: '6%',
          right: '2%',
          bottom: '0%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          axisLabel: {
            interval: 0,
            rotate: 50,
          },
          boundaryGap: false,
          data: []
        },
        yAxis: {type: 'value', inverse: true, min: 1, minInterval: 1},
        series: []
      }
    }
  },
  watch: {
    studentId: {
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  computed: {
    sName() {
      if (this.studentName) {
        return `(${this.studentName})`
      } else {
        return ""
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
      this.chart = echarts.init(this.$refs.chart2, 'macarons')
      this.setOptions(this.studentId)
    },
    setOptions(studentId) {
      if (!studentId) return
      this.loading = true
      getPersonalScoreChart2(studentId).then(response => {
        // 设置图例,就是课程名称,语文,数学等
        this.options.legend.data = this.courses;
        // 设置图横坐标
        this.options.xAxis.data = this.exams;
        // 设置数据
        this.options.series = response.data;
        this.chart.setOption(this.options)
        this.loading = false
      })
          .catch((err) => {
            console.log(err);
          })
    }
  }
}
</script>
