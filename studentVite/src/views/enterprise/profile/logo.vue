<template>
  <div class="logo-container">
    <h2>更新企业Logo</h2>
    <div class="current">
      <div class="label">当前Logo</div>
      <img v-if="logoUrl" :src="logoUrl" class="logo" alt="当前Logo" />
      <div v-else class="placeholder">暂无Logo</div>
    </div>
    <form class="form" @submit.prevent="onSubmit">
      <div class="form-item">
        <label>Logo 图片地址(URL)</label>
        <input v-model.trim="newLogo" type="url" placeholder="请输入图片URL" required />
      </div>
      <div class="actions">
        <button class="btn primary" type="submit" :disabled="submitting">
          {{ submitting ? '保存中...' : '保存' }}
        </button>
        <button class="btn" type="button" @click="goBack">返回</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getProfile, updateLogo } from '@/api/enterprise/enterprise'
import { useRouter } from 'vue-router'

const router = useRouter()

const logoUrl = ref('')
const newLogo = ref('')
const submitting = ref(false)

async function load() {
  try {
    const res = await getProfile()
    if (res.code === 1000 && res.data) {
      logoUrl.value = res.data.logo || ''
    }
  } catch (e) {
    // ignore
  }
}

async function onSubmit() {
  if (!newLogo.value) {
    window.alert('请填写图片URL')
    return
  }
  submitting.value = true
  try {
    const res = await updateLogo({ logo: newLogo.value })
    if (res.code === 1000) {
      window.alert('更新成功')
      router.push('/enterprise/profile')
    } else {
      window.alert(res.msg || '更新失败')
    }
  } catch (e) {
    window.alert('更新失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.back()
}

onMounted(load)
</script>

<style scoped>
.logo-container {
  max-width: 640px;
  margin: 24px auto;
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #fff;
}
.current {
  margin-bottom: 16px;
}
.label {
  margin-bottom: 6px;
  color: #6b7280;
}
.logo {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}
.placeholder {
  width: 120px;
  height: 120px;
  border-radius: 12px;
  background: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
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
  border-color: #10b981;
  background: #10b981;
  color: #fff;
}
</style>


