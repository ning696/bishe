<template>
  <div class="app-main">
    <!-- AppMain 中的 router-view 用于渲染嵌套路由 -->
    <!-- 如果路由不是嵌套的，这个 router-view 不会渲染任何内容 -->
    <!-- 实际组件由 App.vue 通过 <component :is="Component" /> 渲染 -->
    <router-view />
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.app-main {
  padding: 20px;
  min-height: calc(100vh - 60px);
  background-color: #f0f2f5;
}
</style>

