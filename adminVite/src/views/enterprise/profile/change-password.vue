<template>
  <div class="cp-container">
    <h2>修改密码</h2>
    <form class="form" @submit.prevent="onSubmit">
      <div class="form-item">
        <label>当前密码</label>
        <input v-model="oldPassword" type="password" placeholder="请输入当前密码" required />
      </div>
      <div class="form-item">
        <label>新密码</label>
        <input v-model="newPassword" type="password" placeholder="请输入新密码" required />
      </div>
      <div class="form-item">
        <label>确认新密码</label>
        <input v-model="confirmPassword" type="password" placeholder="请再次输入新密码" required />
      </div>
      <div class="actions">
        <button class="btn primary" type="submit" :disabled="submitting">
          {{ submitting ? '提交中...' : '保存' }}
        </button>
        <button class="btn" type="button" @click="goBack">返回</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { changePassword } from '@/api/enterprise/enterprise'
import { useRouter } from 'vue-router'

const router = useRouter()

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const submitting = ref(false)

async function onSubmit() {
  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    window.alert('请完整填写表单')
    return
  }
  if (newPassword.value !== confirmPassword.value) {
    window.alert('两次输入的新密码不一致')
    return
  }
  submitting.value = true
  try {
    const res = await changePassword({
      oldPassword: oldPassword.value,
      newPassword: newPassword.value
    })
    if (res.code === 200) {
      window.alert('修改成功，请使用新密码重新登录')
      router.push('/admin/login')
    } else {
      window.alert(res.msg || '修改失败')
    }
  } catch (e) {
    window.alert('修改失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.cp-container {
  max-width: 520px;
  margin: 24px auto;
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #fff;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 14px;
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
  padding: 8px 10px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  outline: none;
}
.actions {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}
.btn {
  height: 36px;
  padding: 0 16px;
  border: 1px solid #d1d5db;
  background: #fff;
  border-radius: 6px;
  cursor: pointer;
}
.btn.primary {
  border-color: #2563eb;
  background: #2563eb;
  color: #fff;
}
</style>


