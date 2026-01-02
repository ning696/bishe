<template>
  <div class="student-resume-delivery" v-loading="loading">
    <el-page-header content="投递简历" @back="router.back()" />

    <el-card shadow="never">
      <el-form label-width="100px" class="delivery-form">
        <el-form-item label="职位">
          <div class="job-field">
            <el-input-number v-model="jobId" :min="1" :step="1" />
            <el-button type="primary" link @click="goJobList">去选择职位</el-button>
            <el-tag v-if="jobName" type="info">{{ jobName }}</el-tag>
          </div>
        </el-form-item>
        <el-form-item label="选择简历">
          <el-select
            v-model="resumeId"
            placeholder="请选择简历"
            filterable
            clearable
            :loading="resumeLoading"
          >
            <el-option
              v-for="item in resumeOptions"
              :key="item.id"
              :label="item.resumeName"
              :value="item.id"
            >
              <div class="resume-option">
                <span>{{ item.resumeName }}</span>
                <el-tag v-if="item.isDefault === 1" size="small" type="success">默认</el-tag>
              </div>
            </el-option>
          </el-select>
          <el-button class="ml8" link type="primary" @click="goResumeList">管理简历</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" :disabled="!canSubmit" @click="submit">
            确认投递
          </el-button>
          <el-button @click="router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { fetchResumeList, deliveryResume, type ResumeListItem } from '@/api/student/resume'
import { fetchStudentJobDetail } from '@/api/student/job'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const submitting = ref(false)

const jobId = ref<number | null>(null)
const jobName = ref<string>('')
const resumeId = ref<number | null>(null)
const resumeOptions = ref<ResumeListItem[]>([])
const resumeLoading = ref(false)

const canSubmit = computed(() => Boolean(jobId.value && resumeId.value))

const goJobList = () => {
  router.push('/student/jobs')
}

const goResumeList = () => {
  router.push('/student/resume')
}

const loadResumes = async () => {
  resumeLoading.value = true
  try {
    const res = await fetchResumeList()
    resumeOptions.value = res.data ?? []
    // 优先默认简历
    const def = resumeOptions.value.find(r => r.isDefault === 1)
    if (def) {
      resumeId.value = def.id
    }
  } catch (e) {
    console.error(e)
  } finally {
    resumeLoading.value = false
  }
}

const loadJobName = async (id: number) => {
  try {
    const res = await fetchStudentJobDetail(id)
    jobName.value = res.data?.jobName || ''
  } catch {
    jobName.value = ''
  }
}

const submit = async () => {
  if (!canSubmit.value || !jobId.value || !resumeId.value) return
  submitting.value = true
  try {
    await deliveryResume({ jobId: jobId.value, resumeId: resumeId.value })
    ElMessage.success('投递成功')
    router.push('/student/interview/application')
  } catch (e) {
    console.error(e)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  const qJobId = Number(route.query.jobId)
  if (!Number.isNaN(qJobId) && qJobId > 0) {
    jobId.value = qJobId
    loadJobName(qJobId)
  }
  loadResumes()
})

watch(jobId, (v) => {
  if (v) loadJobName(v)
})
</script>

<style scoped lang="scss">
.student-resume-delivery {
  padding: 16px;
}
.delivery-form {
  max-width: 640px;
}
.job-field {
  display: flex;
  align-items: center;
  gap: 12px;
}
.resume-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}
.ml8 {
  margin-left: 8px;
}
</style>





