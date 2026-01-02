<template>
  <div class="student-resume-edit" v-loading="loading">
    <el-page-header
      :content="isEdit ? '编辑简历' : '新建简历'"
      @back="router.back()"
    />
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="110px"
      class="resume-form"
    >
      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>基础信息</span>
          </div>
        </template>
        <el-row :gutter="16">
          <el-col :md="12" :sm="24">
            <el-form-item label="简历名称" prop="resumeName">
              <el-input v-model="form.resumeName" placeholder="请输入简历名称" />
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item label="简历文件">
              <div class="resume-file-field">
                <el-upload
                  class="resume-upload"
                  action=""
                  :auto-upload="false"
                  :limit="1"
                  :file-list="resumeFileList"
                  :on-change="handleResumeFileChange"
                  :on-remove="handleResumeFileRemove"
                  :on-exceed="handleResumeFileExceed"
                  accept=".pdf,.doc,.docx"
                >
                  <el-button type="primary">选择文件</el-button>
                </el-upload>
                <div class="resume-file-info">
                  <el-text v-if="resumeFileList.length" size="small" type="info">
                    已选择：{{ resumeFileList[0].name }}
                  </el-text>
                  <template v-else-if="form.resumeFile">
                    <el-link :href="form.resumeFile" target="_blank" type="primary" :underline="false">
                      查看已上传文件
                    </el-link>
                    <el-button type="danger" link @click="clearExistingResumeFile">
                      移除链接
                    </el-button>
                  </template>
                  <el-text size="small" type="info">支持 PDF、Word 等常见文件格式</el-text>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :md="12" :sm="24">
            <el-form-item label="姓名" prop="personalInfo.name">
              <el-input v-model="form.personalInfo.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item label="联系电话">
              <el-input
                v-model="form.personalInfo.phone"
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :md="12" :sm="24">
            <el-form-item label="邮箱">
              <el-input
                v-model="form.personalInfo.email"
                placeholder="请输入电子邮箱"
              />
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item label="性别">
              <el-select v-model="form.personalInfo.gender" placeholder="请选择性别" clearable>
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :md="12" :sm="24">
            <el-form-item label="出生日期">
              <el-date-picker
                v-model="form.personalInfo.birthday"
                type="date"
                placeholder="请选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item label="默认简历">
              <el-switch v-model="form.isDefault" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>教育经历</span>
            <el-button type="primary" link @click="addEducation">
              <el-icon><CirclePlus /></el-icon>
              新增
            </el-button>
          </div>
        </template>
        <div v-if="form.educationBackground.length === 0" class="empty-tip">
          当前暂无教育经历，请点击“新增”添加
        </div>
        <div
          v-for="(item, index) in form.educationBackground"
          :key="index"
          class="dynamic-item"
        >
          <div class="dynamic-item__header">
            <span>教育经历 {{ index + 1 }}</span>
            <el-button
              v-if="form.educationBackground.length > 1"
              type="danger"
              link
              @click="removeEducation(index)"
            >
              删除
            </el-button>
          </div>
          <el-row :gutter="16">
            <el-col :md="12" :sm="24">
              <el-form-item :label="`学校`">
                <el-input v-model="item.school" placeholder="学校名称" />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="24">
              <el-form-item label="专业">
                <el-input v-model="item.major" placeholder="专业名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :md="12" :sm="24">
              <el-form-item label="学历">
                <el-select v-model="item.education" placeholder="请选择" clearable>
                  <el-option label="专科" value="专科" />
                  <el-option label="本科" value="本科" />
                  <el-option label="硕士" value="硕士" />
                  <el-option label="博士" value="博士" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="24">
              <el-form-item label="起止时间">
                <el-date-picker
                  v-model="item.duration"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  value-format="YYYY-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>工作经历</span>
            <el-button type="primary" link @click="addWork">
              <el-icon><CirclePlus /></el-icon>
              新增
            </el-button>
          </div>
        </template>
        <div v-if="form.workExperience.length === 0" class="empty-tip">
          当前暂无工作经历，可根据需要添加
        </div>
        <div
          v-for="(item, index) in form.workExperience"
          :key="index"
          class="dynamic-item"
        >
          <div class="dynamic-item__header">
            <span>工作经历 {{ index + 1 }}</span>
            <el-button
              type="danger"
              link
              @click="removeWork(index)"
            >
              删除
            </el-button>
          </div>
          <el-row :gutter="16">
            <el-col :md="12" :sm="24">
              <el-form-item label="公司">
                <el-input v-model="item.company" placeholder="公司/组织" />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="24">
              <el-form-item label="职位">
                <el-input v-model="item.position" placeholder="职位名称" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :md="12" :sm="24">
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="item.duration"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  value-format="YYYY-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="24">
              <el-form-item label="描述">
                <el-input
                  v-model="item.description"
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 5 }"
                  placeholder="主要职责和成果"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>项目经历</span>
            <el-button type="primary" link @click="addProject">
              <el-icon><CirclePlus /></el-icon>
              新增
            </el-button>
          </div>
        </template>
        <div v-if="form.projectExperience.length === 0" class="empty-tip">
          当前暂无项目经历，可根据需要添加
        </div>
        <div
          v-for="(item, index) in form.projectExperience"
          :key="index"
          class="dynamic-item"
        >
          <div class="dynamic-item__header">
            <span>项目经历 {{ index + 1 }}</span>
            <el-button type="danger" link @click="removeProject(index)">
              删除
            </el-button>
          </div>
          <el-row :gutter="16">
            <el-col :md="12" :sm="24">
              <el-form-item label="项目名称">
                <el-input v-model="item.projectName" placeholder="项目名称" />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="24">
              <el-form-item label="角色">
                <el-input v-model="item.role" placeholder="担任角色" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="16">
            <el-col :md="12" :sm="24">
              <el-form-item label="时间范围">
                <el-date-picker
                  v-model="item.duration"
                  type="monthrange"
                  range-separator="至"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  value-format="YYYY-MM"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="24">
              <el-form-item label="项目描述">
                <el-input
                  v-model="item.description"
                  type="textarea"
                  :autosize="{ minRows: 2, maxRows: 5 }"
                  placeholder="项目内容、职责、成果"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="section-header">
            <span>技能与自我评价</span>
          </div>
        </template>
        <el-form-item label="技能标签">
          <el-input
            v-model="form.skills"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="例如：Java、Spring、MySQL（多个技能请使用逗号分隔）"
          />
        </el-form-item>
        <el-form-item label="自我介绍">
          <el-input
            v-model="form.selfIntroduction"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }"
            placeholder="简要介绍您的优势、职业目标等"
          />
        </el-form-item>
      </el-card>

      <div class="form-actions">
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          保存
        </el-button>
        <el-button @click="router.back()">取消</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ElMessage,
  type FormInstance,
  type FormRules,
  type UploadProps,
  type UploadUserFile
} from 'element-plus'
import {
  fetchResumeDetail,
  createResume,
  updateResume,
  type ResumeDetail
} from '@/api/student/resume'
import { uploadStudentFile } from '@/api/student/student'
import { CirclePlus } from '@element-plus/icons-vue'

