<template>
  <div class="register-container">
    <h2>企业注册</h2>
    <form class="form" @submit.prevent="onSubmit">
      <div class="form-item">
        <label>用户名</label>
        <input v-model.trim="form.username" type="text" placeholder="请输入用户名" required />
      </div>
      <div class="form-item">
        <label>登录密码</label>
        <input v-model="form.password" type="password" placeholder="请输入登录密码" required />
      </div>
      <div class="form-item">
        <label>企业名称</label>
        <input v-model.trim="form.enterpriseName" type="text" placeholder="请输入企业名称" required />
      </div>
      <div class="form-actions">
        <button class="submit-btn" type="submit" :disabled="submitting">
          {{ submitting ? '提交中...' : '注册' }}
        </button>
      </div>
      <p class="tips">已有账号？<a href="/enterprise/login">前往登录</a></p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { register, type RegisterParams } from '@/api/enterprise/enterprise'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive<RegisterParams>({
  username: '',
  password: '',
  enterpriseName: ''
})

const submitting = ref(false)

async function onSubmit() {
  if (!form.username || !form.password || !form.enterpriseName) {
    window.alert('请完整填写用户名、密码和企业名称')
    return
  }
  submitting.value = true
  try {
    const res = await register(form)
    if (res.code === 1000) {
      window.alert('注册成功，请登录')
      router.push('/enterprise/login')
    } else {
      window.alert(res.msg || '注册失败')
    }
  } catch (e) {
    window.alert('注册失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.register-container {
  max-width: 520px;
  margin: 48px auto;
  padding: 24px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #fff;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
label {
  font-size: 14px;
  color: #374151;
}
input {
  height: 36px;
  padding: 6px 10px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  outline: none;
}
.code-row {
  display: flex;
  gap: 8px;
}
.code-row input {
  flex: 1;
}
.code-btn {
  height: 36px;
  padding: 0 12px;
  border: 1px solid #2563eb;
  background: #2563eb;
  color: #fff;
  border-radius: 6px;
  cursor: pointer;
}
.code-btn:disabled {
  background: #93c5fd;
  border-color: #93c5fd;
  cursor: not-allowed;
}
.form-actions {
  margin-top: 8px;
}
.submit-btn {
  width: 100%;
  height: 40px;
  border: none;
  border-radius: 6px;
  background: #10b981;
  color: #fff;
  cursor: pointer;
}
.tips {
  margin-top: 8px;
  font-size: 13px;
  color: #6b7280;
}
.tips a {
  color: #2563eb;
  text-decoration: none;
}
</style>


