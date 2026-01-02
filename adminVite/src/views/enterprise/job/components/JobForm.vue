<template>
  <el-form
    ref="formRef"
    :model="form"
    :rules="rules"
    label-width="110px"
    class="job-form"
    v-loading="loading"
  >
    <el-card shadow="never">
      <template #header>
        <div class="section-header">
          <span></span>
        </div>
      </template>
      <el-row :gutter="16">
        <el-col :md="12" :sm="24">
          <el-form-item label="职位名称" prop="jobName">
            <el-input v-model="form.jobName" placeholder="请输入职位名称" />
          </el-form-item>
        </el-col>
        <el-col :md="12" :sm="24">
          <el-form-item label="职位类别">
            <el-input-number
              v-model="form.categoryId"
              :min="0"
              placeholder="请输入类别ID"
              class="full-width"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :md="12" :sm="24">
          <el-form-item label="工作地点" prop="workLocation">
            <el-input v-model="form.workLocation" placeholder="请输入工作地点" />
          </el-form-item>
        </el-col>
        <el-col :md="12" :sm="24">
          <el-form-item label="招聘人数">
            <el-input-number
              v-model="form.recruitCount"
              :min="1"
              placeholder="请输入人数"
              class="full-width"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :md="12" :sm="24">
          <el-form-item label="薪资下限">
            <el-input-number
              v-model="form.salaryMin"
              :min="0"
              :max="form.salaryMax ?? undefined"
              :step="1000"
              class="full-width"
              placeholder="请输入最低薪资"
            />
          </el-form-item>
        </el-col>
        <el-col :md="12" :sm="24">
          <el-form-item label="薪资上限">
            <el-input-number
              v-model="form.salaryMax"
              :min="form.salaryMin ?? 0"
              :step="1000"
              class="full-width"
              placeholder="请输入最高薪资"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :md="12" :sm="24">
          <el-form-item label="薪资类型">
            <el-select v-model="form.salaryType" placeholder="请选择" clearable>
              <el-option label="面议" value="面议" />
              <el-option label="月薪" value="月薪" />
              <el-option label="年薪" value="年薪" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :md="12" :sm="24">
          <el-form-item label="工作类型">
            <el-select v-model="form.jobType" placeholder="请选择" clearable>
              <el-option label="全职" value="全职" />
              <el-option label="兼职" value="兼职" />
              <el-option label="实习" value="实习" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :md="12" :sm="24">
          <el-form-item label="学历要求">
            <el-select v-model="form.requiredEducation" placeholder="请选择" clearable>
              <el-option label="专科" value="专科" />
              <el-option label="本科" value="本科" />
              <el-option label="硕士" value="硕士" />
              <el-option label="博士" value="博士" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :md="12" :sm="24">
          <el-form-item label="经验（月）">
            <el-input-number
              v-model="form.requiredExperience"
              :min="0"
              :step="6"
              class="full-width"
              placeholder="请输入经验（月）"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :md="12" :sm="24">
          <el-form-item label="截止时间">
            <el-date-picker
              v-model="form.expireTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择截止时间"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :md="12" :sm="24">
          <el-form-item label="关联校园">
            <el-select
              v-model="form.campusIds"
              multiple
              filterable
              placeholder="请选择校园"
              :loading="campusLoading"
            >
              <el-option
                v-for="item in campusOptions"
                :key="item.id"
                :label="item.campusName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <div class="section-header">
          <span>详细描述</span>
        </div>
      </template>
      <el-form-item label="职位描述" prop="jobDescription">
        <el-input
          v-model="form.jobDescription"
          type="textarea"
          :autosize="{ minRows: 6, maxRows: 10 }"
          placeholder="请描述岗位职责、任职条件等信息"
        />
      </el-form-item>
      <el-form-item label="专业要求">
        <el-input
          v-model="form.requiredMajor"
          placeholder="请输入专业要求（可选）"
        />
      </el-form-item>
      <el-form-item label="技能要求">
        <el-input
          v-model="form.requiredSkills"
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 6 }"
          placeholder="请输入技能要求，多个技能请用逗号分隔"
        />
      </el-form-item>
    </el-card>

    <div class="form-actions">
      <el-button @click="emit('cancel')">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        保存
      </el-button>
    </div>
  </el-form>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { EnterpriseJobPayload, EnterpriseJobDetail, CampusItem } from '@/api/enterprise/job'
