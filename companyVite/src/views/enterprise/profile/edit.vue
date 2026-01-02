<template>
  <div class="edit-container">
    <h2>编辑企业资料</h2>
    <form class="form" @submit.prevent="onSubmit">
      <div class="form-item">
        <label>企业名称</label>
        <input v-model.trim="form.companyName" type="text" placeholder="请输入企业名称" />
      </div>
      <div class="form-item">
        <label>联系人姓名</label>
        <input v-model.trim="form.contactName" type="text" placeholder="请输入联系人姓名" />
      </div>
      <div class="form-item">
        <label>联系人手机</label>
        <input v-model.trim="form.contactMobile" type="tel" placeholder="请输入联系人手机" />
      </div>
      <div class="form-item">
        <label>邮箱</label>
        <input v-model.trim="form.email" type="email" placeholder="请输入邮箱" />
      </div>
      <div class="form-item">
        <label>地址</label>
        <input v-model.trim="form.address" type="text" placeholder="请输入公司地址" />
      </div>
      <div class="form-item">
        <label>简介</label>
        <textarea v-model.trim="form.introduction" rows="4" placeholder="请输入公司简介"></textarea>
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
import { onMounted, reactive, ref } from 'vue'
import { getProfile, updateProfile, type UpdateProfileParams } from '@/api/enterprise/enterprise'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive<UpdateProfileParams>({
  companyName: '',
  contactName: '',
  contactMobile: '',
  email: '',
  address: '',
  introduction: ''
})

const submitting = ref(false)

async function load() {
  try {
    const res = await getProfile()
    if (res.code === 200 && res.data) {
      form.companyName = res.data.companyName || ''
      form.contactName = res.data.contactName || ''
      form.contactMobile = res.data.contactMobile || ''
      form.email = res.data.email || ''
      form.address = res.data.address || ''
      form.introduction = res.data.introduction || ''
    }
  } catch (e) {
    // ignore
  }
}

async function onSubmit() {
  submitting.value = true
  try {
    const res = await updateProfile(form)
    if (res.code === 200) {
      window.alert('保存成功')
      router.push('/enterprise/profile')
    } else {
      window.alert(res.msg || '保存失败')
    }
  } catch (e) {
    window.alert('保存失败，请稍后重试')
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
.edit-container {
  max-width: 720px;
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
input, textarea {
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
  border-color: #10b981;
  background: #10b981;
  color: #fff;
}
</style>


