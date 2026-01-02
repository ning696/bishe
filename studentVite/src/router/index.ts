import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import studentRoutes from './modules/student'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/student/login'
  },
  ...studentRoutes,
  {
    path: '/:pathMatch(.*)*',
    redirect: '/student/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

