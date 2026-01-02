import type { Router } from 'vue-router'
import { storeToRefs } from 'pinia'
import { getToken } from '@/utils/cookie'
import { useUserStore } from '@/store/modules/user'
import { ElMessage } from 'element-plus'

// 白名单路由
const whiteList = [
  '/admin/login', 
  '/enterprise/login', 
  '/student/login',
  '/admin/register',
  '/enterprise/register',
  '/student/register'
]

// 根据路径判断角色
function getRoleFromPath(path: string): 'admin' | 'enterprise' | 'student' | null {
  if (path.startsWith('/admin')) {
    return 'admin'
  } else if (path.startsWith('/enterprise')) {
    return 'enterprise'
  } else if (path.startsWith('/student')) {
    return 'student'
  }
  return null
}

// 获取对应角色的登录页路径
function getLoginPath(role: 'admin' | 'enterprise' | 'student'): string {
    console.error('test001')
  return `/${role}/login`
}

export function setupRouterGuard(router: Router) {
  router.beforeEach((to, from, next) => {
    // console.log('[Router Guard] 路由守卫触发:', {
    //   from: from.path,
    //   to: to.path,
    //   toName: to.name
    // })
    //
    const token = getToken()
    const userStore = useUserStore()
    const { role } = storeToRefs(userStore)
    const pathRole = getRoleFromPath(to.path)
    const metaRole = (to.meta?.role as 'admin' | 'enterprise' | 'student' | undefined) || null
    // 优先使用路由 meta.role，其次回退到路径前缀判断
    const requiredRole = (metaRole || pathRole) as 'admin' | 'enterprise' | 'student' | null
    
    // console.log('[Router Guard] 路由信息:', {
    //   token: token ? '存在' : '不存在',
    //   userRole: role.value,
    //   pathRole,
    //   isInWhiteList: whiteList.includes(to.path)
    // })

    // 判断是否在白名单中
    const isInWhiteList = whiteList.includes(to.path)

    // 如果有 Token
    if (token) {
      // 如果 role 为空，尝试从路由元信息或路径恢复角色（刷新页面后的情况）
      if (!role.value && requiredRole) {
        userStore.setUserRole(requiredRole)
      }

      // 如果访问登录页，跳转到对应的 Dashboard
      if (isInWhiteList && to.path.endsWith('/login')) {
        if (pathRole) {
          next(`/${pathRole}/dashboard`)
        } else {
          next('/admin/dashboard')
        }
        return
      }

      // 检查角色是否匹配（优先用 meta.role）
      // if (requiredRole && role.value && role.value !== requiredRole) {
      //   ElMessage.warning('权限不足，请重新登录')
      //   userStore.logout()
      //     console.error('test002')
      //     next(getLoginPath(requiredRole))
      //   return
      // }

      // 已登录，允许访问
      // console.log('[Router Guard] 已登录，允许访问:', to.path)
      next()
    } else {
      // 没有 Token
      // 如果在白名单中，允许访问
      if (isInWhiteList) {
        // console.log('[Router Guard] 白名单路由，允许访问:', to.path)
        next()
        return
      }

      // 需要登录，跳转到对应角色的登录页
      const loginPath = pathRole ? getLoginPath(pathRole) : '/admin/login'
        console.error('test003'+loginPath)

        // console.log('[Router Guard] 未登录，跳转到登录页:', loginPath)
      if (pathRole) {
        next(getLoginPath(pathRole))
      } else {
        next('/admin/login')
      }
    }
  })
  
  // router.afterEach((to, from) => {
  //   console.log('[Router Guard] 路由导航完成:', {
  //     from: from.path,
  //     to: to.path,
  //     toName: to.name
  //   })
  // })
}

