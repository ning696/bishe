import type { RouteRecordRaw } from 'vue-router'

const adminRoutes: RouteRecordRaw[] = [
  {
    path: '/admin',
    redirect: '/admin/dashboard'
  },
  {
    path: '/admin/profile',
    name: 'AdminProfile',
    component: () => import('@/views/admin/profile/index.vue'),
    meta: {
      layout: 'default',
      title: '管理员信息',
      requiresAuth: true,
      role: 'admin'
    }
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/login/index.vue'),
    meta: {
      layout: false,
      title: '管理员登录'
    }
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('@/views/admin/dashboard/index.vue'),
    meta: {
      layout: 'default',
      title: '管理员首页',
      requiresAuth: true,
      role: 'admin'
    }
  },
  {
    path: '/admin/student/list',
    name: 'AdminStudentList',
    component: () => import('@/views/admin/student/List.vue'),
    meta: {
      layout: 'default',
      title: '学生管理',
      requiresAuth: true,
      role: 'admin'
    }
  },
  {
    path: '/admin/enterprise/list',
    name: 'AdminEnterpriseList',
    component: () => import('@/views/admin/enterprise/List.vue'),
    meta: {
      layout: 'default',
      title: '企业管理',
      requiresAuth: true,
      role: 'admin'
    }
  },
  {
    path: '/admin/job/list',
    name: 'AdminJobList',
    component: () => import('@/views/admin/job/List.vue'),
    meta: {
      layout: 'default',
      title: '职位管理',
      requiresAuth: true,
      role: 'admin'
    }
  },
  {
    path: '/admin/campus/list',
    name: 'AdminCampusList',
    component: () => import('@/views/admin/campus/List.vue'),
    meta: {
      layout: 'default',
      title: '校园管理',
      requiresAuth: true,
      role: 'admin'
    }
  },
  {
    path: '/admin/complaint/list',
    name: 'AdminComplaintList',
    component: () => import('@/views/admin/complaint/List.vue'),
    meta: {
      layout: 'default',
      title: '投诉管理',
      requiresAuth: true,
      role: 'admin'
    }
  }
]

export default adminRoutes

