<template>
  <div>
    <!-- 统计卡片 -->
    <div class="stat-cards">
      <a-row :gutter="16">
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">累计巡检次数</div>
            <div class="stat-card-value">{{ stats.totalInspections }}</div>
            <div class="stat-card-desc">总巡检次数</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">累计参与人数</div>
            <div class="stat-card-value">{{ stats.totalInspectors }}</div>
            <div class="stat-card-desc">总参与人员</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">累计问题数量</div>
            <div class="stat-card-value">{{ stats.totalIssues }}</div>
            <div class="stat-card-desc">总问题数量</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">活跃人员</div>
            <div class="stat-card-value">{{ stats.activeInspectors }}</div>
            <div class="stat-card-desc">本月活跃</div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 本周统计 -->
    <div class="stat-cards">
      <a-row :gutter="16">
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">本周巡检次数</div>
            <div class="stat-card-value">{{ stats.weeklyInspections }}</div>
            <div class="stat-card-trend" :class="stats.weeklyInspectionsRate >= 0 ? 'up' : 'down'">
              <arrow-up-outlined v-if="stats.weeklyInspectionsRate >= 0" />
              <arrow-down-outlined v-else />
              {{ Math.abs(stats.weeklyInspectionsRate) }}%
            </div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">本周参与人数</div>
            <div class="stat-card-value">{{ stats.weeklyInspectors }}</div>
            <div class="stat-card-trend" :class="stats.weeklyInspectorsRate >= 0 ? 'up' : 'down'">
              <arrow-up-outlined v-if="stats.weeklyInspectorsRate >= 0" />
              <arrow-down-outlined v-else />
              {{ Math.abs(stats.weeklyInspectorsRate) }}%
            </div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">本周问题数量</div>
            <div class="stat-card-value">{{ stats.weeklyIssues }}</div>
            <div class="stat-card-trend" :class="stats.weeklyIssuesRate >= 0 ? 'up' : 'down'">
              <arrow-up-outlined v-if="stats.weeklyIssuesRate >= 0" />
              <arrow-down-outlined v-else />
              {{ Math.abs(stats.weeklyIssuesRate) }}%
            </div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="stat-card">
            <div class="stat-card-title">本周活跃人员</div>
            <div class="stat-card-value">{{ stats.weeklyActiveInspectors }}</div>
            <div class="stat-card-trend" :class="stats.weeklyActiveInspectorsRate >= 0 ? 'up' : 'down'">
              <arrow-up-outlined v-if="stats.weeklyActiveInspectorsRate >= 0" />
              <arrow-down-outlined v-else />
              {{ Math.abs(stats.weeklyActiveInspectorsRate) }}%
            </div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 图表区域 -->
    <a-row :gutter="16">
      <a-col :span="16">
        <div class="card">
          <div class="card-title">巡检趋势</div>
          <div ref="trendChart" style="width: 100%; height: 300px;"></div>
        </div>
      </a-col>
      <a-col :span="8">
        <div class="card">
          <div class="card-title">问题类型分布</div>
          <div ref="pieChart" style="width: 100%; height: 300px;"></div>
        </div>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px;">
      <a-col :span="12">
        <div class="card chart-card">
          <div class="card-title">巡检人员排名</div>
          <div ref="rankingChart" style="width: 100%; height: 300px;"></div>
        </div>
      </a-col>
      <a-col :span="12">
        <div class="card chart-card">
          <div class="card-title">本周问题</div>
          <div style="height: 300px; overflow-y: auto;">
            <a-table
              :columns="issueColumns"
              :data-source="weeklyIssues"
              :pagination="false"
              size="small"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'status'">
                  <a-tag :color="getStatusColor(record.status)">
                    {{ getStatusText(record.status) }}
                  </a-tag>
                </template>
                <template v-else-if="column.key === 'action'">
                  <a @click="viewIssueDetail(record)">查看</a>
                </template>
              </template>
            </a-table>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowUpOutlined, ArrowDownOutlined } from '@ant-design/icons-vue'
import * as echarts from 'echarts'
import { getRecordStats, getWeeklyTrend, getIssueDistribution, getInspectorRanking } from '@/api/record'
import { getWeeklyIssues } from '@/api/issue'

