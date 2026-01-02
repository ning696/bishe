<template>
  <div class="enterprise-job-add">
    <el-page-header content="发布职位" @back="router.back()" />
    <JobForm
      mode="create"
      :loading="false"
      :submit-loading="submitLoading"
      @submit="handleSubmit"
      @cancel="router.back()"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import JobForm from './components/JobForm.vue'
import { createEnterpriseJob, type EnterpriseJobPayload } from '@/api/enterprise/job'

const router = useRouter()
const submitLoading = ref(false)

const handleSubmit = async (payload: EnterpriseJobPayload) => {
  submitLoading.value = true
  try {
    await createEnterpriseJob(payload)
    ElMessage.success('职位发布成功')
    router.push({ path: '/enterprise/job' })
  } catch (error) {
    console.error('职位发布失败', error)
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.enterprise-job-add {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>

