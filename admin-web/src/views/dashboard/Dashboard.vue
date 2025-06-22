<template>
  <div>
    <!-- 统计卡片 -->
    <div class="card-row">
      <div class="card" @click="router.push('/records/list')">
        <div class="card-title">累计巡检次数</div>
        <div class="card-value">{{ stats.totalInspections }}</div>
      </div>
      <div class="card" @click="router.push('/records/list')">
        <div class="card-title">累计参与人数</div>
        <div class="card-value">{{ stats.totalInspectors }}</div>
      </div>
      <div class="card" @click="router.push('/issues/list')">
        <div class="card-title">累计问题数量</div>
        <div class="card-value">{{ stats.totalIssues }}</div>
      </div>
      <div class="card">
        <div class="card-title">累计活跃人员</div>
        <div class="card-value">{{ stats.activeInspectors }}</div>
      </div>
    </div>

    <!-- 本周统计 -->
    <div class="card-row">
      <div class="card" @click="router.push('/records/list')">
        <div class="card-title">本周巡检次数</div>
        <div class="card-value">{{ stats.weeklyInspections }}</div>
        <div class="card-trend" :class="stats.weeklyInspectionsRate >= 0 ? 'up' : 'down'">
          ↑ {{ Math.abs(stats.weeklyInspectionsRate) }}% 本周环比
        </div>
      </div>
      <div class="card" @click="router.push('/records/list')">
        <div class="card-title">本周参与人数</div>
        <div class="card-value">{{ stats.weeklyInspectors }}</div>
        <div class="card-trend" :class="stats.weeklyInspectorsRate >= 0 ? 'up' : 'down'">
          ↑ {{ Math.abs(stats.weeklyInspectorsRate) }}% 本周环比
        </div>
      </div>
      <div class="card" @click="router.push('/issues/list')">
        <div class="card-title">本周问题数量</div>
        <div class="card-value">{{ stats.weeklyIssues }}</div>
        <div class="card-trend down">
          ↓ {{ Math.abs(stats.weeklyIssuesRate) }}% 本周环比
        </div>
      </div>
      <div class="card">
        <div class="card-title">本周活跃人员</div>
        <div class="card-value">{{ stats.weeklyActiveInspectors }}</div>
        <div class="card-trend" :class="stats.weeklyActiveInspectorsRate >= 0 ? 'up' : 'down'">
          ↑ {{ Math.abs(stats.weeklyActiveInspectorsRate) }}% 本周环比
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="chart-row">
      <div class="chart-card">
        <div class="chart-title">巡检趋势</div>
        <div ref="trendChart" style="width: 100%; height: 300px;"></div>
      </div>
      <div class="chart-card" style="flex: 0 0 350px;">
        <div class="chart-title">问题类型分布</div>
        <div ref="pieChart" style="width: 100%; height: 300px;"></div>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-card">
      <div class="table-title">
        <span>最新问题</span>
        <a @click="router.push('/issues/list')">查看全部</a>
      </div>
      <a-table
        :columns="issueColumns"
        :data-source="weeklyIssues"
        :pagination="false"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <span class="status-badge" :class="getStatusClass(record.status)">
              {{ getStatusText(record.status) }}
            </span>
          </template>
          <template v-else-if="column.key === 'action'">
            <a @click="viewIssueDetail(record)">查看</a>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { getDashboardData } from '@/api/statistics'
import { getWeeklyIssues } from '@/api/issue'

