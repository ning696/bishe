<template>
  <div class="admin-profile-page">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>管理员信息</span>
          <el-button type="danger" size="small" @click="onLogout">退出登录</el-button>
        </div>
      </template>
      <el-skeleton v-if="loading" :rows="4" animated />
      <div v-else class="profile-content">
        <div class="base">
          <el-avatar :size="64" :src="info?.avatar" icon="UserFilled" />
          <div class="meta">
            <div class="name">{{ info?.nickname || info?.username }}</div>
            <div class="sub">ID: {{ info?.id }}</div>
          </div>
        </div>
        <el-descriptions :column="2" border class="mt16">
          <el-descriptions-item label="用户名">{{ info?.username }}</el-descriptions-item>
          <el-descriptions-item label="昵称">{{ info?.nickname || '-' }}</el-descriptions-item>
          <el-descriptions-item label="角色">
            <el-tag v-for="r in info?.roles || []" :key="r" class="mr8" type="success">{{ r }}</el-tag>
            <span v-if="!info?.roles || info?.roles.length === 0">-</span>
          </el-descriptions-item>
          <el-descriptions-item label="权限">
            <el-tag v-for="p in info?.permissions || []" :key="p" class="mr8" type="info">{{ p }}</el-tag>
            <span v-if="!info?.permissions || info?.permissions.length === 0">-</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
  </template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminInfo, logout } from '@/api/admin/admin'
import { removeToken } from '@/utils/cookie'
import { useRouter } from 'vue-router'

interface AdminInfo {
  id: number
  username: string
  nickname?: string
  avatar?: string
  roles?: string[]
  permissions?: string[]
}

const router = useRouter()
const loading = ref(false)
const info = ref<AdminInfo | null>(null)

const fetchInfo = async () => {
  loading.value = true
  try {
    const res = await getAdminInfo()
    info.value = res.data
  } catch (e) {
    // 已由拦截器处理
  } finally {
    loading.value = false
  }
}

const onLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    })
    await logout()
              console.error('test006')

    removeToken()
    ElMessage.success('已退出登录')
    router.replace('/enterprise/login')
  } catch {
    // cancel
  }
}

onMounted(fetchInfo)
</script>

<style scoped>
.admin-profile-page {
  padding: 16px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.profile-content .base {
  display: flex;
  align-items: center;
}
.profile-content .meta {
  margin-left: 12px;
}
.profile-content .name {
  font-size: 18px;
  font-weight: 600;
}
.mt16 { margin-top: 16px; }
.mr8 { margin-right: 8px; }
</style>


