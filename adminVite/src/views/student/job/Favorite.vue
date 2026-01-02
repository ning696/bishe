<template>
  <div class="student-job-favorite">
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>我的收藏</span>
          <el-button type="primary" link @click="loadFavorites">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      <el-table
        v-loading="loading"
        :data="favoriteList"
        border
        empty-text="暂无收藏"
      >
        <el-table-column label="职位名称" min-width="200">
          <template #default="{ row }"> 
            <div class="job-info">
              <div class="job-name" @click="handleViewDetail(row.id)">
                {{ row.jobName }}
              </div>
              <div class="job-enterprise">{{ row.enterpriseName }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="地点" prop="workLocation" width="140" />
        <el-table-column label="薪资范围" width="160">
          <template #default="{ row }">
            {{ formatSalary(row) }}
          </template>
        </el-table-column>
        <el-table-column label="收藏时间" prop="favoriteTime" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleViewDetail(row.id)">
              查看
            </el-button>
            <el-divider direction="vertical" />
            <el-button
              link
              type="warning"
              :loading="cancelLoadingId === row.id"
              @click="handleCancelFavorite(row)"
            >
              取消收藏
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
  fetchStudentFavoriteJobList,
  unfavoriteStudentJob,
  type FavoriteJobListItem
} from '@/api/student/job'
import { Refresh } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const favoriteList = ref<FavoriteJobListItem[]>([])
const cancelLoadingId = ref<number | null>(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const loadFavorites = async () => {
  loading.value = true
  try {
    const res = await fetchStudentFavoriteJobList({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    const tableData = res.data
    favoriteList.value = (tableData?.rows ?? []) as FavoriteJobListItem[]
    pagination.total = Number(tableData?.total ?? 0)
  } catch (error) {
    console.error('加载收藏列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  loadFavorites()
}

const handlePageChange = (page: number) => {
  pagination.pageNum = page
  loadFavorites()
}

const handleViewDetail = (jobId: number) => {
  router.push({ path: `/student/job/${jobId}` })
}

const handleCancelFavorite = async (row: FavoriteJobListItem) => {
  cancelLoadingId.value = row.id
  try {
    await unfavoriteStudentJob(row.id)
    ElMessage.success('已取消收藏')
    loadFavorites()
  } catch (error) {
    console.error('取消收藏失败', error)
  } finally {
    cancelLoadingId.value = null
  }
}

const formatSalary = (row: FavoriteJobListItem) => {
  const min = row.salaryMin ?? 0
  const max = row.salaryMax ?? 0
  if (!min && !max) return '面议'
  if (min && !max) return `${min} 起`
  if (!min && max) return `${max} 以下`
  return `${min} - ${max} ${row.salaryType ?? ''}`.trim()
}

onMounted(() => {
  loadFavorites()
})
</script>

<style scoped lang="scss">
.student-job-favorite {
  padding: 16px;
}

.table-card {
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
}

.job-info {
  display: flex;
  flex-direction: column;
  gap: 4px;

  .job-name {
    font-weight: 600;
    color: #409eff;
    cursor: pointer;
  }

  .job-enterprise {
    color: #888;
    font-size: 12px;
  }
}
</style>

