import Cookies from 'js-cookie'
import type { UserInfo } from '@/store/modules/user'

const TokenKey = 'Campus-Hiring-Enterprise-Token'
const RoleKey = 'Campus-Hiring-Enterprise-Role'
const UserInfoKey = 'Campus-Hiring-Enterprise-UserInfo'

/**
 * 获取 Token
 */
export function getToken(): string | undefined {
  return Cookies.get(TokenKey)
}

/**
 * 设置 Token
 * @param token Token 值
 */
export function setToken(token: string): void {
  Cookies.set(TokenKey, token)
}

/**
 * 移除 Token
 */
export function removeToken(): void {
  Cookies.remove(TokenKey)
}

/**
 * 获取角色
 */
export function getRole(): 'admin' | 'enterprise' | 'student' | undefined {
  const role = Cookies.get(RoleKey)
  if (role === 'admin' || role === 'enterprise' || role === 'student') {
    return role
  }
  return undefined
}

/**
 * 设置角色
 * @param role 角色值
 */
export function setRole(role: 'admin' | 'enterprise' | 'student'): void {
  Cookies.set(RoleKey, role)
}

/**
 * 移除角色
 */
export function removeRole(): void {
  Cookies.remove(RoleKey)
}

/**
 * 获取用户信息
 */
export function getUserInfo(): UserInfo | null {
  const userInfoStr = Cookies.get(UserInfoKey)
  if (!userInfoStr) {
    return null
  }
  try {
    return JSON.parse(userInfoStr) as UserInfo
  } catch {
    return null
  }
}

/**
 * 设置用户信息
 * @param userInfo 用户信息对象
 */
export function setUserInfo(userInfo: UserInfo): void {
  Cookies.set(UserInfoKey, JSON.stringify(userInfo))
}

/**
 * 移除用户信息
 */
export function removeUserInfo(): void {
  Cookies.remove(UserInfoKey)
}

