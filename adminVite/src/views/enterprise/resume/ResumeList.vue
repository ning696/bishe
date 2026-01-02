<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>职位申请</span>
          <div class="actions">
            <el-input v-model="query.jobId" placeholder="职位ID" clearable style="width: 140px" />
            <el-select v-model="query.applicationStatus" placeholder="申请状态" clearable style="width: 140px; margin-left: 8px">
              <el-option :value="0" label="待处理" />
              <el-option :value="1" label="已通过" />
              <el-option :value="2" label="已拒绝" />
              <el-option :value="3" label="已取消" />
            </el-select>
            <el-button type="primary" style="margin-left: 8px" @click="handleSearch">查询</el-button>
            <el-button style="margin-left: 8px" @click="handleReset">重置</el-button>
          </div>
        </div>
      </template>
      <el-skeleton :loading="loading" animated>
        <template #template>
          <el-skeleton-item variant="text" style="width: 40%; margin-bottom: 12px;" />
          <el-skeleton-item variant="rect" style="height: 300px;" />
        </template>
        <template #default>
          <el-table :data="rows" v-loading="loading" style="width: 100%" border>
            <el-table-column prop="studentName" label="学生姓名" min-width="120" />
            <el-table-column prop="studentPhone" label="手机号" min-width="140" />
            <el-table-column prop="jobName" label="职位名称" min-width="180" />
            <el-table-column prop="applicationStatusName" label="申请状态" min-width="120">
              <template #default="{ row }">
                <el-tag :type="applicationStatusTag(row.applicationStatus)">{{ row.applicationStatusName }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="applicationTime" label="申请时间" min-width="180" />
            <el-table-column label="操作" width="280" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="viewResume(row)">查看简历</el-button>
                <el-button 
                  v-if="row.applicationStatus === 0" 
                  type="success" 
                  link 
                  @click="handleApplication(row, 1)"
                >
                  通过
                </el-button>
                <el-button 
                  v-if="row.applicationStatus === 0" 
                  type="danger" 
                  link 
                  @click="handleApplication(row, 2)"
                >
                  拒绝
                </el-button>
                <el-button 
                  v-if="row.applicationStatus === 1" 
                  type="warning" 
                  link 
                  @click="showArrangeDialog(row)"
                >
                  安排面试
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              v-model:current-page="query.pageNum"
              v-model:page-size="query.pageSize"
              :page-sizes="[10, 20, 50]"
              @size-change="handleSearch"
              @current-change="handleSearch"
            />
          </div>
        </template>
      </el-skeleton>
      <el-empty v-if="!loading && rows.length === 0" description="暂无数据" />
    </el-card>

    <!-- 处理申请对话框 -->
    <el-dialog
      v-model="handleDialogVisible"
      :title="handleForm.applicationStatus === 1 ? '通过申请' : '拒绝申请'"
      width="500px"
    >
      <el-form :model="handleForm" label-width="100px">
        <el-form-item label="学生姓名">
          <el-input v-model="handleForm.studentName" disabled />
        </el-form-item>
        <el-form-item label="职位名称">
          <el-input v-model="handleForm.jobName" disabled />
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input
            v-model="handleForm.handleRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入处理备注（可选）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmHandle" :loading="handleLoading">确认</el-button>
      </template>
    </el-dialog>

    <!-- 安排面试对话框 -->
    <el-dialog
      v-model="arrangeDialogVisible"
      title="安排面试"
      width="600px"
    >
      <el-form :model="arrangeForm" :rules="arrangeRules" ref="arrangeFormRef" label-width="100px">
        <el-form-item label="学生姓名">
          <el-input v-model="arrangeForm.studentName" disabled />
        </el-form-item>
        <el-form-item label="职位名称">
          <el-input v-model="arrangeForm.jobName" disabled />
        </el-form-item>
        <el-form-item label="面试时间" prop="interviewTime">
          <el-date-picker
            v-model="arrangeForm.interviewTime"
            type="datetime"
            placeholder="选择面试时间"
            style="width: 100%"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="面试地点" prop="interviewLocation">
          <el-input v-model="arrangeForm.interviewLocation" placeholder="请输入面试地点" />
        </el-form-item>
        <el-form-item label="面试类型" prop="interviewType">
          <el-select v-model="arrangeForm.interviewType" placeholder="请选择面试类型" style="width: 100%">
            <el-option label="现场面试" value="现场面试" />
            <el-option label="视频面试" value="视频面试" />
            <el-option label="电话面试" value="电话面试" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="arrangeForm.contactPerson" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="arrangeForm.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="arrangeForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（可选）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="arrangeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmArrange" :loading="arrangeLoading">确认安排</el-button>
      </template>
    </el-dialog>

    <!-- 简历详情对话框 -->
    <ResumeDetailDialog
      v-model="resumeDetailVisible"
      :resume-id="currentResumeId"
      :application-id="currentApplicationId"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  fetchInterviewApplications,
  handleInterviewApplication,
  arrangeInterview,
  type EnterpriseInterviewApplicationItem,
  type InterviewApplicationHandlePayload,
  type InterviewArrangePayload
} from '@/api/enterprise/interview'
import ResumeDetailDialog from './components/ResumeDetailDialog.vue'

