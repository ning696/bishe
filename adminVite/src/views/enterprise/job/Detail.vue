<template>
  <div class="enterprise-job-detail" v-loading="loading">
    <el-page-header content="职位详情" @back="router.back()" />
    <template v-if="detail">
      <el-card shadow="never" class="overview-card">
        <div class="overview-left">
          <div class="job-name">{{ detail.jobName }}</div>
          <div class="job-meta">
            <el-tag :type="statusTagType(detail.status)">
              {{ detail.statusName }}
            </el-tag>
            <el-tag v-if="detail.jobType" type="info" class="tag-gap">
              {{ detail.jobType }}
            </el-tag>
            <span class="meta-item">地点：{{ detail.workLocation }}</span>
            <span class="meta-item">招聘人数：{{ detail.recruitCount ?? '未填写' }}</span>
          </div>
          <div class="job-salary">{{ formatSalary(detail) }}</div>
          <div class="job-stat">
            浏览 {{ detail.viewCount ?? 0 }} 次 · 申请 {{ detail.applyCount ?? 0 }} 次
          </div>
        </div>
        <div class="overview-right">
          <el-button type="primary" @click="handleEdit">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="primary" link @click="loadDetail">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </el-card>

      <el-card shadow="never">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="职位类别">
            {{ detail.categoryName ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="学历要求">
            {{ detail.requiredEducation ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="经验要求">
            {{ detail.requiredExperience ? `${detail.requiredExperience} 个月` : '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="专业要求">
            {{ detail.requiredMajor ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="技能要求" :span="2">
            {{ detail.requiredSkills ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="发布时间">
            {{ detail.publishTime ?? '未发布' }}
          </el-descriptions-item>
          <el-descriptions-item label="截止时间">
            {{ detail.expireTime ?? '未设置' }}
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" :span="2">
            {{ detail.auditRemark ?? '暂无' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="section-title">职位描述</div>
        </template>
        <div class="description-content" v-if="detail.jobDescription">
          <p v-html="formatMultiline(detail.jobDescription)" />
        </div>
        <el-empty v-else description="暂无职位描述" />
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="section-title">关联校园</div>
        </template>
        <el-empty v-if="campusList.length === 0" description="暂无关联校园" />
        <el-table v-else :data="campusList" border>
          <el-table-column label="校园名称" prop="campusName" />
          <el-table-column label="校园ID" prop="campusId" width="120" />
        </el-table>
      </el-card>
    </template>
    <el-skeleton v-else animated :rows="6" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  fetchEnterpriseJobDetail,
  fetchJobCampusRelations,
  type EnterpriseJobDetail,
  type CampusJobRelation
} from '@/api/enterprise/job'
import { Refresh, Edit } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const jobId = computed(() => Number(route.params.id))
const loading = ref(false)
const detail = ref<EnterpriseJobDetail | null>(null)
const campusList = ref<CampusJobRelation[]>([])

const statusTagType = (status: number) => {
  switch (status) {
    case 1:
      return 'success'
    case 2:
      return 'danger'
    case 3:
      return 'info'
    default:
      return 'warning'
  }
}

const formatSalary = (item: EnterpriseJobDetail) => {
  const min = item.salaryMin ?? 0
  const max = item.salaryMax ?? 0
  if (!min && !max) return '薪资面议'
  if (min && !max) return `${min} 起 (${item.salaryType ?? ''})`.trim()
  if (!min && max) return `${max} 以下 (${item.salaryType ?? ''})`.trim()
  return `${min} - ${max} ${item.salaryType ?? ''}`.trim()
}

const formatMultiline = (text: string) => {
  return text.replace(/\n/g, '<br />')
}

const loadDetail = async () => {
  if (!jobId.value) {
    ElMessage.error('职位ID不存在')
    router.back()
    return
  }
  loading.value = true
  try {
    const [detailRes, campusRes] = await Promise.all([
      fetchEnterpriseJobDetail(jobId.value),
      fetchJobCampusRelations(jobId.value)
    ])
    detail.value = detailRes.data
    campusList.value = campusRes.data ?? []
  } catch (error) {
    console.error('加载职位详情失败', error)
  } finally {
    loading.value = false
  }
}

const handleEdit = () => {
  if (!jobId.value) return
  router.push({ path: `/enterprise/job/edit/${jobId.value}` })
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped lang="scss">
.enterprise-job-detail {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.overview-card {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;

  .overview-left {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .overview-right {
    display: flex;
    gap: 12px;
    align-items: center;
  }
}

.job-name {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.job-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;

  .meta-item {
    color: #666;
  }
}

.job-salary {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

.job-stat {
  color: #909399;
}

.tag-gap {
  margin-left: 4px;
}

.section-title {
  font-weight: 600;
}

.description-content {
  color: #444;
  line-height: 1.6;
}
</style>

