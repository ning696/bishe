<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>学生登录</h2>
      </div>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        label-width="0"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="register-link">
            <span>还没有账号？</span>
            <el-link type="primary" @click="goToRegister">立即注册</el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance, FormRules } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login, getDetail } from '@/api/student/student'
import { useUserStore } from '@/store/modules/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await login({
          username: loginForm.username,
          password: loginForm.password
        })

        if (response.code === 1000 && response.data) {
          // 保存 Token
          userStore.setUserToken(response.data)
          // 设置角色
          userStore.setUserRole('student')
          // 保存用户信息（优先从详情接口获取，失败则回退仅用户名）
          try {
            const detailRes = await getDetail()
            if (detailRes.code === 1000 && detailRes.data) {
              userStore.setUserInfo({
                id: detailRes.data.id,
                username: detailRes.data.username || loginForm.username,
                name: detailRes.data.realName || detailRes.data.nickName,
                avatar: detailRes.data.headImage
              })
            } else {
              userStore.setUserInfo({
                username: loginForm.username
              })
            }
          } catch {
            userStore.setUserInfo({
              username: loginForm.username
            })
          }

          ElMessage.success('登录成功')
          router.push('/student/dashboard')
        } else {
          ElMessage.error(response.msg || '登录失败')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败，请重试')
      } finally {
        loading.value = false
      }
    }
  })
}

const goToRegister = () => {
  router.push('/student/register')
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-box {
    width: 400px;
    padding: 40px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);

    .login-header {
      text-align: center;
      margin-bottom: 30px;

      h2 {
        margin: 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;
      }
    }

    .login-form {
      .login-button {
        width: 100%;
        margin-top: 10px;
      }

      .register-link {
        width: 100%;
        text-align: center;
        font-size: 14px;
        color: #606266;

        span {
          margin-right: 8px;
        }
      }
    }
  }
}
</style>

