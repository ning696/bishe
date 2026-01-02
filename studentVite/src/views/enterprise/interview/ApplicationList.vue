<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>面试管理</span>
          <div class="actions">
            <el-input v-model="query.jobId" placeholder="职位ID" clearable style="width: 160px" />
            <el-select v-model="query.interviewStatus" placeholder="面试状态" clearable style="width: 160px; margin-left: 8px">
              <el-option :value="0" label="待安排" />
              <el-option :value="1" label="已安排" />
              <el-option :value="2" label="已完成" />
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
            <el-table-column prop="jobName" label="职位名称" min-width="180" />
            <el-table-column prop="interviewTime" label="面试时间" min-width="180" />
            <el-table-column prop="interviewLocation" label="面试地点" min-width="200" />
            <el-table-column prop="interviewType" label="面试类型" width="120" />
            <el-table-column prop="interviewStatusName" label="面试状态" min-width="120">
              <template #default="{ row }">
                <el-tag :type="interviewStatusTag(row.interviewStatus)">{{ row.interviewStatusName }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="contactPerson" label="联系人" width="120" />
            <el-table-column prop="contactPhone" label="联系电话" min-width="140" />
            <el-table-column prop="createTime" label="创建时间" min-width="180" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="viewDetail(row)">查看详情</el-button>
                <el-button 
                  v-if="row.interviewStatus === 0" 
                  type="warning" 
                  link 
                  @click="showArrangeDialog(row)"
                >
                  安排面试
                </el-button>
                <el-button 
                  v-if="row.interviewStatus === 1" 
                  type="success" 
                  link 
                  @click="showArrangeDialog(row)"
                >
                  修改安排
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

    <!-- 安排/修改面试对话框 -->
    <el-dialog
      v-model="arrangeDialogVisible"
      :title="arrangeForm.id ? '修改面试安排' : '安排面试'"
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
        <el-button type="primary" @click="confirmArrange" :loading="arrangeLoading">确认</el-button>
      </template>
    </el-dialog>

    <!-- 面试详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="面试详情"
      width="600px"
    >
      <el-descriptions :column="1" border v-if="currentRow">
        <el-descriptions-item label="学生姓名">{{ currentRow.studentName }}</el-descriptions-item>
        <el-descriptions-item label="职位名称">{{ currentRow.jobName }}</el-descriptions-item>
        <el-descriptions-item label="面试时间">{{ currentRow.interviewTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="面试地点">{{ currentRow.interviewLocation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="面试类型">{{ currentRow.interviewType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="面试状态">
          <el-tag :type="interviewStatusTag(currentRow.interviewStatus)">{{ currentRow.interviewStatusName }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentRow.contactPerson || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.contactPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRow.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button type="primary" @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import {
  fetchInterviewList,
  arrangeInterview,
  type EnterpriseInterviewItem,
  type InterviewArrangePayload
} from '@/api/enterprise/interview'

const loading = ref(false)
const rows = ref<EnterpriseInterviewItem[]>([])
const total = ref(0)
const query = reactive({
  pageNum: 1,
  pageSize: 10,
  jobId: '' as any,
  interviewStatus: '' as any
})

// 安排面试对话框
const arrangeDialogVisible = ref(false)
const arrangeLoading = ref(false)
const arrangeFormRef = ref<FormInstance>()
const arrangeForm = reactive<InterviewArrangePayload & {
  id?: number
  studentName: string
  jobName: string
  remark?: string
}>({
  id: undefined,
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

const arrangeRules: FormRules = {
  interviewTime: [
    { required: true, message: '请选择面试时间', trigger: 'change' }
  ],
  interviewLocation: [
    { required: true, message: '请输入面试地点', trigger: 'blur' }
  ]
}

// 详情对话框
const detailDialogVisible = ref(false)
const currentRow = ref<EnterpriseInterviewItem | null>(null)

function interviewStatusTag(status?: number) {
  if (status === 1) return 'success'
  if (status === 2) return 'info'
  if (status === 3) return 'danger'
  return 'warning'
}

function handleSearch() {
  query.pageNum = 1
  fetchData()
}

function handleReset() {
  query.jobId = ''
  query.interviewStatus = ''
  handleSearch()
}

function viewDetail(row: EnterpriseInterviewItem) {
  currentRow.value = row
  detailDialogVisible.value = true
}

function showArrangeDialog(row: EnterpriseInterviewItem) {
  arrangeForm.id = row.id
  // 如果interview已存在，需要从job_application表查询对应的applicationId
  // 但根据业务逻辑，如果interview已存在，应该通过更新接口而不是创建接口
  // 这里先使用0，实际使用时可能需要根据业务逻辑调整
  arrangeForm.applicationId = row.applicationId || 0
  arrangeForm.studentId = row.studentId
  arrangeForm.jobId = row.jobId
  arrangeForm.studentName = row.studentName
  arrangeForm.jobName = row.jobName
  arrangeForm.interviewTime = row.interviewTime || ''
  arrangeForm.interviewLocation = row.interviewLocation || ''
  arrangeForm.interviewType = row.interviewType || '现场面试'
  arrangeForm.contactPerson = row.contactPerson || ''
  arrangeForm.contactPhone = row.contactPhone || ''
  arrangeForm.remark = ''
  arrangeDialogVisible.value = true
}

async function confirmArrange() {
  if (!arrangeFormRef.value) return

  await arrangeFormRef.value.validate(async (valid) => {
    if (!valid) return

    // 注意：根据后端实现，arrange接口会创建新的interview记录
    // 如果interview已存在（arrangeForm.id有值），可能需要调用更新接口
    // 但根据接口文档，只有arrange接口，所以这里先按创建处理
    // 如果interview已存在，可能需要先查询对应的applicationId
    if (!arrangeForm.applicationId) {
      ElMessage.warning('无法获取申请ID，请从职位申请页面安排面试')
      return
    }

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
    const res = await fetchInterviewList({
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      jobId: query.jobId || undefined,
      interviewStatus: query.interviewStatus !== '' ? query.interviewStatus : undefined
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
