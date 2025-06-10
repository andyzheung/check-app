<template>
  <div>
    <h2>问题统计</h2>
    
    <a-card class="card-container">
      <a-row :gutter="16">
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-title">问题总数</div>
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-footer">总问题数量</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-title">未解决问题</div>
            <div class="stat-value">{{ stats.open }}</div>
            <div class="stat-footer">等待处理的问题</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-title">处理中问题</div>
            <div class="stat-value">{{ stats.processing }}</div>
            <div class="stat-footer">正在处理的问题</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-title">已解决问题</div>
            <div class="stat-value">{{ stats.closed }}</div>
            <div class="stat-footer">已解决的问题</div>
          </div>
        </a-col>
      </a-row>
    </a-card>
    
    <a-row :gutter="16" style="margin-top: 16px;">
      <a-col :span="12">
        <a-card title="问题类型分布">
          <div ref="typeChart" style="height: 300px;"></div>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="问题状态分布">
          <div ref="statusChart" style="height: 300px;"></div>
        </a-card>
      </a-col>
    </a-row>
    
    <a-row :gutter="16" style="margin-top: 16px;">
      <a-col :span="24">
        <a-card title="问题趋势">
          <div class="chart-toolbar">
            <a-radio-group v-model:value="trendTimeRange" @change="fetchTrendData">
              <a-radio-button value="week">本周</a-radio-button>
              <a-radio-button value="month">本月</a-radio-button>
              <a-radio-button value="quarter">本季度</a-radio-button>
              <a-radio-button value="year">本年</a-radio-button>
            </a-radio-group>
          </div>
          <div ref="trendChart" style="height: 300px;"></div>
        </a-card>
      </a-col>
    </a-row>
    
    <a-row :gutter="16" style="margin-top: 16px;">
      <a-col :span="12">
        <a-card title="问题区域分布">
          <div ref="areaChart" style="height: 300px;"></div>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card title="问题处理人员排名">
          <div ref="handlerChart" style="height: 300px;"></div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getIssueStatistics, getIssueTrend, getIssueByArea, getIssueByHandler } from '@/api/issue'

export default defineComponent({
  name: 'IssueStatistics',
  setup() {
    const typeChart = ref(null)
    const statusChart = ref(null)
    const trendChart = ref(null)
    const areaChart = ref(null)
    const handlerChart = ref(null)
    
    const trendTimeRange = ref('month')
    
    const stats = reactive({
      total: 0,
      open: 0,
      processing: 0,
      closed: 0
    })
    
    const typeChartInstance = ref(null)
    const statusChartInstance = ref(null)
    const trendChartInstance = ref(null)
    const areaChartInstance = ref(null)
    const handlerChartInstance = ref(null)
    
    // 获取统计数据
    const fetchStatistics = async () => {
      try {
        const data = await getIssueStatistics()
        stats.total = data.total || 0
        stats.open = data.open || 0
        stats.processing = data.processing || 0
        stats.closed = data.closed || 0
        
        // 初始化图表
        initTypeChart(data.byType || [])
        initStatusChart([
          { name: '未解决', value: stats.open },
          { name: '处理中', value: stats.processing },
          { name: '已解决', value: stats.closed }
        ])
      } catch (error) {
        console.error('Failed to get issue statistics:', error)
      }
    }
    
    // 获取趋势数据
    const fetchTrendData = async () => {
      try {
        const data = await getIssueTrend(trendTimeRange.value)
        initTrendChart(data)
      } catch (error) {
        console.error('Failed to get issue trend:', error)
      }
    }
    
    // 获取区域分布数据
    const fetchAreaData = async () => {
      try {
        const data = await getIssueByArea()
        initAreaChart(data)
      } catch (error) {
        console.error('Failed to get area distribution:', error)
      }
    }
    
    // 获取处理人员排名数据
    const fetchHandlerData = async () => {
      try {
        const data = await getIssueByHandler()
        initHandlerChart(data)
      } catch (error) {
        console.error('Failed to get handler ranking:', error)
      }
    }
    
    // 初始化类型分布图表
    const initTypeChart = (data) => {
      if (!typeChart.value) return
      
      typeChartInstance.value = echarts.init(typeChart.value)
      typeChartInstance.value.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '问题类型',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '15',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
          }
        ]
      })
      
      window.addEventListener('resize', () => {
        typeChartInstance.value && typeChartInstance.value.resize()
      })
    }
    
    // 初始化状态分布图表
    const initStatusChart = (data) => {
      if (!statusChart.value) return
      
      statusChartInstance.value = echarts.init(statusChart.value)
      statusChartInstance.value.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '问题状态',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '15',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data.map(item => ({
              ...item,
              itemStyle: {
                color: item.name === '未解决' ? '#ff4d4f' : 
                       item.name === '处理中' ? '#1890ff' : '#52c41a'
              }
            }))
          }
        ]
      })
      
      window.addEventListener('resize', () => {
        statusChartInstance.value && statusChartInstance.value.resize()
      })
    }
    
    // 初始化趋势图表
    const initTrendChart = (data) => {
      if (!trendChart.value) return
      
      trendChartInstance.value = echarts.init(trendChart.value)
      trendChartInstance.value.setOption({
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增问题', '解决问题']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: data.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '新增问题',
            type: 'line',
            data: data.created,
            smooth: true
          },
          {
            name: '解决问题',
            type: 'line',
            data: data.solved,
            smooth: true
          }
        ]
      })
      
      window.addEventListener('resize', () => {
        trendChartInstance.value && trendChartInstance.value.resize()
      })
    }
    
    // 初始化区域分布图表
    const initAreaChart = (data) => {
      if (!areaChart.value) return
      
      areaChartInstance.value = echarts.init(areaChart.value)
      areaChartInstance.value.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 'center',
          data: data.map(item => item.name)
        },
        series: [
          {
            name: '区域分布',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
      
      window.addEventListener('resize', () => {
        areaChartInstance.value && areaChartInstance.value.resize()
      })
    }
    
    // 初始化处理人员排名图表
    const initHandlerChart = (data) => {
      if (!handlerChart.value) return
      
      handlerChartInstance.value = echarts.init(handlerChart.value)
      handlerChartInstance.value.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.map(item => item.name),
          inverse: true
        },
        series: [
          {
            name: '处理问题数',
            type: 'bar',
            data: data.map(item => item.value)
          }
        ]
      })
      
      window.addEventListener('resize', () => {
        handlerChartInstance.value && handlerChartInstance.value.resize()
      })
    }
    
    onMounted(() => {
      fetchStatistics()
      fetchTrendData()
      fetchAreaData()
      fetchHandlerData()
    })
    
    return {
      stats,
      typeChart,
      statusChart,
      trendChart,
      areaChart,
      handlerChart,
      trendTimeRange,
      fetchTrendData
    }
  }
})
</script>

<style scoped>
.card-container {
  margin-bottom: 16px;
}

.stat-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}

.stat-title {
  font-size: 14px;
  color: #8c8c8c;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 8px;
}

.stat-footer {
  font-size: 12px;
  color: #8c8c8c;
}

.chart-toolbar {
  margin-bottom: 16px;
  text-align: right;
}
</style> 