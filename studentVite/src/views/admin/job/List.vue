<template>
  <div class="admin-job-list-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>职位列表</span>
        </div>
      </template>

      <el-form :inline="true" :model="query" class="mb12">
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 160px">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
            <el-option label="已下架" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="企业ID">
          <el-input-number v-model="query.enterpriseId" :min="1" placeholder="企业ID" />
        </el-form-item>
        <el-form-item label="职位名">
          <el-input v-model="query.jobName" placeholder="职位关键字" clearable style="width: 240px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="jobName" label="职位名称" min-width="160" />
        <el-table-column prop="enterpriseName" label="企业" min-width="160" />
        <el-table-column prop="city" label="城市" min-width="120" />
        <el-table-column label="薪资" min-width="140">
          <template #default="{ row }">
            <span v-if="row.salaryMin && row.salaryMax">{{ row.salaryMin }}k - {{ row.salaryMax }}k</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else type="info">已下架</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="openDetail(row)">详情</el-button>
            <el-button size="small" type="warning" link @click="openAudit(row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt12 flex-end">
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

    <JobDetailDrawer v-model:visible="detailVisible" :job-id="currentId" />
    <AuditDialog v-model:visible="auditVisible" :job-id="currentId" @success="fetchList" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { getJobPage, type JobQueryParams, type JobVO } from '@/api/admin/job'
import JobDetailDrawer from './components/JobDetailDrawer.vue'
import AuditDialog from './components/AuditDialog.vue'

const loading = ref(false)
const list = ref<JobVO[]>([])
const total = ref(0)
const query = reactive<JobQueryParams>({
  pageNum: 1,
  pageSize: 10,
  status: undefined,
  enterpriseId: undefined,
  jobName: ''
})

const detailVisible = ref(false)
const auditVisible = ref(false)
const currentId = ref<number | null>(null)

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getJobPage(query)
    list.value = res.rows || []
    total.value = res.total || 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.pageNum = 1
  fetchList()
}
const resetQuery = () => {
  query.status = undefined
  query.enterpriseId = undefined
  query.jobName = ''
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

const openDetail = (row: JobVO) => {
  currentId.value = row.id
  detailVisible.value = true
}
const openAudit = (row: JobVO) => {
  currentId.value = row.id
  auditVisible.value = true
}

onMounted(fetchList)
</script>

<style scoped>
.admin-job-list-page {
  padding: 16px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.mb12 { margin-bottom: 12px; }
.mt12 { margin-top: 12px; }
.flex-end {
  display: flex;
  justify-content: flex-end;
}
</style>


