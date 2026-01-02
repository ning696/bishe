<template>
  <div class="enterprise-job-edit">
    <el-page-header content="编辑职位" @back="router.back()" />
    <JobForm
      mode="edit"
      :loading="loading"
      :submit-loading="submitLoading"
      :initial-data="initialData"
      @submit="handleSubmit"
      @cancel="router.back()"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import JobForm from './components/JobForm.vue'
import {
  fetchEnterpriseJobDetail,
  fetchJobCampusRelations,
  updateEnterpriseJob,
  type EnterpriseJobPayload,
  type EnterpriseJobDetail
} from '@/api/enterprise/job'

const route = useRoute()
const router = useRouter()

const jobId = computed(() => Number(route.params.id))
const loading = ref(false)
const submitLoading = ref(false)
const initialData = ref<(EnterpriseJobDetail & { campusIds?: number[] }) | null>(null)

const loadDetail = async () => {
  if (!jobId.value) {
    ElMessage.error('未获取到职位ID')
    router.back()
    return
  }
  loading.value = true
  try {
    const [detailRes, campusRes] = await Promise.all([
      fetchEnterpriseJobDetail(jobId.value),
      fetchJobCampusRelations(jobId.value)
    ])
    const campusIds = (campusRes.data ?? []).map((item) => item.campusId)
    initialData.value = {
      ...detailRes.data,
      campusIds
    }
  } catch (error) {
    console.error('加载职位详情失败', error)
  } finally {
    loading.value = false
  }
}

const handleSubmit = async (payload: EnterpriseJobPayload) => {
  if (!jobId.value) return
  submitLoading.value = true
  try {
    await updateEnterpriseJob({
      ...payload,
      jobId: jobId.value
    })
    ElMessage.success('职位更新成功')
    router.push({ path: '/enterprise/job' })
  } catch (error) {
    console.error('更新职位失败', error)
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped lang="scss">
.enterprise-job-edit {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>

