<template>
  <div class="student-job-list">
    <el-card class="query-card" shadow="never">
      <el-form :inline="true" :model="queryForm" label-width="90px">
        <el-form-item label="职位名称">
          <el-input
            v-model="queryForm.jobName"
            placeholder="请输入职位或企业名称"
            clearable
            class="query-item"
          />
        </el-form-item>
        <el-form-item label="工作地点">
          <el-input
            v-model="queryForm.workLocation"
            placeholder="请输入地点"
            clearable
            class="query-item"
          />
        </el-form-item>
        <el-form-item label="工作类型">
          <el-select
            v-model="queryForm.jobType"
            placeholder="请选择"
            clearable
            class="query-item"
          >
            <el-option
              v-for="item in jobTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学历要求">
          <el-select
            v-model="queryForm.requiredEducation"
            placeholder="请选择"
            clearable
            class="query-item"
          >
            <el-option
              v-for="item in educationOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="薪资下限">
          <el-input-number
            v-model="queryForm.salaryMin"
            :min="0"
            :max="queryForm.salaryMax || undefined"
            :step="1000"
            placeholder="0"
            class="query-item"
          />
        </el-form-item>
        <el-form-item label="薪资上限">
          <el-input-number
            v-model="queryForm.salaryMax"
            :min="queryForm.salaryMin || 0"
            :step="1000"
            placeholder="不限"
            class="query-item"
          />
        </el-form-item>
        <el-form-item label="排序">
          <el-select
            v-model="queryForm.sortField"
            placeholder="请选择"
            clearable
            class="query-item"
          >
            <el-option
              v-for="item in sortFieldOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="顺序">
          <el-select
            v-model="queryForm.sortOrder"
            placeholder="请选择"
            clearable
            class="query-item"
          >
            <el-option label="降序" value="desc" />
            <el-option label="升序" value="asc" />
          </el-select>
        </el-form-item>
        <el-form-item class="query-actions">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>职位列表</span>
          <el-button type="primary" link @click="loadJobs">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      <el-table
        v-loading="loading"
        :data="jobList"
        border
        class="job-table"
        empty-text="暂无数据"
      >
        <el-table-column label="职位/企业" min-width="220">
          <template #default="{ row }">
            <div class="job-title" @click="handleViewDetail(row.id)">
              <el-icon class="job-title__icon"><Briefcase /></el-icon>
              <div>
                <div class="job-title__name">{{ row.jobName }}</div>
                <div class="job-title__enterprise">{{ row.enterpriseName }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="地点" prop="workLocation" width="120" />
        <el-table-column label="薪资" width="160">
          <template #default="{ row }">
            {{ formatSalary(row) }}
          </template>
        </el-table-column>
        <el-table-column label="类型" prop="jobType" width="110" />
        <el-table-column label="浏览/申请" width="140">
          <template #default="{ row }">
            <el-tag size="small" type="info">浏览 {{ row.viewCount ?? 0 }}</el-tag>
            <el-tag size="small" type="success" class="tag-gap">
              申请 {{ row.applyCount ?? 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="publishTime" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleViewDetail(row.id)">
              查看
            </el-button>
            <el-divider direction="vertical" />
            <el-button
              link
              :type="row.isFavorite ? 'warning' : 'primary'"
              @click="toggleFavorite(row)"
              :loading="favoriteLoadingId === row.id"
            >
              {{ row.isFavorite ? '取消收藏' : '收藏' }}
            </el-button>
            <el-divider direction="vertical" />
            <el-button
              link
              type="success"
              @click="openDeliveryDialog(row.id)"
            >
              投递简历
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="applyDialogVisible"
      title="投递简历"
      width="480px"
      destroy-on-close
    >
      <el-skeleton v-if="resumeLoading" :rows="4" animated />
      <el-form v-else label-width="90px">
        <el-form-item label="职位">
          <span>{{ currentJobName }}</span>
        </el-form-item>
        <el-form-item label="选择简历">
          <el-select
            v-model="selectedResumeId"
            placeholder="请选择简历"
            filterable
            clearable
          >
            <el-option
              v-for="resume in resumeOptions"
              :key="resume.id"
              :label="resume.resumeName"
              :value="resume.id"
            >
              <div class="resume-option">
                <span>{{ resume.resumeName }}</span>
                <el-tag
                  v-if="resume.isDefault === 1"
                  size="small"
                  type="success"
                >
                  默认
                </el-tag>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="applyDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          :loading="applySubmitting"
          :disabled="!selectedResumeId"
          @click="submitDelivery"
        >
          确认投递
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  fetchStudentJobList,
  favoriteStudentJob,
  unfavoriteStudentJob,
  type JobListQuery,
  type StudentJobListItem
} from '@/api/student/job'
import {
  deliveryResume,
  fetchResumeList,
  type ResumeListItem
} from '@/api/student/resume'
import { Refresh, Briefcase } from '@element-plus/icons-vue'

interface QueryForm {
  jobName: string
  workLocation: string
  jobType: string
  requiredEducation: string
  salaryMin?: number | null
  salaryMax?: number | null
  sortField?: string
  sortOrder?: 'asc' | 'desc'
}

const router = useRouter()

const loading = ref(false)
const jobList = ref<StudentJobListItem[]>([])
const favoriteLoadingId = ref<number | null>(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const queryForm = reactive<QueryForm>({
  jobName: '',
  workLocation: '',
  jobType: '',
  requiredEducation: '',
  salaryMin: undefined,
  salaryMax: undefined,
  sortField: 'publishTime',
  sortOrder: 'desc'
})

const jobTypeOptions = [
  { label: '全职', value: '全职' },
  { label: '兼职', value: '兼职' },
  { label: '实习', value: '实习' }
]

const educationOptions = [
  { label: '专科', value: '专科' },
  { label: '本科', value: '本科' },
  { label: '硕士', value: '硕士' },
  { label: '博士', value: '博士' }
]

const sortFieldOptions = [
  { label: '发布时间', value: 'publishTime' },
  { label: '薪资', value: 'salary' },
  { label: '浏览量', value: 'viewCount' }
]

const resumeOptions = ref<ResumeListItem[]>([])
const resumeLoading = ref(false)
const applyDialogVisible = ref(false)
const selectedResumeId = ref<number | null>(null)
const currentJobId = ref<number | null>(null)
const applySubmitting = ref(false)

const currentJobName = computed(() => {
  const job = jobList.value.find((item) => item.id === currentJobId.value)
  return job?.jobName ?? ''
})

const buildQueryPayload = (): JobListQuery => {
  const payload: JobListQuery = {
    pageNum: pagination.pageNum,
    pageSize: pagination.pageSize
  }
  if (queryForm.jobName) payload.jobName = queryForm.jobName
  if (queryForm.workLocation) payload.workLocation = queryForm.workLocation
  if (queryForm.jobType) payload.jobType = queryForm.jobType
  if (queryForm.requiredEducation) {
    payload.requiredEducation = queryForm.requiredEducation
  }
  if (
    queryForm.salaryMin !== undefined &&
    queryForm.salaryMin !== null &&
    queryForm.salaryMin !== 0
  ) {
    payload.salaryMin = queryForm.salaryMin
  }
  if (
    queryForm.salaryMax !== undefined &&
    queryForm.salaryMax !== null &&
    queryForm.salaryMax !== 0
  ) {
    payload.salaryMax = queryForm.salaryMax
  }
  if (queryForm.sortField) payload.sortField = queryForm.sortField
  if (queryForm.sortOrder) payload.sortOrder = queryForm.sortOrder
  return payload
}

const loadJobs = async () => {
  loading.value = true
  try {
    const response = await fetchStudentJobList(buildQueryPayload())
    const tableData = response.data
    jobList.value = (tableData?.rows ?? []) as StudentJobListItem[]
    pagination.total = Number(tableData?.total ?? 0)
  } catch (error) {
    console.error('加载职位列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadJobs()
}

const handleReset = () => {
  queryForm.jobName = ''
  queryForm.workLocation = ''
  queryForm.jobType = ''
  queryForm.requiredEducation = ''
  queryForm.salaryMin = undefined
  queryForm.salaryMax = undefined
  queryForm.sortField = 'publishTime'
  queryForm.sortOrder = 'desc'
  pagination.pageNum = 1
  loadJobs()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  loadJobs()
}

const handleCurrentChange = (page: number) => {
  pagination.pageNum = page
  loadJobs()
}

const handleViewDetail = (jobId: number) => {
  router.push({ path: `/student/job/${jobId}` })
}

const toggleFavorite = async (row: StudentJobListItem) => {
  if (!row.id) return
  favoriteLoadingId.value = row.id
  try {
    if (row.isFavorite) {
      await unfavoriteStudentJob(row.id)
      row.isFavorite = false
      ElMessage.success('已取消收藏')
    } else {
      await favoriteStudentJob(row.id)
      row.isFavorite = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    console.error('收藏操作失败', error)
  } finally {
    favoriteLoadingId.value = null
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

const openDeliveryDialog = async (jobId: number) => {
  currentJobId.value = jobId
  selectedResumeId.value = null
  applyDialogVisible.value = true
  await ensureResumeOptions()
}

const submitDelivery = async () => {
  if (!currentJobId.value || !selectedResumeId.value) {
    ElMessage.warning('请选择简历后再投递')
    return
  }
  applySubmitting.value = true
  try {
    await deliveryResume({
      jobId: currentJobId.value,
      resumeId: selectedResumeId.value
    })
    ElMessage.success('投递成功')
    applyDialogVisible.value = false
  } catch (error) {
    console.error('投递简历失败', error)
  } finally {
    applySubmitting.value = false
  }
}

const formatSalary = (row: StudentJobListItem) => {
  const min = row.salaryMin ?? 0
  const max = row.salaryMax ?? 0
  if (!min && !max) {
    return '面议'
  }
  if (min && !max) {
    return `${min} 起`
  }
  if (!min && max) {
    return `${max} 以下`
  }
  return `${min} - ${max} ${row.salaryType ?? ''}`.trim()
}

onMounted(() => {
  loadJobs()
})
</script>

<style scoped lang="scss">
.student-job-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
  box-sizing: border-box;

  .query-card {
    .query-item {
      width: 200px;
    }

    .query-actions {
      margin-left: 16px;
    }
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 16px;
      font-weight: 500;
    }

    .job-table {
      width: 100%;
    }

    .pagination-wrapper {
      display: flex;
      justify-content: flex-end;
      padding-top: 12px;
    }
  }
}

.job-title {
  display: flex;
  gap: 12px;
  align-items: center;
  cursor: pointer;

  &__icon {
    font-size: 22px;
    color: #409eff;
  }

  &__name {
    font-weight: 600;
    color: #333;
  }

  &__enterprise {
    color: #999;
    font-size: 12px;
  }

  &:hover .job-title__name {
    color: #409eff;
  }
}

.tag-gap {
  margin-left: 6px;
}

.resume-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
}
</style>

