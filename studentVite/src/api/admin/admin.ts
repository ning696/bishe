import request from '@/utils/request'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResponse {
  code: number
  msg: string
  data: string // JWT Token
}

export interface AdminInfoResponse {
  code: number
  msg: string
  data: {
    id: number
    username: string
    nickname?: string
    avatar?: string
    roles?: string[]
    permissions?: string[]
  }
}

/**
 * 管理员登录
 */
export function login(params: LoginParams): Promise<LoginResponse> {
  return request({
    url: '/admin/admin/login',
    method: 'post',
    data: params
  })
}

/**
 * 获取管理员信息
 */
export function getAdminInfo(): Promise<AdminInfoResponse> {
  return request({
    url: '/admin/admin/info',
    method: 'get'
  })
}

/**
 * 管理员退出登录
 */
export function logout(): Promise<{ code: number; msg: string; data?: any }> {
  return request({
    url: '/admin/admin/logout',
    method: 'delete'
  })
}

