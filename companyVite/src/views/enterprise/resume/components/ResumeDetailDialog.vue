<template>
  <el-dialog
    v-model="visible"
    title="简历详情"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="resume-detail">
      <!-- 学生基本信息 -->
      <el-card shadow="never" v-if="detail?.studentName">
        <template #header>
          <div class="section-header">
            <span>学生信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名">{{ detail.studentName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ detail.studentPhone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱" :span="2">{{ detail.studentEmail || '-' }}</el-descriptions-item>
          <el-descriptions-item v-if="detail.deliveryStatusName" label="投递状态">
            <el-tag :type="getStatusTagType(detail.deliveryStatus)">
              {{ detail.deliveryStatusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="detail.viewTime" label="查看时间">
            {{ detail.viewTime }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 简历基本信息 -->
      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>简历信息</span>
            <el-button v-if="detail?.resumeFile" type="primary" @click="downloadResume">
              <el-icon><Download /></el-icon>
              下载简历
            </el-button>
          </div>
        </template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="简历名称">{{ detail?.resumeName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="简历文件">
            <el-link v-if="detail?.resumeFile" :href="detail.resumeFile" target="_blank" type="primary">
              {{ getFileName(detail.resumeFile) }}
            </el-link>
            <span v-else>-</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 个人信息 -->
      <el-card shadow="never" v-if="personalInfo">
        <template #header>
          <div class="section-header">
            <span>个人信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名">{{ personalInfo.name || '-' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ personalInfo.phone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ personalInfo.email || '-' }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ personalInfo.gender || '-' }}</el-descriptions-item>
          <el-descriptions-item label="出生日期" :span="2">{{ personalInfo.birthday || '-' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 教育背景 -->
      <el-card shadow="never" v-if="educationBackground && educationBackground.length > 0">
        <template #header>
          <div class="section-header">
            <span>教育背景</span>
          </div>
        </template>
        <div
          v-for="(item, index) in educationBackground"
          :key="index"
          class="experience-item"
        >
          <div class="experience-header">
            <span class="experience-title">{{ item.school || '未知学校' }}</span>
            <span class="experience-time">{{ formatTimeRange(item.startTime, item.endTime) }}</span>
          </div>
          <div class="experience-content">
            <div><strong>专业：</strong>{{ item.major || '-' }}</div>
            <div><strong>学历：</strong>{{ item.education || '-' }}</div>
          </div>
        </div>
      </el-card>

      <!-- 工作经历 -->
      <el-card shadow="never" v-if="workExperience && workExperience.length > 0">
        <template #header>
          <div class="section-header">
            <span>工作经历</span>
          </div>
        </template>
        <div
          v-for="(item, index) in workExperience"
          :key="index"
          class="experience-item"
        >
          <div class="experience-header">
            <span class="experience-title">{{ item.company || '未知公司' }} - {{ item.position || '未知职位' }}</span>
            <span class="experience-time">{{ formatTimeRange(item.startTime, item.endTime) }}</span>
          </div>
          <div class="experience-content" v-if="item.description">
            {{ item.description }}
          </div>
        </div>
      </el-card>

      <!-- 项目经历 -->
      <el-card shadow="never" v-if="projectExperience && projectExperience.length > 0">
        <template #header>
          <div class="section-header">
            <span>项目经历</span>
          </div>
        </template>
        <div
          v-for="(item, index) in projectExperience"
          :key="index"
          class="experience-item"
        >
          <div class="experience-header">
            <span class="experience-title">{{ item.projectName || '未知项目' }}</span>
            <span class="experience-time">{{ formatTimeRange(item.startTime, item.endTime) }}</span>
          </div>
          <div class="experience-content">
            <div v-if="item.role"><strong>角色：</strong>{{ item.role }}</div>
            <div v-if="item.description" style="margin-top: 8px">{{ item.description }}</div>
          </div>
        </div>
      </el-card>

      <!-- 技能与自我评价 -->
      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>技能与自我评价</span>
          </div>
        </template>
        <div class="skills-section">
          <div v-if="detail?.skills">
            <strong>技能标签：</strong>
            <div class="skills-content">{{ detail.skills }}</div>
          </div>
          <div v-if="detail?.selfIntroduction" style="margin-top: 16px">
            <strong>自我介绍：</strong>
            <div class="self-intro-content">{{ detail.selfIntroduction }}</div>
          </div>
          <div v-if="!detail?.skills && !detail?.selfIntroduction" class="empty-tip">
            暂无技能和自我评价信息
          </div>
        </div>
      </el-card>
    </div>
    <template #footer>
      <el-button type="primary" @click="handleClose">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import { fetchResumeDetail, type EnterpriseResumeDetail } from '@/api/enterprise/resume'

interface PersonalInfo {
  name?: string
  phone?: string
  email?: string
  gender?: string
  birthday?: string
}

interface EducationItem {
  school?: string
  major?: string
  education?: string
  startTime?: string
  endTime?: string
}

interface WorkItem {
  company?: string
  position?: string
  description?: string
  startTime?: string
  endTime?: string
}

interface ProjectItem {
  projectName?: string
  role?: string
  description?: string
  startTime?: string
  endTime?: string
}

const props = defineProps<{
  modelValue: boolean
  resumeId?: number
  applicationId?: number
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const loading = ref(false)
const detail = ref<EnterpriseResumeDetail | null>(null)

const personalInfo = computed<PersonalInfo | null>(() => {
  if (!detail.value?.personalInfo) return null
  try {
    return JSON.parse(detail.value.personalInfo)
  } catch {
    return null
  }
})

const educationBackground = computed<EducationItem[]>(() => {
  if (!detail.value?.educationBackground) return []
  try {
    const parsed = JSON.parse(detail.value.educationBackground)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
})

const workExperience = computed<WorkItem[]>(() => {
  if (!detail.value?.workExperience) return []
  try {
    const parsed = JSON.parse(detail.value.workExperience)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
})

const projectExperience = computed<ProjectItem[]>(() => {
  if (!detail.value?.projectExperience) return []
  try {
    const parsed = JSON.parse(detail.value.projectExperience)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
})

watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal && props.resumeId) {
      loadDetail()
    }
  },
  { immediate: true }
)

async function loadDetail() {
  if (!props.resumeId) return

  loading.value = true
  try {
    const res = await fetchResumeDetail(props.resumeId, props.applicationId)
    if (res.code === 1000 && res.data) {
      detail.value = res.data
    } else {
      ElMessage.error(res.msg || '获取简历详情失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '获取简历详情失败')
  } finally {
    loading.value = false
  }
}

function formatTimeRange(startTime?: string, endTime?: string) {
  if (!startTime && !endTime) return '-'
  if (startTime && endTime) return `${startTime} 至 ${endTime}`
  if (startTime) return `${startTime} 至今`
  return endTime || '-'
}

function getFileName(url: string) {
  if (!url) return ''
  const parts = url.split('/')
  return parts[parts.length - 1] || '简历文件'
}

function getStatusTagType(status?: number) {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  if (status === 3) return 'info'
  return 'warning'
}

function downloadResume() {
  if (!detail.value?.resumeFile) {
    ElMessage.warning('简历文件不存在')
    return
  }
  // 直接打开链接下载
  window.open(detail.value.resumeFile, '_blank')
}

function handleClose() {
  detail.value = null
  visible.value = false
}
</script>

<style scoped lang="scss">
.resume-detail {
  max-height: 70vh;
  overflow-y: auto;
  padding: 8px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.experience-item {
  padding: 12px;
  margin-bottom: 12px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  background-color: #fafafa;

  &:last-child {
    margin-bottom: 0;
  }
}

.experience-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.experience-title {
  font-weight: 600;
  font-size: 15px;
  color: #303133;
}

.experience-time {
  color: #909399;
  font-size: 13px;
}

.experience-content {
  color: #606266;
  line-height: 1.6;
  font-size: 14px;
}

.skills-section {
  color: #606266;
  line-height: 1.8;
}

.skills-content,
.self-intro-content {
  margin-top: 8px;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  white-space: pre-wrap;
  word-break: break-word;
}

.empty-tip {
  color: #909399;
  text-align: center;
  padding: 20px;
}
</style>

