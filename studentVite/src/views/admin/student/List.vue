<template>
  <div class="admin-student-list-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>学生列表</span>
        </div>
      </template>

      <el-form :inline="true" :model="query" class="mb12">
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 160px">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="2" />
            <el-option label="待审核" :value="3" />
            <el-option label="已拉黑" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="用户名/昵称/手机号" clearable style="width: 240px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="nickName" label="昵称" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="statusName" label="状态" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">禁用</el-tag>
            <el-tag v-else-if="row.status === 3" type="warning">待审核</el-tag>
            <el-tag v-else type="info">已拉黑</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" min-width="160">
          <template #default="{ row }">
            {{ formatDateOnly(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="openDetail(row)">详情</el-button>
            <el-button size="small" type="warning" link @click="openUpdateStatus(row)">状态</el-button>
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

    <StudentDetailDrawer
      v-model:visible="detailVisible"
      :student-id="currentId"
    />
    <StudentStatusDialog
      v-model:visible="statusVisible"
      :student-id="currentId"
      :current-status="currentStatus"
      @success="fetchList"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { formatDate } from '@/utils'
import { fetchStudentList, type StudentQueryParams, type StudentListItem } from '@/api/admin/student'
import StudentDetailDrawer from './components/StudentDetailDrawer.vue'
import StudentStatusDialog from './components/StudentStatusDialog.vue'

const loading = ref(false)
const list = ref<StudentListItem[]>([])
const total = ref(0)
const query = reactive<StudentQueryParams>({
  pageNum: 1,
  pageSize: 10,
  status: undefined,
  keyword: ''
})

const detailVisible = ref(false)
const statusVisible = ref(false)
const currentId = ref<number | null>(null)
const currentStatus = ref<number | null>(null)

const formatDateOnly = (value?: string | number | Date | null) => {
  return value ? formatDate(value, 'YYYY-MM-DD') : ''
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await fetchStudentList(query)
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
  query.keyword = ''
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

const openDetail = (row: StudentListItem) => {
  currentId.value = row.id
  detailVisible.value = true
}

const openUpdateStatus = (row: StudentListItem) => {
  currentId.value = row.id
  currentStatus.value = row.status ?? null
  statusVisible.value = true
}

onMounted(fetchList)
</script>

<style scoped>
.admin-student-list-page {
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

