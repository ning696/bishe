import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken, getRole, setRole, removeRole, getUserInfo as getUserInfoFromCookie, setUserInfo as setUserInfoCookie, removeUserInfo } from '@/utils/cookie'

export interface UserInfo {
  id?: number
  username?: string
  name?: string
  avatar?: string
  phone?: string
  email?: string
  [key: string]: any
}

export type UserRole = 'admin' | 'enterprise' | 'student'

export const useUserStore = defineStore('user', () => {
  // 状态 - 从 cookie 恢复 token、role 和 userInfo
  const token = ref<string | undefined>(getToken())
  const userInfo = ref<UserInfo | null>(getUserInfoFromCookie())
  const role = ref<UserRole | null>(getRole() || null)

  // 设置 Token
  const setUserToken = (newToken: string) => {
    token.value = newToken
    setToken(newToken)
  }

  // 设置用户信息（同时持久化到 cookie）
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    setUserInfoCookie(info)
  }

  // 设置角色（同时持久化到 cookie）
  const setUserRole = (newRole: UserRole) => {
    role.value = newRole
    setRole(newRole)
  }

  // 获取用户信息（从 cookie 读取）
  const getUserInfo = (): UserInfo | null => {
    return userInfo.value
  }

  // 退出登录
  const logout = () => {
    token.value = undefined
    userInfo.value = null
    role.value = null
    console.error('test005')

    removeToken()
    removeRole()
    removeUserInfo()
  }

  return {
    token,
    userInfo,
    role,
    setUserToken,
    setUserInfo,
    setUserRole,
    getUserInfo,
    logout
  }
})

