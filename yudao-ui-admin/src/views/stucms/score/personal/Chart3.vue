<template>
  <el-card class="box-card" v-loading="loading">
    <div slot="header">
      总分折线图<span style="color: red;">{{ sName }}</span>
    </div>
    <div ref="chart3" :style="{height:height,width:width}"/>
  </el-card>

</template>

<script>
import echarts from 'echarts'

require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'
import {getPersonalScoreChart3} from "@/api/stucms/score/personal";

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
      studentName: "",
      options: {
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            const d = params[0];
            return params[0].axisValueLabel + '<br>总分:' + d.data.value + "<br>排名:" + d.data.rank
          }
        },
        legend: {
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
        yAxis: {type: 'value'},
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
      this.chart = echarts.init(this.$refs.chart3, 'macarons')
      this.setOptions(this.studentId)
    },
    setOptions(studentId) {
      if (!studentId) return
      this.loading = true
      getPersonalScoreChart3(studentId).then(response => {
        let data = response.data;
        // 设置学生姓名
        this.studentName = data.studentName
        // 设置图例,就是课程名称,语文,数学等
        this.options.legend.data = this.courses;
        // 设置图横坐标
        this.options.xAxis.data = this.exams;
        // 设置数据
        this.options.series = data.series;
        this.chart.setOption(this.options)
        this.loading = false
      })
          .catch(() => {})
    }
  }
}
</script>