interface PersonalInfo {
  name: string
  phone: string
  email: string
  gender: string
  birthday: string
}

interface DurationRange {
  startTime?: string
  endTime?: string
  duration?: string[]
}

interface EducationItem extends DurationRange {
  school: string
  major: string
  education: string
}

interface WorkItem extends DurationRange {
  company: string
  position: string
  description: string
}

interface ProjectItem extends DurationRange {
  projectName: string
  role: string
  description: string
}

interface ResumeForm {
  resumeName: string
  resumeFile: string
  isDefault: boolean
  personalInfo: PersonalInfo
  educationBackground: EducationItem[]
  workExperience: WorkItem[]
  projectExperience: ProjectItem[]
  skills: string
  selfIntroduction: string
}

const route = useRoute()
const router = useRouter()

const resumeId = computed(() => {
  const id = route.params.id
  return id ? Number(id) : null
})

const isEdit = computed(() => Boolean(resumeId.value))
const loading = ref(false)
const submitLoading = ref(false)
const resumeFileList = ref<UploadUserFile[]>([])
const selectedResumeFile = ref<File | null>(null)

const formRef = ref<FormInstance>()
const defaultPersonalInfo: PersonalInfo = {
  name: '',
  phone: '',
  email: '',
  gender: '',
  birthday: ''
}

const createEmptyEducation = (): EducationItem => ({
  school: '',
  major: '',
  education: '',
  duration: []
})

const createEmptyWork = (): WorkItem => ({
  company: '',
  position: '',
  description: '',
  duration: []
})

const createEmptyProject = (): ProjectItem => ({
  projectName: '',
  role: '',
  description: '',
  duration: []
})

const form = reactive<ResumeForm>({
  resumeName: '',
  resumeFile: '',
  isDefault: false,
  personalInfo: { ...defaultPersonalInfo },
  educationBackground: [createEmptyEducation()],
  workExperience: [],
  projectExperience: [],
  skills: '',
  selfIntroduction: ''
})

