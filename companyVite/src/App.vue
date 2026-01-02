<template>
  <router-view v-slot="{ Component, route }">
    <template v-if="Component">
      <Layout v-if="route.meta?.layout !== false">
        <transition name="fade-transform" mode="out-in">
          <keep-alive :include="keepAliveNames">
            <component :is="Component" :key="route.fullPath" />
          </keep-alive>
        </transition>
      </Layout>
      <transition v-else name="fade-transform" mode="out-in">
        <component :is="Component" :key="route.fullPath" />
      </transition>
    </template>
  </router-view>
</template>

<script setup lang="ts">
import { computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import Layout from './layout/index.vue'

const route = useRoute()

// 需要缓存的页面（通过路由 meta.keepAlive 标记或白名单）
const keepAliveNames = computed<string[]>(() => {
  // 白名单：职位列表、收藏列表、简历列表
  const whiteList = ['StudentJobList', 'StudentJobFavorite', 'StudentResumeList']
  return Array.from(new Set([
    ...whiteList
  ]))
})

// 监听路由变化，调试组件加载
// watch(() => route.name, (newName, oldName) => {
//   console.log('[App.vue] 路由变化:', {
//     from: oldName,
//     to: newName,
//     path: route.path,
//     component: route.name,
//     meta: route.meta
//   })
// }, { immediate: true })
//
// // 监听 Component 的变化
// watch(() => route.path, (newPath) => {
//   console.log('[App.vue] 路由路径变化:', newPath)
//   console.log('[App.vue] 路由对象:', route)
// }, { immediate: true })
//
// // 监听组件加载错误
// watch(() => route.matched, (matched) => {
//   console.log('[App.vue] 路由匹配:', matched)
//   if (matched.length > 0) {
//     const lastMatch = matched[matched.length - 1]
//     if (lastMatch) {
//       console.log('[App.vue] 最后匹配的路由:', {
//         path: lastMatch.path,
//         name: lastMatch.name,
//         component: lastMatch.components?.default ? '存在' : '不存在'
//       })
//     }
//   }
// }, { immediate: true, deep: true })
</script>

<style>
#app {
  width: 100%;
  height: 100%;
}

/* 过渡动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all .25s ease;
}
.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(10px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-10px);
}
</style>
