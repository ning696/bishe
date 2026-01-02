<template>
  <el-drawer v-model="visibleLocal" title="职位详情" size="560px" :destroy-on-close="true">
    <el-skeleton v-if="loading" :rows="6" animated />
    <div v-else-if="detail" class="detail">
      <div class="title">
        <div class="name">{{ detail.jobName }}</div>
        <div class="enterprise">{{ detail.enterpriseName }}</div>
      </div>
      <el-descriptions :column="2" border class="mt12">
        <el-descriptions-item label="职位ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="企业ID">{{ detail.enterpriseId }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ detail.city || '-' }}</el-descriptions-item>
        <el-descriptions-item label="薪资">
          <span v-if="detail.salaryMin && detail.salaryMax">{{ detail.salaryMin }}k - {{ detail.salaryMax }}k</span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail.status === 0" type="warning">待审核</el-tag>
          <el-tag v-else-if="detail.status === 1" type="success">已通过</el-tag>
          <el-tag v-else-if="detail.status === 2" type="danger">已拒绝</el-tag>
          <el-tag v-else type="info">已下架</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div class="section">
        <div class="label">岗位描述</div>
        <div class="content">{{ detail.description || '-' }}</div>
      </div>
      <div class="section">
        <div class="label">任职要求</div>
        <div class="content">{{ detail.requirement || '-' }}</div>
      </div>
    </div>
    <div v-else class="empty">暂无数据</div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { getJobDetail, type JobDetailVO } from '@/api/admin/job'

const props = defineProps<{
  visible: boolean
  jobId: number | null
}>()
const emit = defineEmits<{
  (e: 'update:visible', v: boolean): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const loading = ref(false)
const detail = ref<JobDetailVO | null>(null)

const fetch = async (id: number) => {
  loading.value = true
  try {
    const res = await getJobDetail({ id })
    detail.value = res.data
  } finally {
    loading.value = false
  }
}

watch(() => props.jobId, (id) => {
  if (visibleLocal.value && id) fetch(id)
}, { immediate: true })

watch(visibleLocal, (open) => {
  if (open && props.jobId) fetch(props.jobId)
})
</script>

<style scoped>
.title .name {
  font-size: 18px;
  font-weight: 600;
}
.title .enterprise {
  color: #666;
}
.section {
  margin-top: 16px;
}
.label {
  color: #666;
  margin-bottom: 6px;
}
.content {
  white-space: pre-wrap;
  line-height: 1.6;
}
.empty {
  color: #999;
}
.mt12 { margin-top: 12px; }
</style>


