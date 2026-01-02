<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 总览统计卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in overviewCards" :key="item.key">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: item.color }">
              <el-icon :size="24"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <!-- 今日数据卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in todayCards" :key="item.key">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: item.color }">
              <el-icon :size="24"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Briefcase, Document, ChatLineRound, Service, Warning } from '@element-plus/icons-vue'
import { getOverviewa } from '@/api/admin/statistics'

const loading = ref(false)
const overviewData = ref<any>(null)

const overviewCards = computed(() => {
  if (!overviewData.value) return []
  const data = overviewData.value
  return [
    { key: 'totalUsers', label: '总用户数', value: data.totalUsers || 0, icon: User, color: '#409eff' },
    { key: 'totalJobs', label: '总职位数', value: data.totalJobs || 0, icon: Briefcase, color: '#67c23a' },
    { key: 'totalApplications', label: '总投递数', value: data.totalApplications || 0, icon: Document, color: '#e6a23c' },
    { key: 'totalInterviews', label: '总面试数', value: data.totalInterviews || 0, icon: ChatLineRound, color: '#f56c6c' },
    { key: 'totalConsultations', label: '总咨询数', value: data.totalConsultations || 0, icon: Service, color: '#909399' },
    { key: 'totalComplaints', label: '总投诉数', value: data.totalComplaints || 0, icon: Warning, color: '#e6a23c' }
  ]
})

const todayCards = computed(() => {
  if (!overviewData.value) return []
  const data = overviewData.value
  return [
    { key: 'todayActiveUsers', label: '今日活跃用户', value: data.todayActiveUsers || 0, icon: User, color: '#409eff' },
    { key: 'todayNewUsers', label: '今日新增用户', value: data.todayNewUsers || 0, icon: User, color: '#67c23a' },
    { key: 'todayNewJobs', label: '今日新增职位', value: data.todayNewJobs || 0, icon: Briefcase, color: '#e6a23c' },
    { key: 'todayNewApplications', label: '今日新增投递', value: data.todayNewApplications || 0, icon: Document, color: '#f56c6c' }
  ]
})

const fetchData = async () => {
  loading.value = true
  try {
    const response = await getOverviewa()
    if (response.code === 1000 && response.data) {
      overviewData.value = response.data
    } else {
      ElMessage.error(response.msg || '获取数据失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '获取数据失败，请重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  .stat-card {
    margin-bottom: 20px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    .stat-content {
      display: flex;
      align-items: center;

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        margin-right: 15px;
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 28px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 5px;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
}
</style>

