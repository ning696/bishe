import type { RouteRecordRaw } from 'vue-router'

const enterpriseRoutes: RouteRecordRaw[] = [
  {
    path: '/enterprise',
    redirect: '/enterprise/dashboard'
  },
  {
    path: '/enterprise/resume/list',
    name: 'EnterpriseResumeList',
    component: () => import('@/views/enterprise/resume/ResumeList.vue'),
    meta: {
      layout: 'default',
      title: '职位申请',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/interview/application/list',
    name: 'EnterpriseInterviewApplicationList',
    component: () => import('@/views/enterprise/interview/ApplicationList.vue'),
    meta: {
      layout: 'default',
      title: '面试申请',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/login',
    name: 'EnterpriseLogin',
    component: () => import('@/views/enterprise/login/index.vue'),
    meta: {
      layout: false,
      title: '企业登录'
    }
  },
  {
    path: '/enterprise/dashboard',
    name: 'EnterpriseDashboard',
    component: () => import('@/views/enterprise/dashboard/index.vue'),
    meta: {
      layout: 'default',
      title: '企业首页',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/job',
    name: 'EnterpriseJobList',
    component: () => import('@/views/enterprise/job/List.vue'),
    meta: {
      layout: 'default',
      title: '职位管理',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/job/add',
    name: 'EnterpriseJobAdd',
    component: () => import('@/views/enterprise/job/Add.vue'),
    meta: {
      layout: 'default',
      title: '发布职位',
      requiresAuth: true,
      role: 'enterprise',
      activeMenu: '/enterprise/job'
    }
  },
  {
    path: '/enterprise/job/edit/:id',
    name: 'EnterpriseJobEdit',
    component: () => import('@/views/enterprise/job/Edit.vue'),
    meta: {
      layout: 'default',
      title: '编辑职位',
      requiresAuth: true,
      role: 'enterprise',
      activeMenu: '/enterprise/job',
      hidden: true
    }
  },
  {
    path: '/enterprise/job/:id',
    name: 'EnterpriseJobDetail',
    component: () => import('@/views/enterprise/job/Detail.vue'),
    meta: {
      layout: 'default',
      title: '职位详情',
      requiresAuth: true,
      role: 'enterprise',
      activeMenu: '/enterprise/job',
      hidden: true
    }
  },
  {
    path: '/enterprise/register',
    name: 'EnterpriseRegister',
    component: () => import('@/views/enterprise/register/index.vue'),
    meta: {
      layout: false,
      title: '企业注册'
    }
  },
  {
    path: '/enterprise/profile',
    name: 'EnterpriseProfile',
    component: () => import('@/views/enterprise/profile/index.vue'),
    meta: {
      layout: 'default',
      title: '企业资料',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/profile/edit',
    name: 'EnterpriseProfileEdit',
    component: () => import('@/views/enterprise/profile/edit.vue'),
    meta: {
      layout: 'default',
      title: '编辑企业资料',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/profile/change-password',
    name: 'EnterpriseChangePassword',
    component: () => import('@/views/enterprise/profile/change-password.vue'),
    meta: {
      layout: 'default',
      title: '修改密码',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/profile/logo',
    name: 'EnterpriseUpdateLogo',
    component: () => import('@/views/enterprise/profile/logo.vue'),
    meta: {
      layout: 'default',
      title: '更新Logo',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/profile/certification',
    name: 'EnterpriseCertification',
    component: () => import('@/views/enterprise/profile/certification.vue'),
    meta: {
      layout: 'default',
      title: '企业认证',
      requiresAuth: true,
      role: 'enterprise'
    }
  },
  {
    path: '/enterprise/consultation',
    name: 'EnterpriseConsultation',
    component: () => import('@/views/enterprise/consultation/index.vue'),
    meta: {
      layout: 'default',
      title: '线上咨询',
      requiresAuth: true,
      role: 'enterprise'
    }
  }
]

export default enterpriseRoutes

