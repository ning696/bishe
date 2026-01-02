<template>
  <div class="admin-complaint-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>投诉管理</span>
          <div>
            <el-button type="primary" link @click="fetchList">刷新</el-button>
          </div>
        </div>
      </template>

      <el-form :inline="true" :model="query" class="filter-form">
        <el-form-item label="投诉类型">
          <el-select
            v-model="query.complaintType"
            placeholder="全部"
            clearable
            style="width: 180px"
          >
            <el-option label="学生投诉企业" :value="1" />
            <el-option label="企业投诉学生" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select
            v-model="query.handleStatus"
            placeholder="全部"
            clearable
            style="width: 180px"
          >
            <el-option label="待处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="已处理" :value="2" />
            <el-option label="已关闭" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" border v-loading="loading">
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column prop="complaintTypeName" label="投诉类型" width="140" />
        <el-table-column prop="complainerName" label="投诉人" min-width="140" />
        <el-table-column prop="complainedName" label="被投诉对象" min-width="160" />
        <el-table-column prop="title" label="投诉标题" min-width="200" />
        <el-table-column label="处理状态" width="140">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.handleStatus)">
              {{ row.handleStatusName || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" min-width="170">
          <template #default="{ row }">
            {{ formatDateText(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openDetail(row.id)">
              详情
            </el-button>
            <el-button type="warning" link size="small" @click="openHandle(row)">
              处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="query.pageNum"
          :page-size="query.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @size-change="onSizeChange"
          @current-change="onCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog
      v-model="detailVisible"
      title="投诉详情"
      width="680px"
      :destroy-on-close="true"
    >
      <el-descriptions v-if="detail" :column="2" border>
        <el-descriptions-item label="投诉编号">
          {{ detail.id }}
        </el-descriptions-item>
        <el-descriptions-item label="投诉类型">
          {{ detail.complaintTypeName }}
        </el-descriptions-item>
        <el-descriptions-item label="投诉人">
          {{ detail.complainerName }}
        </el-descriptions-item>
        <el-descriptions-item label="被投诉对象">
          {{ detail.complainedName }}
        </el-descriptions-item>
        <el-descriptions-item label="关联职位">
          {{ detail.jobName || '--' }}
        </el-descriptions-item>
        <el-descriptions-item label="处理状态">
          {{ detail.handleStatusName }}
        </el-descriptions-item>
        <el-descriptions-item label="投诉标题" :span="2">
          {{ detail.title }}
        </el-descriptions-item>
        <el-descriptions-item label="投诉内容" :span="2">
          {{ detail.content }}
        </el-descriptions-item>
        <el-descriptions-item label="附件" :span="2">
          <el-link
            v-if="detail.attachment"
            type="primary"
            :href="detail.attachment"
            target="_blank"
          >
            查看附件
          </el-link>
          <span v-else>--</span>
        </el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">
          {{ detail.handleResult || '--' }}
        </el-descriptions-item>
        <el-descriptions-item label="处理备注" :span="2">
          {{ detail.handleRemark || '--' }}
        </el-descriptions-item>
        <el-descriptions-item label="处理时间">
          {{ formatDateText(detail.handleTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ formatDateText(detail.createTime) }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="handleVisible"
      title="处理投诉"
      width="520px"
      :destroy-on-close="true"
    >
      <el-form :model="handleForm" label-width="100px">
        <el-form-item label="处理状态" required>
          <el-select v-model="handleForm.handleStatus" placeholder="请选择处理状态">
            <el-option label="处理中" :value="1" />
            <el-option label="已处理" :value="2" />
            <el-option label="已关闭" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input
            v-model="handleForm.handleResult"
            type="textarea"
            :rows="3"
            placeholder="请输入处理结果"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="handleForm.handleRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" :loading="submittingHandle" @click="submitHandle">
          确认处理
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils'
import {
  fetchComplaintList,
  fetchComplaintDetail,
  handleComplaint,
  type ComplaintListItem,
  type ComplaintDetail,
  type ComplaintHandleParams
} from '@/api/admin/complaint'

interface ComplaintQuery {
  pageNum: number
  pageSize: number
  complaintType?: number
  handleStatus?: number
}

const loading = ref(false)
const list = ref<ComplaintListItem[]>([])
const total = ref(0)
const detailVisible = ref(false)
const handleVisible = ref(false)
const detail = ref<ComplaintDetail | null>(null)
const submittingHandle = ref(false)

const query = reactive<ComplaintQuery>({
  pageNum: 1,
  pageSize: 10,
  complaintType: undefined,
  handleStatus: undefined
})

const handleForm = reactive<ComplaintHandleParams>({
  complaintId: 0,
  handleStatus: 1,
  handleResult: '',
  handleRemark: ''
})

const formatDateText = (value?: string | null) => {
  if (!value) return '--'
  return formatDate(value, 'YYYY-MM-DD HH:mm')
}

const getStatusTag = (status?: number) => {
  switch (status) {
    case 0:
      return 'info'
    case 1:
      return 'warning'
    case 2:
      return 'success'
    case 3:
      return 'danger'
    default:
      return 'info'
  }
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await fetchComplaintList(query)
    list.value = res.rows || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取投诉列表失败:', error)
    ElMessage.error('获取投诉列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.pageNum = 1
  fetchList()
}

const resetQuery = () => {
  query.complaintType = undefined
  query.handleStatus = undefined
  handleSearch()
}

const onSizeChange = (size: number) => {
  query.pageSize = size
  query.pageNum = 1
  fetchList()
}

const onCurrentChange = (page: number) => {
  query.pageNum = page
  fetchList()
}

const openDetail = async (complaintId: number) => {
  try {
    const res = await fetchComplaintDetail(complaintId)
    if (res.code === 1000 && res.data) {
      detail.value = res.data
      detailVisible.value = true
    } else {
      ElMessage.error(res.msg || '获取投诉详情失败')
    }
  } catch (error) {
    console.error('获取投诉详情失败:', error)
    ElMessage.error('获取投诉详情失败')
  }
}

const openHandle = (row: ComplaintListItem) => {
  handleForm.complaintId = row.id
  handleForm.handleStatus =
    row.handleStatus && row.handleStatus >= 1 ? row.handleStatus : 1
  handleForm.handleResult = ''
  handleForm.handleRemark = ''
  handleVisible.value = true
}

const submitHandle = async () => {
  if (!handleForm.complaintId) {
    ElMessage.warning('投诉信息缺失')
    return
  }
  if (handleForm.handleStatus === 0) {
    ElMessage.warning('处理状态不能为待处理')
    return
  }

  submittingHandle.value = true
  try {
    const res = await handleComplaint(handleForm)
    if (res.code === 1000) {
      ElMessage.success('投诉处理成功')
      handleVisible.value = false
      fetchList()
    } else {
      ElMessage.error(res.msg || '投诉处理失败')
    }
  } catch (error) {
    console.error('投诉处理失败:', error)
    ElMessage.error('投诉处理失败')
  } finally {
    submittingHandle.value = false
  }
}

onMounted(fetchList)
</script>

<style scoped>
.admin-complaint-page {
  padding: 16px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.filter-form {
  margin-bottom: 16px;
}

.table-footer {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>