const rules: FormRules<ResumeForm> = {
  resumeName: [
    { required: true, message: '请输入简历名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  'personalInfo.name': [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
}

const resolveDuration = (item: { duration?: string[]; startTime?: string; endTime?: string }) => {
  if (item.duration && item.duration.length === 2) {
    return {
      startTime: item.duration[0],
      endTime: item.duration[1]
    }
  }
  return {
    startTime: item.startTime ?? '',
    endTime: item.endTime ?? ''
  }
}

const normalizeList = <T extends { [key: string]: any }>(list: T[]) => {
  return list
    .map((item) => {
      const { startTime, endTime } = resolveDuration(item as DurationRange)
      const cleaned = { ...item }
      delete cleaned.duration
      return { ...cleaned, startTime, endTime }
    })
    .filter((item) => Object.values(item).some((value) => value))
}

const parseJsonSafe = <T>(value: string | null, fallback: T): T => {
  if (!value) return fallback
  try {
    const parsed = JSON.parse(value)
    return Array.isArray(fallback) && Array.isArray(parsed) ? parsed : parsed ?? fallback
  } catch (error) {
    console.warn('JSON解析失败，已使用默认值', error)
    return fallback
  }
}

const populateForm = (detail: ResumeDetail) => {
  form.resumeName = detail.resumeName ?? ''
  form.resumeFile = detail.resumeFile ?? ''
  form.isDefault = detail.isDefault === 1
  resumeFileList.value = []
  selectedResumeFile.value = null
  form.personalInfo = {
    ...defaultPersonalInfo,
    ...parseJsonSafe(detail.personalInfo, defaultPersonalInfo)
  }
  const educationList = parseJsonSafe(detail.educationBackground, []) as EducationItem[]
  form.educationBackground =
    educationList.length > 0 ? educationList.map((item) => ({ ...item, duration: [item.startTime ?? '', item.endTime ?? ''].filter(Boolean) })) : [createEmptyEducation()]

  const workList = parseJsonSafe(detail.workExperience, []) as WorkItem[]
  form.workExperience = workList.map((item) => ({
    ...item,
    duration: [item.startTime ?? '', item.endTime ?? ''].filter(Boolean)
  }))

  const projectList = parseJsonSafe(detail.projectExperience, []) as ProjectItem[]
  form.projectExperience = projectList.map((item) => ({
    ...item,
    duration: [item.startTime ?? '', item.endTime ?? ''].filter(Boolean)
  }))

  form.skills = detail.skills ?? ''
  form.selfIntroduction = detail.selfIntroduction ?? ''
}

const loadDetail = async () => {
  if (!resumeId.value) return
  loading.value = true
  try {
    const res = await fetchResumeDetail(resumeId.value)
    if (res.data) {
      populateForm(res.data)
    }
  } catch (error) {
    console.error('加载简历详情失败', error)
  } finally {
    loading.value = false
  }
}

const addEducation = () => {
  form.educationBackground.push(createEmptyEducation())
}

const removeEducation = (index: number) => {
  form.educationBackground.splice(index, 1)
  if (form.educationBackground.length === 0) {
    form.educationBackground.push(createEmptyEducation())
  }
}

const addWork = () => {
  form.workExperience.push(createEmptyWork())
}

const removeWork = (index: number) => {
  form.workExperience.splice(index, 1)
}

const addProject = () => {
  form.projectExperience.push(createEmptyProject())
}

const removeProject = (index: number) => {
  form.projectExperience.splice(index, 1)
}

const buildPayload = () => {
  const educationList = normalizeList(form.educationBackground)
  const workList = normalizeList(form.workExperience)
  const projectList = normalizeList(form.projectExperience)

  return {
    resumeName: form.resumeName,
    resumeFile: form.resumeFile || undefined,
    personalInfo: JSON.stringify(form.personalInfo),
    educationBackground: educationList.length ? JSON.stringify(educationList) : undefined,
    workExperience: workList.length ? JSON.stringify(workList) : undefined,
    projectExperience: projectList.length ? JSON.stringify(projectList) : undefined,
    skills: form.skills || undefined,
    selfIntroduction: form.selfIntroduction || undefined,
    isDefault: form.isDefault ? 1 : 0
  }
}

const handleSubmit = () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      if (selectedResumeFile.value) {
        const uploadRes = await uploadStudentFile(selectedResumeFile.value)
        if (!uploadRes.data?.name) {
          throw new Error('上传结果缺少文件地址')
        }
        form.resumeFile = uploadRes.data.name
      }

      const payload = buildPayload()
      if (isEdit.value && resumeId.value) {
        await updateResume({
          ...payload,
          resumeId: resumeId.value
        })
        ElMessage.success('简历更新成功')
      } else {
        await createResume(payload)
        ElMessage.success('简历创建成功')
      }
      resumeFileList.value = []
      selectedResumeFile.value = null
      router.push({ path: '/student/resume' })
    } catch (error) {
      console.error('保存简历失败', error)
      ElMessage.error(error instanceof Error ? error.message : '保存简历失败，请稍后重试')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleResumeFileChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
  if (!uploadFile.raw) {
    return
  }
  selectedResumeFile.value = uploadFile.raw
  resumeFileList.value = uploadFiles.slice(-1)
}

const handleResumeFileRemove: UploadProps['onRemove'] = () => {
  resumeFileList.value = []
  selectedResumeFile.value = null
}

const handleResumeFileExceed: UploadProps['onExceed'] = () => {
  ElMessage.warning('仅支持上传一个文件，请先移除已选文件')
}

const clearExistingResumeFile = () => {
  form.resumeFile = ''
}

onMounted(() => {
  loadDetail()
})
</script>

<style scoped lang="scss">
.student-resume-edit {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.resume-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.dynamic-item {
  border: 1px dashed #ebeef5;
  padding: 16px;
  margin-bottom: 12px;
  border-radius: 6px;

  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    font-weight: 600;
  }
}

.empty-tip {
  color: #909399;
  padding: 12px 0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-bottom: 24px;
}

.resume-file-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.resume-file-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
</style>