export default defineComponent({
  name: 'Dashboard',
  components: {
    ArrowUpOutlined,
    ArrowDownOutlined
  },
  setup() {
    const router = useRouter()
    const trendChart = ref(null)
    const pieChart = ref(null)
    const rankingChart = ref(null)
    
    // 统计数据
    const stats = reactive({
      totalInspections: 0,
      totalInspectors: 0,
      totalIssues: 0,
      activeInspectors: 0,
      weeklyInspections: 0,
      weeklyInspectors: 0,
      weeklyIssues: 0,
      weeklyActiveInspectors: 0,
      weeklyInspectionsRate: 0,
      weeklyInspectorsRate: 0,
      weeklyIssuesRate: 0,
      weeklyActiveInspectorsRate: 0
    })
    
    // 问题表格列定义
    const issueColumns = [
      { title: '问题描述', dataIndex: 'description', key: 'description', ellipsis: true },
      { title: '类型', dataIndex: 'type', key: 'type' },
      { title: '状态', dataIndex: 'status', key: 'status' },
      { title: '操作', key: 'action', width: 80 }
    ]
    
    // 本周问题列表
    const weeklyIssues = ref([])
    
    // 获取统计数据
    const getStats = async () => {
      try {
        const data = await getRecordStats()
        Object.assign(stats, data)
      } catch (error) {
        console.error('Failed to get stats:', error)
      }
    }
    
    // 获取本周问题
    const getWeeklyIssueList = async () => {
      try {
        const data = await getWeeklyIssues()
        weeklyIssues.value = data
      } catch (error) {
        console.error('Failed to get weekly issues:', error)
      }
    }
    
    // 初始化巡检趋势图
    const initTrendChart = async () => {
      try {
        const data = await getWeeklyTrend()
        const chart = echarts.init(trendChart.value)
        
        const option = {
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['巡检次数', '问题数量']
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
              name: '巡检次数',
              type: 'line',
              data: data.inspections,
              smooth: true,
              lineStyle: {
                color: '#1890ff'
              },
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: 'rgba(24, 144, 255, 0.3)'
                    },
                    {
                      offset: 1,
                      color: 'rgba(24, 144, 255, 0.1)'
                    }
                  ]
                }
              }
            },
            {
              name: '问题数量',
              type: 'line',
              data: data.issues,
              smooth: true,
              lineStyle: {
                color: '#ff4d4f'
              },
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [
                    {
                      offset: 0,
                      color: 'rgba(255, 77, 79, 0.3)'
                    },
                    {
                      offset: 1,
                      color: 'rgba(255, 77, 79, 0.1)'
                    }
                  ]
                }
              }
            }
          ]
        }
        
        chart.setOption(option)
        window.addEventListener('resize', () => {
          chart.resize()
        })
      } catch (error) {
        console.error('Failed to init trend chart:', error)
      }
    }
    
    // 初始化问题分布饼图
    const initPieChart = async () => {
      try {
        const data = await getIssueDistribution()
        const chart = echarts.init(pieChart.value)
        
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '问题类型',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: data
            }
          ]
        }
        
        chart.setOption(option)
        window.addEventListener('resize', () => {
          chart.resize()
        })
      } catch (error) {
        console.error('Failed to init pie chart:', error)
      }
    }
    
    // 初始化巡检人员排名图
    const initRankingChart = async () => {
      try {
        const data = await getInspectorRanking()
        const chart = echarts.init(rankingChart.value)
        
        const option = {
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
            type: 'value',
            boundaryGap: [0, 0.01]
          },
          yAxis: {
            type: 'category',
            data: data.map(item => item.name),
            inverse: true
          },
          series: [
            {
              name: '巡检次数',
              type: 'bar',
              data: data.map(item => item.count),
              itemStyle: {
                color: function(params) {
                  const colorList = ['#1890ff', '#52c41a', '#faad14', '#13c2c2', '#722ed1', '#eb2f96']
                  return colorList[params.dataIndex % colorList.length]
                }
              }
            }
          ]
        }
        
        chart.setOption(option)
        window.addEventListener('resize', () => {
          chart.resize()
        })
      } catch (error) {
        console.error('Failed to init ranking chart:', error)
      }
    }
    
    // 获取状态颜色
    const getStatusColor = (status) => {
      switch (status) {
        case 'pending': return 'orange'
        case 'processing': return 'blue'
        case 'completed': return 'green'
        default: return 'default'
      }
    }
    
    // 获取状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 'pending': return '待处理'
        case 'processing': return '处理中'
        case 'completed': return '已完成'
        default: return status
      }
    }
    
    // 查看问题详情
    const viewIssueDetail = (record) => {
      router.push({
        path: '/issues',
        query: { id: record.id }
      })
    }
    
    onMounted(() => {
      getStats()
      getWeeklyIssueList()
      
      // 使用setTimeout确保DOM已经渲染
      setTimeout(() => {
        initTrendChart()
        initPieChart()
        initRankingChart()
      }, 100)
    })
    
    return {
      stats,
      issueColumns,
      weeklyIssues,
      trendChart,
      pieChart,
      rankingChart,
      getStatusColor,
      getStatusText,
      viewIssueDetail
    }
  }
})
</script>

<style scoped>
.stat-cards {
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(31, 38, 135, 0.08);
  padding: 16px;
  height: 100%;
}

.stat-card-title {
  color: #8c8c8c;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-card-value {
  font-size: 24px;
  font-weight: bold;
  color: #262626;
  margin-bottom: 8px;
}

.stat-card-desc {
  color: #8c8c8c;
  font-size: 12px;
}

.stat-card-trend {
  display: inline-flex;
  align-items: center;
  font-size: 12px;
  margin-left: 8px;
}

.stat-card-trend.up {
  color: #52c41a;
}

.stat-card-trend.down {
  color: #ff4d4f;
}

.chart-card {
  display: flex;
  flex-direction: column;
  height: 380px;
}

.chart-card .card-title {
  margin-bottom: 16px;
}
</style> 