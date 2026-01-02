import request from '@/utils/request'
import type { TableData } from '@/api/types'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResponse {
  code: number
  msg: string
  data: string // JWT Token
}

export interface RegisterParams {
  username: string
  password: string
}

export interface StudentInfo {
  nickName?: string
  headImage?: string
}

export interface StudentDetail {
  id: number
  username: string
  nickName?: string
  realName?: string
  phone?: string
  email?: string
  headImage?: string
  gender?: number
  genderName?: string
  birthday?: string
  campusId?: number
  campusName?: string
  major?: string
  education?: string
  educationName?: string
  grade?: string
  skills?: string
  experience?: number
  expectedSalary?: number
  expectedLocation?: string
  status?: number
  statusName?: string
  createTime?: string
}

export interface UpdateInfoParams {
  nickName?: string
  realName?: string
  phone?: string
  email?: string
  gender?: number
  birthday?: string
  campusId?: number
  major?: string
  education?: string
  grade?: string
  skills?: string
  experience?: number
  expectedSalary?: number
  expectedLocation?: string
}

export interface ChangePasswordParams {
  oldPassword: string
  newPassword: string
}

export interface UpdateHeadImageParams {
  headImage: string
}

export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

export interface UploadStudentFileResponse {
  name: string
  success: boolean
}

export interface CampusOption {
  id: number
  campusName: string
}

/**
 * 学生注册
 */
export function register(params: RegisterParams): Promise<ApiResponse> {
  return request({
    url: '/student/student/register',
    method: 'post',
    data: params
  })
}

/**
 * 学生登录
 */
export function login(params: LoginParams): Promise<LoginResponse> {
  return request({
    url: '/student/student/login',
    method: 'post',
    data: params
  })
}

/**
 * 获取学生信息
 */
export function getInfo(): Promise<ApiResponse<StudentInfo>> {
  return request({
    url: '/student/student/info',
    method: 'get'
  })
}

/**
 * 获取学生详情
 */
export function getDetail(): Promise<ApiResponse<StudentDetail>> {
  return request({
    url: '/student/student/detail',
    method: 'get'
  })
}

/**
 * 更新学生信息
 */
export function updateInfo(params: UpdateInfoParams): Promise<ApiResponse> {
  return request({
    url: '/student/student/edit',
    method: 'put',
    data: params
  })
}

/**
 * 修改密码
 */
export function changePassword(params: ChangePasswordParams): Promise<ApiResponse> {
  return request({
    url: '/student/student/changePassword',
    method: 'put',
    data: params
  })
}

/**
 * 更新头像
 */
export function updateHeadImage(params: UpdateHeadImageParams): Promise<ApiResponse> {
  return request({
    url: '/student/student/headImage/update',
    method: 'put',
    data: params
  })
}

/**
 * 上传学生相关文件（如简历附件）
 */
export function uploadStudentFile(file: File): Promise<ApiResponse<UploadStudentFileResponse>> {
  const formData = new FormData()
  formData.append('file', file)

  return request({
    url: '/student/student/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}

/**
 * 学生端获取校园选项（模糊搜索）
 */
export function fetchCampusOptions(keyword = ''): Promise<ApiResponse<TableData<CampusOption>>> {
  return request({
    url: '/student/campus/list',
    method: 'get',
    params: {
      keyword,
      pageNum: 1,
      pageSize: 20
    }
  })
}

