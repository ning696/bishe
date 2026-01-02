<template>
  <div class="change-password-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="120px"
        class="password-form"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码（至少6位）"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
            @keyup.enter="handleSave"
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { changePassword } from '@/api/student/student'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const userStore = useUserStore()

const passwordFormRef = ref<FormInstance>()
const loading = ref(false)
const saving = ref(false)

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules: FormRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleSave = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        const response = await changePassword({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })

        if (response.code === 1000) {
          ElMessage.success('密码修改成功，请重新登录')
          // 清空表单
          passwordForm.oldPassword = ''
          passwordForm.newPassword = ''
          passwordForm.confirmPassword = ''
          // 清除token和用户信息
          userStore.logout()
          // 延迟跳转到登录页面，让用户看到成功提示
          setTimeout(() => {
            router.push('/student/login')
          }, 1500)
        } else {
          ElMessage.error(response.msg || '密码修改失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '密码修改失败，请重试')
      } finally {
        saving.value = false
      }
    }
  })
}

const goBack = () => {
  router.push('/student/profile')
}
</script>

<style scoped lang="scss">
.change-password-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .password-form {
    margin-top: 20px;
    max-width: 600px;
  }
}
</style>

