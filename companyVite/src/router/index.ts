import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import enterpriseRoutes from './modules/enterprise'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/enterprise/login'
  },
  ...enterpriseRoutes,
  {
    path: '/:pathMatch(.*)*',
    redirect: '/enterprise/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

