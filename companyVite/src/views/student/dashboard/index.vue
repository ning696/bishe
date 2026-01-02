<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 个人信息卡片 -->
      <el-col :xs="24" :lg="8">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="user-info">
            <div class="avatar">
              <el-avatar 
                :size="80" 
                :src="statistics.headImage || userStore.userInfo?.avatar" 
                :icon="UserFilled" 
              />
            </div>
            <div class="user-details">
              <div class="username">{{ userStore.userInfo?.name || userStore.userInfo?.username || '学生' }}</div>
              <div class="user-meta">
                <el-text type="info">用户名：{{ userStore.userInfo?.username || '-' }}</el-text>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 统计数据卡片 -->
      <el-col :xs="24" :lg="16">
        <el-row :gutter="20" v-loading="loading">
          <el-col :xs="12" :sm="8" :md="6" v-for="item in statCards" :key="item.key">
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
      </el-col>
    </el-row>

    <!-- 职位推荐 -->
    <el-row :gutter="20" class="recommend-section">
      <el-col :span="24">
        <el-card class="recommend-card">
          <template #header>
            <div class="card-header recommend-header">
              <div>
                <span>职位推荐</span>
                <el-text type="info" size="small" class="recommend-tip">
                  为你匹配度最高的岗位
                </el-text>
              </div>
              <div class="header-actions">
                <el-button link type="primary" @click="refreshRecommended" :loading="recommendLoading">
                  刷新推荐
                </el-button>
              </div>
            </div>
          </template>
          <div class="recommend-body" v-loading="recommendLoading">
            <template v-if="recommendedJobs.length">
              <div class="job-card" v-for="job in recommendedJobs" :key="job.id">
                <div class="job-main">
                  <div class="job-title">{{ job.jobName }}</div>
                  <div class="job-meta">
                    <span class="company">{{ job.enterpriseName }}</span>
                    <span class="divider">·</span>
                    <span class="location">{{ job.workLocation || '地点待定' }}</span>
                  </div>
                  <div class="salary">{{ formatSalary(job) }}</div>
                </div>
                <div class="job-side">
                  <el-tag type="success" class="match-tag">
                    匹配度 {{ formatMatchScore(job.matchScore) }}
                  </el-tag>
                  <el-button type="primary" text @click="goJobDetail(job.id)">
                    查看详情
                  </el-button>
                </div>
              </div>
            </template>
            <el-empty v-else description="暂无推荐职位">
              <el-button type="primary" @click="refreshRecommended" :loading="recommendLoading">
                重新获取
              </el-button>
            </el-empty>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
