import type { RouteRecordRaw } from 'vue-router'

const studentRoutes: RouteRecordRaw[] = [
  {
    path: '/student',
    redirect: '/student/dashboard'
  },
  {
    path: '/student/login',
    name: 'StudentLogin',
    component: () => import('@/views/student/login/index.vue'),
    meta: {
      layout: false,
      title: '学生登录'
    }
  },
  {
    path: '/student/register',
    name: 'StudentRegister',
    component: () => import('@/views/student/register/index.vue'),
    meta: {
      layout: false,
      title: '学生注册'
    }
  },
  {
    path: '/student/dashboard',
    name: 'StudentDashboard',
    component: () => import('@/views/student/dashboard/index.vue'),
    meta: {
      layout: 'default',
      title: '学生首页',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/jobs',
    name: 'StudentJobList',
    component: () => import('@/views/student/job/List.vue'),
    meta: {
      layout: 'default',
      title: '职位列表',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/job/:id',
    name: 'StudentJobDetail',
    component: () => import('@/views/student/job/Detail.vue'),
    meta: {
      layout: 'default',
      title: '职位详情',
      requiresAuth: true,
      role: 'student',
      activeMenu: '/student/jobs',
      hidden: true
    }
  },
  {
    path: '/student/job/favorite',
    name: 'StudentJobFavorite',
    component: () => import('@/views/student/job/Favorite.vue'),
    meta: {
      layout: 'default',
      title: '我的收藏',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/resume',
    name: 'StudentResumeList',
    component: () => import('@/views/student/resume/List.vue'),
    meta: {
      layout: 'default',
      title: '我的简历',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/resume/edit/:id?',
    name: 'StudentResumeEdit',
    component: () => import('@/views/student/resume/Edit.vue'),
    meta: {
      layout: 'default',
      title: '编辑简历',
      requiresAuth: true,
      role: 'student',
      activeMenu: '/student/resume',
      hidden: true
    }
  },
  {
    path: '/student/resume/delivery',
    name: 'StudentResumeDelivery',
    component: () => import('@/views/student/resume/Delivery.vue'),
    meta: {
      layout: 'default',
      title: '投递简历',
      requiresAuth: true,
      role: 'student',
      activeMenu: '/student/resume',
      hidden: true
    }
  },
  {
    path: '/student/interview/application',
    name: 'StudentInterviewApplication',
    component: () => import('@/views/student/interview/ApplicationList.vue'),
    meta: {
      layout: 'default',
      title: '面试申请',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/interview/application/list',
    name: 'StudentInterviewApplicationList',
    component: () => import('@/views/student/interview/ApplicationList.vue'),
    meta: {
      layout: 'default',
      title: '面试申请',
      requiresAuth: true,
      role: 'student',
      activeMenu: '/student/interview/application'
    }
  },
  {
    path: '/student/interview/apply',
    name: 'StudentInterviewApply',
    component: () => import('@/views/student/interview/Apply.vue'),
    meta: {
      layout: 'default',
      title: '申请面试',
      requiresAuth: true,
      role: 'student',
      activeMenu: '/student/jobs',
      hidden: true
    }
  },
  {
    path: '/student/profile',
    name: 'StudentProfile',
    component: () => import('@/views/student/personal/profile/index.vue'),
    meta: {
      layout: 'default',
      title: '个人信息',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/profile/edit',
    name: 'StudentProfileEdit',
    component: () => import('@/views/student/personal/profile/edit.vue'),
    meta: {
      layout: 'default',
      title: '编辑信息',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/profile/change-password',
    name: 'StudentChangePassword',
    component: () => import('@/views/student/personal/profile/change-password.vue'),
    meta: {
      layout: 'default',
      title: '修改密码',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/personal/application/list',
    name: 'StudentPersonalApplicationList',
    component: () => import('@/views/student/personal/PersonalApplicationList.vue'),
    meta: {
      layout: 'default',
      title: '职位申请',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/chat/session/:id',
    name: 'StudentChatSession',
    component: () => import('@/views/student/chat/Session.vue'),
    meta: {
      layout: 'default',
      title: '即时聊天',
      requiresAuth: true,
      role: 'student',
      hidden: true
    }
  },
  {
    path: '/student/consultation',
    name: 'StudentConsultation',
    component: () => import('@/views/student/consultation/index.vue'),
    meta: {
      layout: 'default',
      title: '线上咨询',
      requiresAuth: true,
      role: 'student'
    }
  },
  {
    path: '/student/complaint',
    name: 'StudentComplaint',
    component: () => import('@/views/student/complaint/index.vue'),
    meta: {
      layout: 'default',
      title: '我的投诉',
      requiresAuth: true,
      role: 'student'
    }
  }
]

export default studentRoutes

