<template>
  <div class="dashboard-container">
    <div class="toolbar">
      <div class="spacer"></div>
    </div>
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in statCards" :key="item.key">
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

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card class="talent-card">
          <template #header>
            <div class="card-header talent-header">
              <div>
                <span>职位人才推荐</span>
                <el-text type="info" size="small" class="talent-subtitle">基于职位画像实时匹配候选人</el-text>
              </div>
              <div class="talent-actions">
                <el-select
                  v-model="selectedJobId"
                  placeholder="请选择职位"
                  filterable
                  clearable
                  :loading="jobLoading"
                  class="job-selector"
                >
                  <el-option
                    v-for="job in jobOptions"
                    :key="job.id"
                    :label="job.jobName"
                    :value="job.id"
                  />
                </el-select>
                <el-button
                  type="primary"
                  link
                  :disabled="!selectedJobId"
                  :loading="talentLoading"
                  @click="refreshRecommendations"
                >
                  刷新推荐
                </el-button>
                <el-button type="primary" @click="goJobManage">职位管理</el-button>
              </div>
            </div>
          </template>
          <div class="talent-body" v-loading="talentLoading">
            <template v-if="jobOptions.length === 0 && !jobLoading">
              <el-empty description="暂无在招职位">
                <el-button type="primary" @click="goJobManage">去发布职位</el-button>
              </el-empty>
            </template>
            <template v-else-if="selectedJobId && recommendedTalent.length">
              <div class="talent-item" v-for="talent in recommendedTalent" :key="talent.id">
                <div class="talent-info">
                  <div class="talent-name">
                    <span>{{ talent.realName || talent.nickName || '匿名候选人' }}</span>
                    <el-tag type="success" size="small" v-if="talent.matchScore != null">
                      匹配度 {{ talent.matchScore }}%
                    </el-tag>
                  </div>
                  <div class="talent-meta">
                    <span v-if="talent.major">专业：{{ talent.major }}</span>
                    <span v-if="talent.education">学历：{{ talent.education }}</span>
                    <span v-if="talent.expectedLocation">期望地点：{{ talent.expectedLocation }}</span>
                  </div>
                  <div class="talent-skills" v-if="talent.skills">
                    技能：{{ talent.skills }}
                  </div>
                </div>
                <div class="talent-side">
                  <div class="talent-update" v-if="talent.updateTime">
                    最近活跃：{{ formatDate(talent.updateTime) }}
                  </div>
                  <div class="talent-actions-inline">
                    <el-button type="primary" text @click="viewResume(talent.id)">查看简历</el-button>
                  </div>
                </div>
              </div>
            </template>
            <template v-else-if="selectedJobId && !talentLoading">
              <el-empty description="暂未匹配到优秀人才">
                <el-button type="primary" @click="refreshRecommendations">重新计算</el-button>
              </el-empty>
            </template>
            <template v-else>
              <el-empty description="请选择一个职位以查看推荐" />
            </template>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Briefcase, View, Document, ChatLineRound, User } from '@element-plus/icons-vue'
import { getAnalysis } from '@/api/enterprise/statistics'
import { fetchEnterpriseJobList, type EnterpriseJobListItem } from '@/api/enterprise/job'
import { fetchTalentRecommendations, type TalentRecommendItem } from '@/api/enterprise/talent'
import { useRouter } from 'vue-router'

const loading = ref(false)
const analysisData = ref<any>(null)
const router = useRouter()

const statCards = computed(() => {
  if (!analysisData.value) return []
  const data = analysisData.value
  return [
    { key: 'totalJobs', label: '总职位数', value: data.totalJobs || 0, icon: Briefcase, color: '#409eff' },
    { key: 'totalViews', label: '总浏览量', value: data.totalViews || 0, icon: View, color: '#67c23a' },
    { key: 'totalApplications', label: '总投递数', value: data.totalApplications || 0, icon: Document, color: '#e6a23c' },
    { key: 'totalInterviews', label: '总面试数', value: data.totalInterviews || 0, icon: ChatLineRound, color: '#f56c6c' },
    { key: 'totalHires', label: '总录用数', value: data.totalHires || 0, icon: User, color: '#909399' },
    { key: 'conversionRate', label: '转化率', value: data.conversionRate ? `${(data.conversionRate * 100).toFixed(2)}%` : '0%', icon: Document, color: '#e6a23c' }
  ]
})

