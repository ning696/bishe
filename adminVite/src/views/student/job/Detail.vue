<template>
  <div class="student-job-detail" v-loading="loading">
    <el-page-header content="职位详情" @back="router.back()" />

    <template v-if="detail">
      <el-card class="operation-card" shadow="never">
        <div class="operation-card__left">
          <div class="job-name">{{ detail.jobName }}</div>
          <div class="job-meta">
            <el-tag type="info" v-if="detail.jobType">{{ detail.jobType }}</el-tag>
            <el-tag type="success" v-if="detail.categoryName" class="meta-gap">
              {{ detail.categoryName }}
            </el-tag>
            <span class="meta-text">{{ detail.enterpriseName }}</span>
            <span class="meta-text">{{ detail.workLocation }}</span>
          </div>
          <div class="job-salary">{{ formatSalary(detail) }}</div>
          <div class="job-stat">
            浏览 {{ detail.viewCount ?? 0 }} 次 · 已有 {{ detail.applyCount ?? 0 }} 人申请
          </div>
        </div>
        <div class="operation-card__right">
          <el-button
            type="primary"
            @click="handleToggleFavorite"
            :loading="favoriteLoading"
          >
            <el-icon class="btn-icon">
              <StarFilled v-if="detail.isFavorite" />
              <Star v-else />
            </el-icon>
            {{ detail.isFavorite ? '取消收藏' : '收藏职位' }}
          </el-button>
          <el-button type="success" @click="openResumeDialog('delivery')">
            <el-icon class="btn-icon"><Promotion /></el-icon>
            投递简历
          </el-button>
          <el-button type="warning" @click="openResumeDialog('interview')">
            <el-icon class="btn-icon"><Calendar /></el-icon>
            申请面试
          </el-button>
          <el-button type="primary" :loading="chatLoading" @click="onStartChat">
            立即沟通
          </el-button>
        </div>
      </el-card>

      <el-card shadow="never">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="职位名称">{{ detail.jobName }}</el-descriptions-item>
          <el-descriptions-item label="所属企业">{{ detail.enterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="工作地点">{{ detail.workLocation }}</el-descriptions-item>
          <el-descriptions-item label="招聘人数">
            {{ detail.recruitCount ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="学历要求">
            {{ detail.requiredEducation ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="工作经验">
            {{ detail.requiredExperience ? `${detail.requiredExperience} 个月` : '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item label="职位类型">{{ detail.jobType ?? '未填写' }}</el-descriptions-item>
          <el-descriptions-item label="发布时间">
            {{ detail.publishTime ?? '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item :span="2" label="薪资范围">
            {{ formatSalary(detail) }}
          </el-descriptions-item>
          <el-descriptions-item :span="2" label="技能要求">
            {{ detail.requiredSkills || '未填写' }}
          </el-descriptions-item>
          <el-descriptions-item :span="2" label="专业要求">
            {{ detail.requiredMajor || '未填写' }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card class="description-card" shadow="never">
        <template #header>
          <div class="description-card__title">岗位描述</div>
        </template>
        <div class="description-card__content">
          <el-empty description="暂无描述" v-if="!detail.jobDescription" />
          <p v-else v-html="formatMultiline(detail.jobDescription)" />
        </div>
      </el-card>

      <el-card class="description-card" shadow="never">
        <template #header>
          <div class="description-card__title">其他信息</div>
        </template>
        <div class="description-card__content">
          <ul class="info-list">
            <li v-if="detail.expireTime">投递截止时间：{{ detail.expireTime }}</li>
            <li>职位编号：{{ detail.id }}</li>
            <li>创建时间：{{ detail.createTime ?? '未填写' }}</li>
          </ul>
        </div>
      </el-card>
    </template>

    <el-skeleton v-else animated :rows="8" />

    <el-dialog
      v-model="resumeDialogVisible"
      :title="dialogMode === 'delivery' ? '投递简历' : '申请面试'"
      width="480px"
      destroy-on-close
    >
      <el-skeleton v-if="resumeLoading" :rows="4" animated />
      <el-form v-else label-width="90px">
        <el-form-item label="职位">
          <span>{{ detail?.jobName }}</span>
        </el-form-item>
        <el-form-item label="选择简历">
          <el-select
            v-model="selectedResumeId"
            placeholder="请选择简历"
            clearable
            filterable
          >
            <el-option
              v-for="resume in resumeOptions"
              :key="resume.id"
              :label="resume.resumeName"
              :value="resume.id"
            >
              <div class="resume-option">
                <span>{{ resume.resumeName }}</span>
                <el-tag v-if="resume.isDefault === 1" size="small" type="success">
                  默认
                </el-tag>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resumeDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          :loading="resumeSubmitting"
          :disabled="!selectedResumeId"
          @click="submitResumeAction"
        >
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  fetchStudentJobDetail,
  favoriteStudentJob,
  unfavoriteStudentJob,
  type StudentJobDetail
} from '@/api/student/job'
import {
  fetchResumeList,
  deliveryResume,
  type ResumeListItem
} from '@/api/student/resume'
import { applyInterview } from '@/api/student/interview'
import { createOrGetSession } from '@/api/student/chat'
import { Star, StarFilled, Promotion, Calendar } from '@element-plus/icons-vue'

type DialogMode = 'delivery' | 'interview'

const route = useRoute()
const router = useRouter()

const jobId = computed(() => Number(route.params.id))
const loading = ref(false)
const detail = ref<StudentJobDetail | null>(null)
const favoriteLoading = ref(false)
const chatLoading = ref(false)

const resumeDialogVisible = ref(false)
const dialogMode = ref<DialogMode>('delivery')
const resumeOptions = ref<ResumeListItem[]>([])
const resumeLoading = ref(false)
const resumeSubmitting = ref(false)
const selectedResumeId = ref<number | null>(null)

const loadDetail = async () => {
  if (!jobId.value) {
    ElMessage.error('无效的职位ID')
    return
  }
  loading.value = true
  try {
    const res = await fetchStudentJobDetail(jobId.value)
    detail.value = res.data
  } catch (error) {
    console.error('获取职位详情失败', error)
  } finally {
    loading.value = false
  }
}

const handleToggleFavorite = async () => {
  if (!detail.value) return
  favoriteLoading.value = true
  try {
    if (detail.value.isFavorite) {
      await unfavoriteStudentJob(detail.value.id)
      detail.value.isFavorite = false
      ElMessage.success('已取消收藏')
    } else {
      await favoriteStudentJob(detail.value.id)
      detail.value.isFavorite = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('收藏操作失败', error)
  } finally {
    favoriteLoading.value = false
  }
}

const ensureResumeOptions = async () => {
  if (resumeOptions.value.length > 0) {
    return
  }
  resumeLoading.value = true
  try {
    const res = await fetchResumeList()
    resumeOptions.value = res.data ?? []
  } catch (error) {
    console.error('加载简历列表失败', error)
  } finally {
    resumeLoading.value = false
  }
}

const openResumeDialog = async (mode: DialogMode) => {
  if (!detail.value) return
  dialogMode.value = mode
  selectedResumeId.value = null
  resumeDialogVisible.value = true
  await ensureResumeOptions()
}

const submitResumeAction = async () => {
  if (!detail.value || !selectedResumeId.value) {
    ElMessage.warning('请选择简历')
    return
  }
  resumeSubmitting.value = true
  try {
    if (dialogMode.value === 'delivery') {
      await deliveryResume({
        jobId: detail.value.id,
        resumeId: selectedResumeId.value
      })
      detail.value.isApplied = true
      ElMessage.success('投递成功')
    } else {
      await applyInterview({
        jobId: detail.value.id,
        resumeId: selectedResumeId.value
      })
      ElMessage.success('申请已提交，请等待企业回复')
    }
    resumeDialogVisible.value = false
  } catch (error) {
    console.error('操作失败', error)
  } finally {
    resumeSubmitting.value = false
  }
}

const formatSalary = (item: StudentJobDetail) => {
  const min = item.salaryMin ?? 0
  const max = item.salaryMax ?? 0
  if (!min && !max) {
    return '薪资面议'
  }
  if (min && !max) {
    return `${min} 起 (${item.salaryType ?? ''})`.trim()
  }
  if (!min && max) {
    return `${max} 以下 (${item.salaryType ?? ''})`.trim()
  }
  return `${min} - ${max} ${item.salaryType ?? ''}`.trim()
}

const formatMultiline = (text: string) => {
  return text.replace(/\n/g, '<br />')
}

const onStartChat = async () => {
  if (!detail.value) return
  chatLoading.value = true
  try {
    const res = await createOrGetSession({
      enterpriseId: detail.value.enterpriseId,
      jobId: detail.value.id
    })
    // 使用 id 字段（后端返回的是 id，不是 sessionId）
    const sessionId = res.data?.id
    if (sessionId) {
      router.push({ name: 'StudentChatSession', params: { id: sessionId } })
    } else {
      ElMessage.error('创建会话失败：未返回会话ID')
    }
  } catch (error) {
    // 统一错误在请求拦截器已提示
  } finally {
    chatLoading.value = false
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped lang="scss">
.student-job-detail {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.operation-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;

  &__left {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  &__right {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }
}

.job-name {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.job-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;

  .meta-gap {
    margin-left: 4px;
  }

  .meta-text {
    color: #666;
  }
}

.job-salary {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

.job-stat {
  color: #999;
}

.description-card {
  .description-card__title {
    font-weight: 600;
  }

  .description-card__content {
    color: #444;
    line-height: 1.6;

    p {
      white-space: pre-wrap;
      margin: 0;
    }
  }
}

.info-list {
  padding-left: 18px;
  margin: 0;

  li {
    line-height: 1.8;
    color: #555;
  }
}

.resume-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-icon {
  margin-right: 4px;
}
</style>

