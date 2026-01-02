<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>职位申请</span>
        </div>
      </template>
      <el-skeleton :loading="loading" animated>
        <template #template>
          <el-skeleton-item variant="text" style="width: 40%; margin-bottom: 12px;" />
          <el-skeleton-item variant="rect" style="height: 300px;" />
        </template>
        <template #default>
          <el-table :data="rows" v-loading="loading" style="width: 100%">
            <el-table-column prop="jobName" label="职位" min-width="160" />
            <el-table-column prop="enterpriseName" label="企业" min-width="160" />
            <el-table-column prop="applicationStatusName" label="状态" min-width="120">
              <template #default="{ row }">
                <el-tag :type="statusTagType(row.applicationStatus)">{{ row.applicationStatusName }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="applicationTime" label="申请时间" min-width="180" />
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
        </template>
      </el-skeleton>
      <el-empty v-if="!loading && rows.length === 0" description="暂无申请记录" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import type { PersonalApplicationItem } from '@/api/student/personal'

const loading = ref(false)
const rows = ref<PersonalApplicationItem[]>([])
const total = ref(0)
const query = reactive({
  pageNum: 1,
  pageSize: 10
})

function statusTagType(status?: number) {
  if (status === 1) return 'success' // 已通过
  if (status === 2) return 'danger' // 已拒绝
  if (status === 3) return 'info' // 已取消
  return ''
}

async function fetchData() {
  loading.value = true
  try {
    const { fetchPersonalApplicationList } = await import('@/api/student/personal')
    const res = await fetchPersonalApplicationList({
      pageNum: query.pageNum,
      pageSize: query.pageSize
    })
    rows.value = res.data?.rows ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

watch(() => [query.pageNum, query.pageSize], fetchData)
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
.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>