const jobOptions = ref<EnterpriseJobListItem[]>([])
const jobLoading = ref(false)
const selectedJobId = ref<number | null>(null)
const recommendedTalent = ref<TalentRecommendItem[]>([])
const talentLoading = ref(false)

const fetchData = async () => {
  loading.value = true
  try {
    const response = await getAnalysis()
    if (response.code === 1000 && response.data) {
      analysisData.value = response.data
    } else {
      ElMessage.error(response.msg || '获取数据失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '获取数据失败，请重试')
  } finally {
    loading.value = false
  }
}

const loadJobOptions = async () => {
  jobLoading.value = true
  try {
    const res = await fetchEnterpriseJobList({ pageNum: 1, pageSize: 100, status: 1 })
    if (res.code === 1000 && res.data) {
      jobOptions.value = res.data.rows || []
      if (!selectedJobId.value && jobOptions.value.length) {
        selectedJobId.value = jobOptions.value[0].id
      }
      if (!jobOptions.value.length) {
        selectedJobId.value = null
      }
    } else {
      jobOptions.value = []
      selectedJobId.value = null
      ElMessage.error(res.msg || '获取职位列表失败')
    }
  } catch (error: any) {
    jobOptions.value = []
    selectedJobId.value = null
    ElMessage.error(error.message || '获取职位列表失败，请重试')
  } finally {
    jobLoading.value = false
  }
}

const fetchTalent = async () => {
  if (!selectedJobId.value) {
    recommendedTalent.value = []
    return
  }
  talentLoading.value = true
  try {
    const res = await fetchTalentRecommendations({ jobId: selectedJobId.value, limit: 6 })
    if (res.code === 1000) {
      recommendedTalent.value = res.data || []
    } else {
      recommendedTalent.value = []
      ElMessage.error(res.msg || '获取人才推荐失败')
    }
  } catch (error: any) {
    recommendedTalent.value = []
    ElMessage.error(error.message || '获取人才推荐失败，请稍后重试')
  } finally {
    talentLoading.value = false
  }
}

const refreshRecommendations = () => {
  fetchTalent()
}

const goJobManage = () => {
  router.push('/enterprise/job/list')
}

const viewResume = (studentId: number) => {
  router.push({ path: '/enterprise/resume/list', query: { studentId } })
}

const formatDate = (value: string) => {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }
  return date.toLocaleString()
}

watch(selectedJobId, (newVal, oldVal) => {
  if (newVal && newVal !== oldVal) {
    fetchTalent()
  }
})

onMounted(() => {
  fetchData()
  loadJobOptions()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  .toolbar {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    .spacer {
      flex: 1;
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

  .card-header {
    font-weight: 600;
    font-size: 16px;
  }

  .talent-card {
    .talent-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .talent-subtitle {
        margin-left: 12px;
      }

      .talent-actions {
        display: flex;
        align-items: center;
        gap: 12px;

        .job-selector {
          width: 260px;
        }
      }
    }

    .talent-body {
      min-height: 220px;
    }

    .talent-item {
      display: flex;
      justify-content: space-between;
      padding: 18px 0;
      border-bottom: 1px solid #f2f6fc;

      &:last-child {
        border-bottom: none;
      }

      .talent-info {
        flex: 1;

        .talent-name {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        .talent-meta {
          margin-top: 8px;
          color: #909399;
          display: flex;
          flex-wrap: wrap;
          gap: 12px;
          font-size: 13px;
        }

        .talent-skills {
          margin-top: 8px;
          color: #606266;
        }
      }

      .talent-side {
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        justify-content: space-between;
        min-width: 160px;
        text-align: right;

        .talent-update {
          color: #909399;
          font-size: 12px;
        }
      }
    }
  }
}
</style>

