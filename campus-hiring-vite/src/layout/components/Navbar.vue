<template>
  <div class="navbar">
    <div class="navbar-left">
      <h2 class="navbar-title">校园招聘系统</h2>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="(m, idx) in breadcrumbs" :key="m.path + '-' + idx">
          <span v-if="idx === breadcrumbs.length - 1">{{ m.meta?.title || m.name }}</span>
          <router-link v-else :to="m.path">{{ m.meta?.title || m.name }}</router-link>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="navbar-right">
      <el-dropdown @command="handleCommand">
        <span class="user-info">
          <el-icon><User /></el-icon>
          <span class="username">{{ userStore.userInfo?.name || userStore.userInfo?.username || '用户' }}</span>
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">个人信息</el-dropdown-item>
            <el-dropdown-item v-if="role !== 'admin'" command="changePassword">修改密码</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/store/modules/user'
import { ElMessage } from 'element-plus'
import { User, ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const { role } = storeToRefs(userStore)

// 面包屑：基于当前匹配的路由，过滤隐藏项
const breadcrumbs = computed(() => {
  return route.matched.filter(r => r.meta?.title && !r.meta?.hidden)
})

const handleCommand = (command: string) => {
  if (command === 'profile') {
    if (role.value === 'enterprise') {
      router.push('/enterprise/profile')
    } else if (role.value === 'student') {
    router.push('/student/profile')
    } else if (role.value === 'admin') {
      router.push('/admin/profile')
    } else {
      router.push('/admin/login')
    }
  } else if (command === 'changePassword') {
    if (role.value === 'enterprise') {
      router.push('/enterprise/profile/change-password')
    } else if (role.value === 'student') {
    router.push('/student/profile/change-password')
    } else if (role.value === 'admin') {
      router.push('/admin/dashboard')
    } else {
      router.push('/admin/login')
    }
  } else if (command === 'logout') {
    const currentRole = role.value
    userStore.logout()
    ElMessage.success('退出登录成功')
    // 根据角色跳转到对应登录页
    if (currentRole === 'admin') {
      router.push('/admin/login')
    } else if (currentRole === 'enterprise') {
      router.push('/enterprise/login')
    } else if (currentRole === 'student') {
      router.push('/student/login')
    } else {
      router.push('/admin/login')
    }
  }
}
</script>

<style scoped lang="scss">
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .navbar-left {
    display: flex;
    align-items: center;
    gap: 16px;

    .navbar-title {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    :deep(.el-breadcrumb) {
      font-size: 13px;
      color: #909399;
    }
  }

  .navbar-right {
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      color: #606266;

      .username {
        margin: 0 8px;
      }

      &:hover {
        color: #409eff;
      }
    }
  }
}
</style>

