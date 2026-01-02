<template>
  <div class="profile-container">
    <h2>企业资料</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else class="card">
      <div class="avatar-section" v-if="profile">
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
          :http-request="handleAvatarUpload"
          :disabled="uploading"
        >
          <el-avatar
            :size="100"
            :src="profile.logo"
            :icon="UserFilled"
            :class="{ 'avatar-loading': uploading }"
          />
          <div class="avatar-mask" v-if="uploading">
            <el-icon><Loading /></el-icon>
          </div>
        </el-upload>
        <div class="avatar-info">
          <h3>{{ profile.enterpriseName || '未设置企业名称' }}</h3>
          <p class="username">用户名：{{ profile.username || '-' }}</p>
        </div>
      </div>
      <div class="row">
        <span class="label">企业名称</span>
        <span class="value">{{ profile?.enterpriseName || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">用户名</span>
        <span class="value">{{ profile?.username || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">法定代表人</span>
        <span class="value">{{ profile?.legalPerson || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">联系电话</span>
        <span class="value">{{ profile?.phone || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">邮箱</span>
        <span class="value">{{ profile?.email || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">地址</span>
        <span class="value">{{ profile?.address || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">行业</span>
        <span class="value">{{ profile?.industry || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">规模</span>
        <span class="value">{{ profile?.scale || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">简介</span>
        <span class="value">{{ profile?.description || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">官网</span>
        <span class="value">
          <template v-if="profile?.website">
            <a :href="profile.website" target="_blank" rel="noopener">{{ profile.website }}</a>
          </template>
          <template v-else>-</template>
        </span>
      </div>
      <div class="row">
        <span class="label">认证状态</span>
        <span class="value">{{ profile?.certificationStatusName || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">认证文件</span>
        <span class="value">
          <template v-if="profile?.certificationFile">
            <a :href="profile.certificationFile" target="_blank" rel="noopener">查看认证文件</a>
          </template>
          <template v-else>-</template>
        </span>
      </div>
      <div class="row">
        <span class="label">账号状态</span>
        <span class="value">{{ profile?.statusName || '-' }}</span>
      </div>
      <div class="row">
        <span class="label">创建时间</span>
        <span class="value">{{ profile?.createTime || '-' }}</span>
      </div>
    </div>

    <div class="actions">
      <router-link class="btn" to="/enterprise/profile/edit">编辑资料</router-link>
      <router-link class="btn" to="/enterprise/profile/change-password">修改密码</router-link>
      <router-link class="btn" to="/enterprise/profile/logo">更新Logo</router-link>
      <router-link class="btn" to="/enterprise/profile/certification">企业认证</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Loading, UserFilled } from '@element-plus/icons-vue'
import { getProfile, updateLogo, uploadEnterpriseFile, type EnterpriseProfile } from '@/api/enterprise/enterprise'
import type { UploadRequestOptions } from 'element-plus'

const loading = ref(true)
const uploading = ref(false)
const profile = ref<EnterpriseProfile | null>(null)

async function fetchProfile() {
  loading.value = true
  try {
    const res = await getProfile()
    if (res.code === 1000) {
      profile.value = res.data
    } else {
      ElMessage.error(res.msg || '获取资料失败')
    }
  } catch (e) {
    ElMessage.error('获取资料失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }
  return true
}

const handleAvatarUpload = async (options: UploadRequestOptions) => {
  if (uploading.value) {
    return
  }
  uploading.value = true
  const file = options.file as File
  try {
    const uploadRes = await uploadEnterpriseFile(file)
    if (uploadRes.code !== 1000 || !uploadRes.data?.name) {
      throw new Error(uploadRes.msg || 'Logo上传失败')
    }

    const updateRes = await updateLogo({ logo: uploadRes.data.name })
    if (updateRes.code !== 1000) {
      throw new Error(updateRes.msg || 'Logo更新失败')
    }

    ElMessage.success('Logo更新成功')
    await fetchProfile()
    options.onSuccess?.(updateRes, file)
  } catch (error: any) {
    ElMessage.error(error.message || 'Logo更新失败，请重试')
    options.onError?.(error)
  } finally {
    uploading.value = false
  }
}

onMounted(fetchProfile)
</script>

<style scoped>
.profile-container {
  max-width: 760px;
  margin: 24px auto;
  padding: 0 12px;
}
.loading {
  padding: 16px;
  color: #6b7280;
}
.card {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: #fff;
  padding: 16px;
}
.avatar-section {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f3f4f6;
}
.avatar-uploader {
  position: relative;
  width: 100px;
  height: 100px;
  cursor: pointer;
}
.avatar-uploader :deep(.el-upload) {
  width: 100%;
  height: 100%;
}
.avatar-loading {
  opacity: 0.6;
}
.avatar-mask {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0, 0, 0, 0.3);
  color: #fff;
  border-radius: 50%;
}
.avatar-info h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}
.avatar-info .username {
  margin: 0;
  font-size: 14px;
  color: #909399;
}
.row {
  display: flex;
  gap: 16px;
  padding: 10px 0;
  border-bottom: 1px solid #f3f4f6;
}
.row:last-child {
  border-bottom: none;
}
.label {
  width: 160px;
  color: #6b7280;
}
.value {
  flex: 1;
  color: #111827;
}
.actions {
  margin-top: 16px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
.btn {
  display: inline-block;
  padding: 8px 12px;
  border-radius: 6px;
  background: #2563eb;
  color: #fff;
  text-decoration: none;
}
</style>


