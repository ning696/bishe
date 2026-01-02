<template>
  <div class="cert-container">
    <h2>企业认证</h2>
    <p class="desc">请上传可用于审核的证明材料文件（如营业执照、资质证明等）。</p>
    <form class="form" @submit.prevent="onSubmit">
      <div class="form-item">
        <label>认证材料文件</label>
        <input 
          ref="fileInputRef"
          type="file" 
          @change="handleFileChange"
          accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"
          required 
        />
        <div v-if="selectedFile" class="file-info">
          <span>已选择文件：{{ selectedFile.name }}</span>
          <span class="file-size">({{ formatFileSize(selectedFile.size) }})</span>
        </div>
      </div>
      <div class="actions">
        <button class="btn primary" type="submit" :disabled="submitting || !selectedFile">
          {{ submitting ? '提交中...' : '提交审核' }}
        </button>
        <button class="btn" type="button" @click="goBack">返回</button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { applyCertification } from '@/api/enterprise/enterprise'
import { useRouter } from 'vue-router'

const router = useRouter()

const fileInputRef = ref<HTMLInputElement | null>(null)
const selectedFile = ref<File | null>(null)
const submitting = ref(false)

function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0]
  } else {
    selectedFile.value = null
  }
}

function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

async function onSubmit() {
  if (!selectedFile.value) {
    window.alert('请选择认证材料文件')
    return
  }
  submitting.value = true
  try {
    const res = await applyCertification(selectedFile.value)
    if (res.code === 200) {
      window.alert('提交成功，请等待审核结果')
      router.push('/enterprise/profile')
    } else {
      window.alert(res.msg || '提交失败')
    }
  } catch (e) {
    window.alert('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.cert-container {
  max-width: 720px;
  margin: 24px auto;
  padding: 16px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #fff;
}
.desc {
  margin: 6px 0 14px;
  color: #6b7280;
  font-size: 14px;
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
input, textarea {
  padding: 8px 10px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  outline: none;
}
input[type="file"] {
  padding: 6px;
  cursor: pointer;
}
.file-info {
  margin-top: 8px;
  font-size: 14px;
  color: #6b7280;
  display: flex;
  align-items: center;
  gap: 8px;
}
.file-size {
  color: #9ca3af;
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
  border-color: #2563eb;
  background: #2563eb;
  color: #fff;
}
</style>


