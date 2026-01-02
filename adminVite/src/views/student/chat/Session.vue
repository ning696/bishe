<template>
  <div class="student-chat-session">
    <el-page-header :content="`会话 #${sessionId}`" @back="router.back()" />
    <el-alert
      title="聊天页面尚未对接具体消息列表组件，当前为占位页面。"
      type="info"
      show-icon
      class="tips"
    />
    <el-card shadow="never">
      <div class="placeholder">
        会话已创建，ID：{{ sessionId }}。请后续接入消息列表与发送组件。
      </div>
    </el-card>
  </div>
  </template>
  
  <script setup lang="ts">
  import { computed } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  
  const route = useRoute()
  const router = useRouter()
  
  const sessionId = computed(() => route.params.id)
  </script>
  
  <style scoped lang="scss">
  .student-chat-session {
    padding: 16px;
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
  .tips {
    margin-top: 8px;
  }
  .placeholder {
    color: #666;
  }
  </style>
  

