<template>
  <div class="edit-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>编辑学生信息</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
        class="edit-form"
      >
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="editForm.nickName" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="editForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="editForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="editForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="editForm.gender">
                <el-radio :label="0">女</el-radio>
                <el-radio :label="1">男</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="生日" prop="birthday">
              <el-date-picker
                v-model="editForm.birthday"
                type="date"
                placeholder="选择生日"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="所属校园" prop="campusId">
              <el-select
                v-model="editForm.campusId"
                placeholder="请选择所属校园"
                filterable
                remote
                reserve-keyword
                clearable
                :remote-method="handleCampusRemoteMethod"
                :loading="campusLoading"
                style="width: 100%"
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

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="editForm.major" placeholder="请输入专业" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="editForm.education" placeholder="请选择学历" style="width: 100%">
                <el-option label="专科" value="专科" />
                <el-option label="本科" value="本科" />
                <el-option label="硕士" value="硕士" />
                <el-option label="博士" value="博士" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="年级" prop="grade">
              <el-input v-model="editForm.grade" placeholder="请输入年级，如：2024届" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="工作经验" prop="experience">
              <el-input-number
                v-model="editForm.experience"
                :min="0"
                placeholder="请输入工作经验（月）"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="期望薪资" prop="expectedSalary">
              <el-input-number
                v-model="editForm.expectedSalary"
                :min="0"
                :precision="2"
                placeholder="请输入期望薪资"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="期望地点" prop="expectedLocation">
              <el-input v-model="editForm.expectedLocation" placeholder="请输入期望工作地点" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="技能" prop="skills">
          <el-input
            v-model="editForm.skills"
            type="textarea"
            :rows="3"
            placeholder="请输入技能，多个技能用逗号分隔，如：Java,Spring,MySQL"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { getDetail, updateInfo, fetchCampusOptions, type UpdateInfoParams, type CampusOption } from '@/api/student/student'

const router = useRouter()

const editFormRef = ref<FormInstance>()
const loading = ref(false)
const saving = ref(false)
const campusOptions = ref<CampusOption[]>([])
const campusLoading = ref(false)

const editForm = reactive<UpdateInfoParams>({
  nickName: '',
  realName: '',
  phone: '',
  email: '',
  gender: undefined,
  birthday: '',
  campusId: undefined,
  major: '',
  education: '',
  grade: '',
  skills: '',
  experience: undefined,
  expectedSalary: undefined,
  expectedLocation: ''
})

const editRules: FormRules = {
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const loadCampusOptions = async (keyword = '') => {
  campusLoading.value = true
  try {
    const res = await fetchCampusOptions(keyword)
    const rows = (res.data?.rows ?? []) as CampusOption[]
    campusOptions.value = rows
  } catch (error) {
    console.error('加载校园列表失败', error)
  } finally {
    campusLoading.value = false
  }
}

const handleCampusRemoteMethod = (keyword?: string) => {
  const normalizedKeyword = (keyword || '').trim()
  loadCampusOptions(normalizedKeyword)
}

const loadStudentDetail = async () => {
  loading.value = true
  try {
    const response = await getDetail()
    if (response.code === 1000 && response.data) {
      const detail = response.data
      editForm.nickName = detail.nickName || ''
      editForm.realName = detail.realName || ''
      editForm.email = detail.email || ''
      editForm.phone = detail.phone || ''
      editForm.gender = detail.gender
      editForm.birthday = detail.birthday || ''
      editForm.campusId = detail.campusId
      editForm.major = detail.major || ''
      editForm.education = detail.education || ''
      editForm.grade = detail.grade || ''
      editForm.skills = detail.skills || ''
      editForm.experience = detail.experience
      editForm.expectedSalary = detail.expectedSalary
      editForm.expectedLocation = detail.expectedLocation || ''
    } else {
      ElMessage.error(response.msg || '获取详情失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '获取详情失败，请重试')
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  if (!editFormRef.value) return

  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        const params: UpdateInfoParams = {
          nickName: editForm.nickName,
          realName: editForm.realName,
          phone: editForm.phone,
          email: editForm.email,
          gender: editForm.gender,
          birthday: editForm.birthday,
          campusId: editForm.campusId,
          major: editForm.major,
          education: editForm.education,
          grade: editForm.grade,
          skills: editForm.skills,
          experience: editForm.experience,
          expectedSalary: editForm.expectedSalary,
          expectedLocation: editForm.expectedLocation
        }

        const response = await updateInfo(params)
        if (response.code === 1000) {
          ElMessage.success('信息更新成功')
          router.push('/student/profile')
        } else {
          ElMessage.error(response.msg || '信息更新失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '信息更新失败，请重试')
      } finally {
        saving.value = false
      }
    }
  })
}

const goBack = () => {
  router.push('/student/profile')
}

onMounted(() => {
  loadStudentDetail()
  loadCampusOptions()
})
</script>

<style scoped lang="scss">
.edit-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .edit-form {
    margin-top: 20px;
  }

}
</style>