const loading = ref(false)
const rows = ref<EnterpriseInterviewApplicationItem[]>([])
const total = ref(0)
const query = reactive({
  pageNum: 1,
  pageSize: 10,
  jobId: '' as any,
  applicationStatus: '' as any
})

// 处理申请对话框
const handleDialogVisible = ref(false)
const handleLoading = ref(false)
const handleForm = reactive<{
  applicationId: number
  studentId: number
  studentName: string
  jobName: string
  applicationStatus: number
  handleRemark: string
}>({
  applicationId: 0,
  studentId: 0,
  studentName: '',
  jobName: '',
  applicationStatus: 1,
  handleRemark: ''
})

// 安排面试对话框
const arrangeDialogVisible = ref(false)
const arrangeLoading = ref(false)
const arrangeFormRef = ref<FormInstance>()
const arrangeForm = reactive<InterviewArrangePayload & {
  studentName: string
  jobName: string
}>({
  applicationId: 0,
  studentId: 0,
  jobId: 0,
  studentName: '',
  jobName: '',
  interviewTime: '',
  interviewLocation: '',
  interviewType: '现场面试',
  contactPerson: '',
  contactPhone: '',
  remark: ''
})

// 简历详情对话框
const resumeDetailVisible = ref(false)
const currentResumeId = ref<number | undefined>(undefined)
const currentApplicationId = ref<number | undefined>(undefined)

const arrangeRules: FormRules = {
  interviewTime: [
    { required: true, message: '请选择面试时间', trigger: 'change' }
  ],
  interviewLocation: [
    { required: true, message: '请输入面试地点', trigger: 'blur' }
  ]
}

function applicationStatusTag(status?: number) {
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  if (status === 3) return 'info'
  return 'warning'
}

function handleSearch() {
  query.pageNum = 1
  fetchData()
}

function handleReset() {
  query.jobId = ''
  query.applicationStatus = ''
  handleSearch()
}

function viewResume(row: EnterpriseInterviewApplicationItem) {
  if (!row.resumeId) {
    ElMessage.warning('该申请没有关联简历')
    return
  }
  currentResumeId.value = row.resumeId
  currentApplicationId.value = row.id
  resumeDetailVisible.value = true
}

function handleApplication(row: EnterpriseInterviewApplicationItem, status: number) {
  handleForm.applicationId = row.id
  handleForm.studentId = row.studentId
  handleForm.studentName = row.studentName
  handleForm.jobName = row.jobName
  handleForm.applicationStatus = status
  handleForm.handleRemark = ''
  handleDialogVisible.value = true
}

async function confirmHandle() {
  if (!handleForm.applicationId) {
    ElMessage.error('申请ID不能为空')
    return
  }

  handleLoading.value = true
  try {
    const payload: InterviewApplicationHandlePayload = {
      applicationId: handleForm.applicationId,
      applicationStatus: handleForm.applicationStatus,
      handleRemark: handleForm.handleRemark || undefined
    }
    await handleInterviewApplication(payload)
    ElMessage.success(handleForm.applicationStatus === 1 ? '申请已通过' : '申请已拒绝')
    handleDialogVisible.value = false
    fetchData()
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    handleLoading.value = false
  }
}

function showArrangeDialog(row: EnterpriseInterviewApplicationItem) {
  arrangeForm.applicationId = row.id
  arrangeForm.studentId = row.studentId
  arrangeForm.jobId = row.jobId
  arrangeForm.studentName = row.studentName
  arrangeForm.jobName = row.jobName
  arrangeForm.interviewTime = ''
  arrangeForm.interviewLocation = ''
  arrangeForm.interviewType = '现场面试'
  arrangeForm.contactPerson = ''
  arrangeForm.contactPhone = ''
  arrangeForm.remark = ''
  arrangeDialogVisible.value = true
}

async function confirmArrange() {
  if (!arrangeFormRef.value) return

  await arrangeFormRef.value.validate(async (valid) => {
    if (!valid) return

    arrangeLoading.value = true
    try {
      const payload: InterviewArrangePayload = {
        applicationId: arrangeForm.applicationId,
        studentId: arrangeForm.studentId,
        jobId: arrangeForm.jobId,
        interviewTime: arrangeForm.interviewTime,
        interviewLocation: arrangeForm.interviewLocation,
        interviewType: arrangeForm.interviewType || undefined,
        contactPerson: arrangeForm.contactPerson || undefined,
        contactPhone: arrangeForm.contactPhone || undefined,
        remark: arrangeForm.remark || undefined
      }
      await arrangeInterview(payload)
      ElMessage.success('面试安排成功')
      arrangeDialogVisible.value = false
      fetchData()
    } catch (error: any) {
      ElMessage.error(error.message || '安排面试失败')
    } finally {
      arrangeLoading.value = false
    }
  })
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetchInterviewApplications({
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      jobId: query.jobId || undefined,
      applicationStatus: query.applicationStatus !== '' ? query.applicationStatus : undefined
    })
    rows.value = res.rows ?? []
    total.value = res.total ?? 0
  } catch (error: any) {
    ElMessage.error(error.message || '获取数据失败')
  } finally {
    loading.value = false
  }
}

watch(() => [query.pageNum, query.pageSize], () => {
  fetchData()
})

onMounted(fetchData)
</script>

<style scoped>
.page-container {
  padding: 16px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.actions {
  display: flex;
  align-items: center;
}
.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
