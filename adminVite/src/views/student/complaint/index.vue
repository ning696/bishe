<template>
  <div class="student-complaint-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>我的投诉</span>
          <el-button type="primary" link @click="fetchList">
            刷新
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="query" class="filter-form">
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

      <el-table
        :data="list"
        border
        v-loading="loading"
        empty-text="暂无投诉记录"
      >
        <el-table-column prop="id" label="编号" width="90" />
        <el-table-column prop="enterpriseName" label="企业" min-width="160" />
        <el-table-column prop="jobName" label="职位" min-width="160" />
        <el-table-column prop="title" label="投诉标题" min-width="200" />
        <el-table-column label="投诉内容" min-width="220">
          <template #default="{ row }">
            <el-tooltip
              effect="dark"
              :content="row.content"
              placement="top"
              :disabled="!row.content"
            >
              <span class="text-ellipsis">{{ row.content || '--' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="处理状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.handleStatus)">
              {{ row.handleStatusName || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="处理结果" min-width="220">
          <template #default="{ row }">
            <el-tooltip
              effect="dark"
              :content="row.handleResult"
              placement="top"
              :disabled="!row.handleResult"
            >
              <span class="text-ellipsis">{{ row.handleResult || '--' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="附件" width="120">
          <template #default="{ row }">
            <el-link
              v-if="row.attachment"
              type="primary"
              :href="row.attachment"
              target="_blank"
            >
              查看附件
            </el-link>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" min-width="160">
          <template #default="{ row }">
            {{ formatDateText(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="处理时间" min-width="160">
          <template #default="{ row }">
            {{ formatDateText(row.handleTime) }}
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
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils'
import {
  fetchComplaintList,
  type ComplaintListItem,
  type ComplaintListQuery
} from '@/api/student/complaint'

const loading = ref(false)
const list = ref<ComplaintListItem[]>([])
const total = ref(0)

const query = reactive<ComplaintListQuery>({
  pageNum: 1,
  pageSize: 10,
  handleStatus: undefined
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
    if (res.code === 1000 && res.data) {
      list.value = res.data.rows || []
      total.value = res.data.total || 0
    } else {
      list.value = []
      total.value = 0
      ElMessage.error(res.msg || '获取投诉列表失败')
    }
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

onMounted(fetchList)
</script>

<style scoped>
.student-complaint-page {
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

.text-ellipsis {
  display: inline-block;
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}
</style>

