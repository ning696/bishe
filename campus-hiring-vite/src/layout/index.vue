<template>
  <el-container class="layout-container">
    <el-aside :width="sidebarWidth" class="sidebar-wrapper">
      <Sidebar @collapse-change="handleCollapseChange" />
    </el-aside>
    <el-container>
      <el-header class="header-wrapper">
        <Navbar />
      </el-header>
      <el-main class="main-wrapper">
        <slot />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useUserStore } from '@/store/modules/user'
import Navbar from './components/Navbar.vue'
import Sidebar from './components/Sidebar.vue'

const userStore = useUserStore()

// 使用 ref 来存储折叠状态
const isCollapse = ref<boolean>(localStorage.getItem('sidebar-collapse') === 'true')

// 侧边栏宽度（根据折叠状态动态计算）
const sidebarWidth = computed(() => {
  return isCollapse.value ? '64px' : '200px'
})

// 处理折叠状态变化
const handleCollapseChange = (value: boolean) => {
  isCollapse.value = value
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
  overflow: hidden;

  .sidebar-wrapper {
    background-color: #304156;
    transition: width 0.3s;
  }

  .header-wrapper {
    padding: 0;
    height: 60px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .main-wrapper {
    padding: 20px;
    background-color: #f0f2f5;
    overflow-y: auto;
    min-height: calc(100vh - 60px);
  }
}
</style>