// ========== 所有导入语句必须在顶部 ==========
import { ref, computed, onMounted, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { UserFilled, Document, ChatLineRound, Briefcase, Edit } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/modules/user'
import { getOverview } from '@/api/student/statistics'
import { fetchRecommendedJobs, type JobRecommendItem } from '@/api/student/job'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const statistics = ref({
  totalApplications: 0,
  pendingApplications: 0,
  passedApplications: 0,
  totalInterviews: 0,
  scheduledInterviews: 0,
  completedInterviews: 0,
  totalFavorites: 0,
  totalResumes: 0,
  defaultResumeId: null,
  resumeCompleteness: 0,
  headImage: null
})

// 加载状态
const loading = ref(false)
const recommendLoading = ref(false)

const statCards = computed(() => {
  return [
    { key: 'applications', label: '投递数', value: statistics.value.totalApplications, icon: Document, color: '#409eff' },
    { key: 'interviews', label: '面试数', value: statistics.value.totalInterviews, icon: ChatLineRound, color: '#67c23a' },
    { key: 'favorites', label: '收藏数', value: statistics.value.totalFavorites, icon: Briefcase, color: '#e6a23c' },
    { key: 'resumes', label: '简历数', value: statistics.value.totalResumes, icon: Edit, color: '#f56c6c' }
  ]
})

// 获取统计数据
const fetchStatistics = async () => {
  loading.value = true
  try {
    const response = await getOverview()
    if (response.code === 1000 && response.data) {
      statistics.value = response.data
      // 如果接口返回了头像，更新 userStore
      if (response.data.headImage && userStore.userInfo) {
        userStore.setUserInfo({
          ...userStore.userInfo,
          avatar: response.data.headImage
        })
      }
    } else {
      ElMessage.error(response.msg || '获取统计数据失败')
    }
  } catch (error) {
    ElMessage.error('获取统计数据失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const recommendedJobs = ref<JobRecommendItem[]>([])

const fetchRecommended = async () => {
  recommendLoading.value = true
  try {
    const response = await fetchRecommendedJobs()
    if (response.code === 1000 && Array.isArray(response.data)) {
      recommendedJobs.value = response.data
    } else {
      recommendedJobs.value = []
      ElMessage.error(response.msg || '获取推荐职位失败')
    }
  } catch (error) {
    recommendedJobs.value = []
    ElMessage.error('获取推荐职位失败，请稍后重试')
  } finally {
    recommendLoading.value = false
  }
}

const refreshRecommended = () => {
  fetchRecommended()
}

const goJobDetail = (jobId: number) => {
  router.push(`/student/job/${jobId}`)
}

const formatSalary = (job: JobRecommendItem) => {
  const { salaryMin, salaryMax, salaryType } = job
  if (salaryMin == null && salaryMax == null) {
    return '面议'
  }
  if (salaryMin != null && salaryMax != null) {
    if (salaryMin === salaryMax) {
      return `${salaryMin} ${salaryType ?? ''}`.trim()
    }
    return `${salaryMin} - ${salaryMax} ${salaryType ?? ''}`.trim()
  }
  if (salaryMin != null) {
    return `${salaryMin} 起`
  }
  return `${salaryMax} 以下`
}

const formatMatchScore = (score?: number | null) => {
  const numericScore = Number(score ?? 0)
  if (!Number.isFinite(numericScore)) {
    return '0%'
  }
  return `${Math.round(numericScore)}%`
}

// 组件挂载时获取数据（页面首次加载和刷新时都会执行）
onMounted(() => {
  fetchStatistics()
  fetchRecommended()
})

// 如果使用了 keep-alive，页面激活时也刷新数据
onActivated(() => {
  fetchStatistics()
  fetchRecommended()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  .info-card {
    .card-header {
      font-weight: 600;
      font-size: 16px;
    }

    .user-info {
      text-align: center;

      .avatar {
        margin-bottom: 15px;
      }

      .user-details {
        .username {
          font-size: 20px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 10px;
        }

        .user-meta {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }

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
        width: 50px;
        height: 50px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        margin-right: 10px;
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 5px;
        }

        .stat-label {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }

  .recommend-section {
    margin-top: 20px;

    .recommend-card {
      .recommend-header {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .recommend-tip {
          margin-left: 10px;
        }

        .header-actions {
          display: flex;
          align-items: center;
          gap: 10px;
        }
      }

      .recommend-body {
        min-height: 180px;
      }

      .job-card {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px 0;
        border-bottom: 1px solid #f0f2f5;

        &:last-child {
          border-bottom: none;
        }

        .job-main {
          .job-title {
            font-size: 18px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 6px;
          }

          .job-meta {
            color: #606266;
            font-size: 14px;
            margin-bottom: 8px;

            .company {
              font-weight: 500;
            }

            .divider {
              margin: 0 6px;
            }
          }

          .salary {
            font-size: 16px;
            font-weight: 600;
            color: #f56c6c;
          }
        }

        .job-side {
          display: flex;
          flex-direction: column;
          align-items: flex-end;
          gap: 10px;
          min-width: 140px;

          .match-tag {
            font-size: 14px;
          }
        }
      }
    }
  }
}
</style>

