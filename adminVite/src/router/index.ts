import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import adminRoutes from './modules/admin'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/admin/login'
  },
  ...adminRoutes,
  {
    path: '/:pathMatch(.*)*',
    redirect: '/admin/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

