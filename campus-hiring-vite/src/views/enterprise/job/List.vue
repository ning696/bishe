<template>
  <div class="enterprise-job-list">
    <el-card class="query-card" shadow="never">
      <el-form :model="queryForm" :inline="true" label-width="80px">
        <el-form-item label="职位名称">
          <el-input
            v-model="queryForm.jobName"
            placeholder="请输入职位名称"
            clearable
            class="query-item"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryForm.status"
            placeholder="请选择状态"
            clearable
            class="query-item"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="handleAddJob">
            <el-icon><CirclePlus /></el-icon>
            发布职位
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
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
        empty-text="暂无职位，请点击发布"
      >
        <el-table-column label="职位名称" min-width="200">
          <template #default="{ row }">
            <div class="job-name" @click="handleView(row.id)">{{ row.jobName }}</div>
            <div class="job-sub">
              发布于：{{ row.publishTime ?? '未发布' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="工作地点" prop="workLocation" width="140" />
        <el-table-column label="薪资范围" width="160">
          <template #default="{ row }">
            {{ formatSalary(row) }}
          </template>
        </el-table-column>
        <el-table-column label="浏览/申请" width="150">
          <template #default="{ row }">
            <el-tag size="small" type="info">浏览 {{ row.viewCount ?? 0 }}</el-tag>
            <el-tag size="small" type="success" class="tag-gap">
              申请 {{ row.applyCount ?? 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">
              {{ row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row.id)">查看</el-button>
            <el-divider direction="vertical" />
            <el-button link type="info" @click="handleEdit(row.id)">编辑</el-button>
            <el-divider direction="vertical" />
            <el-button
              link
              type="warning"
              :disabled="row.status === 3"
              @click="handleOffline(row)"
            >
              下线
            </el-button>
            <el-divider direction="vertical" />
            <el-popconfirm
              title="确定删除该职位吗？"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
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
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  fetchEnterpriseJobList,
  deleteEnterpriseJob,
  offlineEnterpriseJob,
  type EnterpriseJobListItem
} from '@/api/enterprise/job'
import { Refresh, CirclePlus } from '@element-plus/icons-vue'

interface QueryForm {
  jobName: string
  status: number | null
}

const router = useRouter()
const loading = ref(false)
const jobList = ref<EnterpriseJobListItem[]>([])

const queryForm = reactive<QueryForm>({
  jobName: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const statusOptions = [
  { label: '待审核', value: 0 },
  { label: '已通过', value: 1 },
  { label: '已拒绝', value: 2 },
  { label: '已下线', value: 3 }
]

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

const buildQuery = () => {
  return {
    pageNum: pagination.pageNum,
    pageSize: pagination.pageSize,
    jobName: queryForm.jobName || undefined,
    status:
      queryForm.status === null || queryForm.status === undefined
        ? undefined
        : queryForm.status
  }
}

const loadJobs = async () => {
  loading.value = true
  try {
    const res = await fetchEnterpriseJobList(buildQuery())
    const tableData = res.data
    jobList.value = (tableData?.rows ?? []) as EnterpriseJobListItem[]
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
  queryForm.status = null
  pagination.pageNum = 1
  loadJobs()
}

const handleAddJob = () => {
  router.push({ path: '/enterprise/job/add' })
}

const handleView = (jobId: number) => {
  router.push({ path: `/enterprise/job/${jobId}` })
}

const handleEdit = (jobId: number) => {
  router.push({ path: `/enterprise/job/edit/${jobId}` })
}

const handleOffline = async (row: EnterpriseJobListItem) => {
  try {
    await offlineEnterpriseJob(row.id)
    ElMessage.success('职位已下线')
    loadJobs()
  } catch (error) {
    console.error('职位下线失败', error)
  }
}

const handleDelete = async (row: EnterpriseJobListItem) => {
  try {
    await deleteEnterpriseJob(row.id)
    ElMessage.success('删除成功')
    loadJobs()
  } catch (error) {
    console.error('删除职位失败', error)
  }
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  loadJobs()
}

const handlePageChange = (page: number) => {
  pagination.pageNum = page
  loadJobs()
}

const formatSalary = (row: EnterpriseJobListItem) => {
  const min = row.salaryMin ?? 0
  const max = row.salaryMax ?? 0
  if (!min && !max) return '面议'
  if (min && !max) return `${min} 起`
  if (!min && max) return `${max} 以下`
  return `${min} - ${max} ${row.salaryType ?? ''}`.trim()
}

onMounted(() => {
  loadJobs()
})
</script>

<style scoped lang="scss">
.enterprise-job-list {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.query-card {
  .query-item {
    width: 200px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  padding-top: 12px;
}

.job-name {
  font-weight: 600;
  color: #409eff;
  cursor: pointer;
}

.job-sub {
  color: #909399;
  font-size: 12px;
}

.tag-gap {
  margin-left: 6px;
}
</style>