import { fetchCampusList } from '@/api/enterprise/job'

type JobFormModel = EnterpriseJobPayload & { categoryId?: number | null }

const props = defineProps<{
  mode: 'create' | 'edit'
  loading: boolean
  submitLoading: boolean
  initialData?: (EnterpriseJobDetail & { campusIds?: number[] }) | null
}>()

const emit = defineEmits<{
  (e: 'submit', payload: JobFormModel): void
  (e: 'cancel'): void
}>()

const formRef = ref<FormInstance>()
const form = reactive<JobFormModel>({
  categoryId: null,
  jobName: '',
  jobDescription: '',
  requiredMajor: '',
  requiredSkills: '',
  requiredEducation: '',
  requiredExperience: null,
  workLocation: '',
  salaryMin: null,
  salaryMax: null,
  salaryType: '',
  jobType: '',
  recruitCount: null,
  expireTime: '',
  campusIds: []
})

const campusOptions = ref<CampusItem[]>([])
const campusLoading = ref(false)

const rules: FormRules<JobFormModel> = {
  jobName: [{ required: true, message: '请输入职位名称', trigger: 'blur' }],
  workLocation: [{ required: true, message: '请输入工作地点', trigger: 'blur' }],
  jobDescription: [{ required: true, message: '请输入职位描述', trigger: 'blur' }]
}

const populateForm = (detail: EnterpriseJobDetail & { campusIds?: number[] }) => {
  form.categoryId = detail.categoryId ?? null
  form.jobName = detail.jobName ?? ''
  form.jobDescription = detail.jobDescription ?? ''
  form.requiredMajor = detail.requiredMajor ?? ''
  form.requiredSkills = detail.requiredSkills ?? ''
  form.requiredEducation = detail.requiredEducation ?? ''
  form.requiredExperience = detail.requiredExperience ?? null
  form.workLocation = detail.workLocation ?? ''
  form.salaryMin = detail.salaryMin ?? null
  form.salaryMax = detail.salaryMax ?? null
  form.salaryType = detail.salaryType ?? ''
  form.jobType = detail.jobType ?? ''
  form.recruitCount = detail.recruitCount ?? null
  form.expireTime = detail.expireTime ?? ''
  form.campusIds = detail.campusIds ?? []
}

watch(
  () => props.initialData,
  (val) => {
    if (val) {
      populateForm(val)
    } else {
      // reset form when initial data cleared
      form.categoryId = null
      form.jobName = ''
      form.jobDescription = ''
      form.requiredMajor = ''
      form.requiredSkills = ''
      form.requiredEducation = ''
      form.requiredExperience = null
      form.workLocation = ''
      form.salaryMin = null
      form.salaryMax = null
      form.salaryType = ''
      form.jobType = ''
      form.recruitCount = null
      form.expireTime = ''
      form.campusIds = []
    }
  },
  { immediate: true }
)

const loadCampusOptions = async () => {
  campusLoading.value = true
  try {
    const res = await fetchCampusList({ pageNum: 1, pageSize: 500 })
    campusOptions.value = (res.data?.rows ?? []) as CampusItem[]
  } catch (error) {
    console.error('加载校园列表失败', error)
  } finally {
    campusLoading.value = false
  }
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (!valid) return
    const payload: JobFormModel = {
      categoryId: form.categoryId ?? undefined,
      jobName: form.jobName,
      jobDescription: form.jobDescription,
      requiredMajor: form.requiredMajor || undefined,
      requiredSkills: form.requiredSkills || undefined,
      requiredEducation: form.requiredEducation || undefined,
      requiredExperience: form.requiredExperience ?? undefined,
      workLocation: form.workLocation,
      salaryMin: form.salaryMin ?? undefined,
      salaryMax: form.salaryMax ?? undefined,
      salaryType: form.salaryType || undefined,
      jobType: form.jobType || undefined,
      recruitCount: form.recruitCount ?? undefined,
      expireTime: form.expireTime || undefined,
      campusIds: form.campusIds?.length ? [...form.campusIds] : undefined
    }
    emit('submit', payload)
  })
}

onMounted(() => {
  loadCampusOptions()
})
</script>

<style scoped lang="scss">
.job-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.section-header {
  font-weight: 600;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-bottom: 24px;
}

.full-width {
  width: 100%;
}
</style>

