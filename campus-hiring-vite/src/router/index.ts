import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import adminRoutes from './modules/admin'
import enterpriseRoutes from './modules/enterprise'
import studentRoutes from './modules/student'

const routes: RouteRecordRaw[] = [
  // {
  //   path: '/',
  //   redirect: '/admin/login'
  // },
  ...adminRoutes,
  ...enterpriseRoutes,
  ...studentRoutes,
  // {
  //   path: '/:pathMatch(.*)*',
  //   redirect: '/admin/login'
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

