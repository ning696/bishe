<template>
  <div class="student-application-list">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>面试申请</span>
          <div class="actions">
            <el-select v-model="query.status" placeholder="状态筛选" clearable style="width: 140px" @change="handleSearch">
              <el-option label="待安排" :value="0" />
              <el-option label="已安排" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已取消" :value="3" />
            </el-select>
            <el-button type="primary" link @click="handleSearch">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>
      <el-table
        v-loading="loading"
        :data="rows"
        border
        empty-text="暂无申请记录"
      >
        <el-table-column label="职位信息" min-width="220">
          <template #default="{ row }">
            <div class="job-info">
              <div class="job-name">{{ row.jobName }}</div>
              <div class="enterprise-name">{{ row.enterpriseName }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="statusType(row.interviewStatus)">
              {{ row.interviewStatusName ?? '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="面试时间" prop="interviewTime" width="180" />
        <el-table-column label="面试地点" prop="interviewLocation" min-width="200" />
        <el-table-column label="面试类型" prop="interviewType" width="150" />
        <el-table-column label="备注" prop="remark" min-width="220" />
        <el-table-column label="申请时间" prop="createTime" width="180" />
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :page-sizes="[10, 20, 50]"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive, watch } from 'vue'
import { fetchInterviewApplicationsPage, type InterviewApplicationItem } from '@/api/student/interview'
import { Refresh } from '@element-plus/icons-vue'

const loading = ref(false)
const rows = ref<InterviewApplicationItem[]>([])
const total = ref(0)
const query = reactive({
  pageNum: 1,
  pageSize: 10,
  status: undefined as number | undefined
})

const statusType = (status?: number | null) => {
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

const fetchData = async () => {
  loading.value = true
  try {
    const res = await fetchInterviewApplicationsPage({
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      status: query.status
    })
    rows.value = res.rows ?? []
    total.value = res.total ?? 0
  } catch (error) {
    console.error('加载申请记录失败', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.pageNum = 1
  fetchData()
}

watch(() => [query.pageNum, query.pageSize], fetchData)
onMounted(fetchData)
</script>

<style scoped lang="scss">
.student-application-list {
  padding: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.job-info {
  display: flex;
  flex-direction: column;
  gap: 4px;

  .job-name {
    font-weight: 600;
  }

  .enterprise-name {
    color: #888;
    font-size: 12px;
  }
}
</style>