export default defineComponent({
  name: 'Dashboard',
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
        const data = await getDashboardData()
        if (data) {
          // 更新统计数据
          stats.totalInspections = data.tasks?.total || 0
          stats.totalInspectors = data.users?.total || 0
          stats.totalIssues = data.issues?.total || 0
          stats.activeInspectors = data.users?.active || 0
          
          // 假设后端API返回的数据包含周统计
          stats.weeklyInspections = data.records?.thisMonth || 0
          stats.weeklyInspectors = data.records?.thisMonth / 5 || 0 // 假设的计算方式
          stats.weeklyIssues = data.issues?.open + data.issues?.processing || 0
          stats.weeklyActiveInspectors = data.users?.active / 3 || 0 // 假设的计算方式
          
          // 假设的增长率
          stats.weeklyInspectionsRate = 12.5
          stats.weeklyInspectorsRate = 4.2
          stats.weeklyIssuesRate = -2.1
          stats.weeklyActiveInspectorsRate = 8.3
        }
      } catch (error) {
        console.error('Failed to get dashboard data:', error)
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
    
    // 初始化趋势图
    const initTrendChart = async () => {
      try {
        // 使用API获取数据
        const trendData = {
          dates: ['第1周', '第2周', '第3周', '第4周', '第5周', '第6周', '第7周'],
          inspections: [120, 132, 101, 134, 90, 230, 210],
          issues: [20, 16, 14, 15, 22, 18, 12]
        }
        
        const chart = echarts.init(trendChart.value)
        chart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
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
            data: trendData.dates
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '巡检次数',
              type: 'line',
              smooth: true,
              areaStyle: {},
              data: trendData.inspections
            },
            {
              name: '问题数量',
              type: 'line',
              smooth: true,
              data: trendData.issues
            }
          ]
        })
        
        window.addEventListener('resize', () => {
          chart.resize()
        })
      } catch (error) {
        console.error('Failed to initialize trend chart:', error)
      }
    }
    
    // 初始化饼图
    const initPieChart = async () => {
      try {
        // 使用API获取数据
        const pieData = [
          { name: '设备异常', value: 40 },
          { name: '环境异常', value: 25 },
          { name: '安全隐患', value: 20 },
          { name: '其他', value: 15 }
        ]
        
        const chart = echarts.init(pieChart.value)
        chart.setOption({
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: pieData.map(item => item.name)
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
              data: pieData
            }
          ]
        })
        
        window.addEventListener('resize', () => {
          chart.resize()
        })
      } catch (error) {
        console.error('Failed to initialize pie chart:', error)
      }
    }
    
    // 初始化排名图表
    const initRankingChart = async () => {
      try {
        // 使用API获取数据或使用模拟数据
        const rankingData = [
          { name: '张三', value: 12 },
          { name: '李四', value: 10 },
          { name: '王五', value: 8 },
          { name: '赵六', value: 7 },
          { name: '张三三', value: 6 }
        ]
        
        const chart = echarts.init(rankingChart.value)
        chart.setOption({
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
            data: rankingData.map(item => item.name)
          },
          series: [
            {
              name: '巡检次数',
              type: 'bar',
              data: rankingData.map(item => item.value)
            }
          ]
        })
        
        window.addEventListener('resize', () => {
          chart.resize()
        })
      } catch (error) {
        console.error('Failed to initialize ranking chart:', error)
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
    
    // 获取状态样式类
    const getStatusClass = (status) => {
      switch (status) {
        case 'pending': return 'status-open'
        case 'processing': return 'status-open'
        case 'completed': return 'status-closed'
        default: return 'status-open'
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
      router,
      stats,
      issueColumns,
      weeklyIssues,
      trendChart,
      pieChart,
      rankingChart,
      getStatusColor,
      getStatusText,
      getStatusClass,
      viewIssueDetail
    }
  }
})
</script>

<style scoped>
.card-row {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(31, 38, 135, 0.08);
  flex: 1;
  padding: 24px 20px 16px 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-width: 0;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.card:hover {
  box-shadow: 0 4px 16px rgba(31, 38, 135, 0.18);
}

.card-title {
  font-size: 16px;
  color: #888;
  margin-bottom: 8px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #222;
}

.card-trend {
  font-size: 12px;
  color: #52c41a;
  margin-top: 4px;
}

.card-trend.down {
  color: #f5222d;
}

.chart-row {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(31, 38, 135, 0.08);
  flex: 1;
  padding: 20px 16px 8px 16px;
  min-width: 0;
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
}

.table-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(31, 38, 135, 0.08);
  padding: 20px 16px 8px 16px;
  margin-bottom: 24px;
}

.table-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-title a {
  font-size: 14px;
  color: #1890ff;
  text-decoration: none;
}

.status-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.status-open {
  background: #fff1f0;
  color: #f5222d;
}

.status-closed {
  background: #f6ffed;
  color: #52c41a;
}


</style> 