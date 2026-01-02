<template>
  <div class="sidebar-container" :class="{ 'is-collapse': isCollapse }">
    <div class="sidebar-toggle" @click="toggleCollapse">
      <el-icon><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
    </div>
    <el-menu
      :default-active="activeMenu"
      :collapse="isCollapse"
      :unique-opened="true"
      router
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409eff"
    >
      <template v-if="isAdmin">
        <el-menu-item index="/admin/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/admin/student/list">
          <el-icon><List /></el-icon>
          <template #title>学生管理</template>
        </el-menu-item>
        <el-menu-item index="/admin/enterprise/list">
          <el-icon><User /></el-icon>
          <template #title>企业管理</template>
        </el-menu-item>
        <el-menu-item index="/admin/job/list">
          <el-icon><Briefcase /></el-icon>
          <template #title>职位管理</template>
        </el-menu-item>
        <el-menu-item index="/admin/campus/list">
          <el-icon><List /></el-icon>
          <template #title>校园管理</template>
        </el-menu-item>
        <el-menu-item index="/admin/complaint/list">
          <el-icon><Warning /></el-icon>
          <template #title>投诉管理</template>
        </el-menu-item>
      </template>
      <template v-else-if="isEnterprise">
        <el-menu-item index="/enterprise/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/enterprise/resume/list">
          <el-icon><Document /></el-icon>
          <template #title>职位申请</template>
        </el-menu-item>
        <el-menu-item index="/enterprise/job">
          <el-icon><List /></el-icon>
          <template #title>职位管理</template>
        </el-menu-item>
        <el-menu-item index="/enterprise/job/add">
          <el-icon><CirclePlus /></el-icon>
          <template #title>发布职位</template>
        </el-menu-item>
        <el-menu-item index="/enterprise/interview/application/list">
          <el-icon><Tickets /></el-icon>
          <template #title>面试申请</template>
        </el-menu-item>
        <el-menu-item index="/enterprise/consultation">
          <el-icon><ChatLineRound /></el-icon>
          <template #title>
            <el-badge :value="enterpriseUnreadCount" :hidden="enterpriseUnreadCount === 0" :max="99">
              线上咨询
            </el-badge>
          </template>
        </el-menu-item>
      </template>
      <template v-else-if="isStudent">
        <el-menu-item index="/student/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        <el-menu-item index="/student/jobs">
          <el-icon><Briefcase /></el-icon>
          <template #title>职位列表</template>
        </el-menu-item>
        <el-menu-item index="/student/job/favorite">
          <el-icon><StarFilled /></el-icon>
          <template #title>我的收藏</template>
        </el-menu-item>
        <el-menu-item index="/student/resume">
          <el-icon><Document /></el-icon>
          <template #title>我的简历</template>
        </el-menu-item>
        <el-menu-item index="/student/interview/application">
          <el-icon><Tickets /></el-icon>
          <template #title>面试申请</template>
        </el-menu-item>
        <el-menu-item index="/student/personal/application/list">
          <el-icon><List /></el-icon>
          <template #title>职位申请</template>
        </el-menu-item>
        <el-menu-item index="/student/consultation">
          <el-icon><ChatLineRound /></el-icon>
          <template #title>
            <el-badge :value="studentUnreadCount" :hidden="studentUnreadCount === 0" :max="99">
              线上咨询
            </el-badge>
          </template>
        </el-menu-item>
        <el-menu-item index="/student/complaint">
          <el-icon><Warning /></el-icon>
          <template #title>我的投诉</template>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { Fold, Expand, Odometer, List, CirclePlus, Briefcase, StarFilled, Document, Tickets, User, ChatLineRound, Warning } from '@element-plus/icons-vue'
import { fetchUnreadCount as fetchStudentUnreadCount } from '@/api/student/consultation'
import { fetchUnreadCount as fetchEnterpriseUnreadCount } from '@/api/enterprise/consultation'

const route = useRoute()
const userStore = useUserStore()

const COLLAPSE_KEY = 'sidebar-collapse'

// 定义 emit
const emit = defineEmits<{
  collapseChange: [value: boolean]
}>()

// 从 localStorage 读取折叠状态
const isCollapse = ref<boolean>(localStorage.getItem(COLLAPSE_KEY) === 'true')

// 当前激活的菜单
const activeMenu = computed(() => (route.meta.activeMenu as string) || route.path)

const isAdmin = computed(() => userStore.role === 'admin')
const isEnterprise = computed(() => userStore.role === 'enterprise')
const isStudent = computed(() => userStore.role === 'student')

// 未读消息数
const studentUnreadCount = ref<number>(0)
const enterpriseUnreadCount = ref<number>(0)

// 轮询定时器
let unreadCountTimer: number | null = null

// 获取未读消息数
const loadUnreadCount = async () => {
  if (isStudent.value) {
    try {
      const response = await fetchStudentUnreadCount()
      if (response.code === 1000 && response.data) {
        studentUnreadCount.value = response.data.totalUnreadCount || 0
      }
    } catch (error) {
      // 静默失败，不影响页面使用
      console.error('获取未读消息数失败:', error)
    }
  } else if (isEnterprise.value) {
    try {
      const response = await fetchEnterpriseUnreadCount()
      if (response.code === 1000 && response.data) {
        enterpriseUnreadCount.value = response.data.totalUnreadCount || 0
      }
    } catch (error) {
      // 静默失败，不影响页面使用
      console.error('获取未读消息数失败:', error)
    }
  }
}

// 切换折叠状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
  localStorage.setItem(COLLAPSE_KEY, String(isCollapse.value))
  emit('collapseChange', isCollapse.value)
}

onMounted(() => {
  // 初始化时从 localStorage 读取
  const saved = localStorage.getItem(COLLAPSE_KEY)
  if (saved !== null) {
    isCollapse.value = saved === 'true'
  }
  
  // 加载未读消息数
  loadUnreadCount()
  
  // 每30秒轮询一次未读消息数
  unreadCountTimer = window.setInterval(() => {
    loadUnreadCount()
  }, 30000)
})

onUnmounted(() => {
  // 清理定时器
  if (unreadCountTimer) {
    clearInterval(unreadCountTimer)
    unreadCountTimer = null
  }
})
</script>

<style scoped lang="scss">
.sidebar-container {
  position: relative;
  width: 200px;
  height: 100%;
  background-color: #304156;
  transition: width 0.3s;

  &.is-collapse {
    width: 64px;
  }

  .sidebar-toggle {
    position: absolute;
    top: 10px;
    right: -15px;
    z-index: 100;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #409eff;
    border-radius: 50%;
    cursor: pointer;
    color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);

    &:hover {
      background-color: #66b1ff;
    }
  }

  :deep(.el-menu) {
    border-right: none;
    height: 100%;
  }

  :deep(.el-menu-item) {
    height: 50px;
    line-height: 50px;
    display: flex;
    align-items: center;
    
    .el-badge {
      width: 100%;
      display: flex;
      align-items: center;
      line-height: normal;
      
      .el-badge__content {
        top: auto;
        transform: translateY(-50%);
      }
    }
  }
}
</style>

