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

/**
 * 企业登录
 */
export function login(params: LoginParams): Promise<LoginResponse> {
  return request({
    url: '/enterprise/enterprise/login',
    method: 'post',
    data: params
  })
}

// ================= 注册与验证码 =================
export interface SendCodeParams {
  mobile?: string
  email?: string
  scene: 'register' | 'resetPassword' | 'certification'
}

export interface BaseResponse<T = any> {
  code: number
  msg: string
  data: T
}

export function sendCode(params: SendCodeParams): Promise<BaseResponse<boolean>> {
  return request({
    url: '/enterprise/enterprise/sendCode',
    method: 'post',
    data: params
  })
}

export interface RegisterParams {
  username: string
  password: string
  enterpriseName: string
}

export function register(params: RegisterParams): Promise<BaseResponse<boolean>> {
  return request({
    url: '/enterprise/enterprise/register',
    method: 'post',
    data: params
  })
}

// ================= 企业资料 =================
export interface EnterpriseProfile {
  id: number
  username: string
  enterpriseName: string
  legalPerson: string
  phone: string
  email: string
  logo?: string
  address?: string
  industry?: string
  scale?: string
  description?: string
  website?: string
  certificationStatus?: number
  certificationStatusName?: string
  certificationFile?: string
  status?: number
  statusName?: string
  createTime?: string
}

export function getProfile(): Promise<BaseResponse<EnterpriseProfile>> {
  return request({
    url: '/enterprise/enterprise/detail',
    method: 'get'
  })
}

export interface UpdateProfileParams {
  companyName?: string
  contactName?: string
  contactMobile?: string
  email?: string
  address?: string
  introduction?: string
}

export function updateProfile(params: UpdateProfileParams): Promise<BaseResponse<boolean>> {
  return request({
    url: '/enterprise/enterprise/edit',
    method: 'put',
    data: params
  })
}

// ================= 修改密码 =================
export interface ChangePasswordParams {
  oldPassword: string
  newPassword: string
}

export function changePassword(params: ChangePasswordParams): Promise<BaseResponse<boolean>> {
  return request({
    url: '/enterprise/enterprise/changePassword',
    method: 'put',
    data: params
  })
}

// ================= 更新Logo =================
export interface UpdateLogoParams {
  logo: string
}

export function updateLogo(params: UpdateLogoParams): Promise<BaseResponse<boolean>> {
  return request({
    url: '/enterprise/enterprise/logo/update',
    method: 'put',
    data: params
  })
}

// ================= 提交认证 =================
export function applyCertification(file: File): Promise<BaseResponse<boolean>> {
  const formData = new FormData()
  formData.append('file', file)

  return request({
    url: '/enterprise/enterprise/certification/apply',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}

// ================= 文件上传 =================
export interface UploadEnterpriseFileResponse {
  name: string
  success: boolean
}

export function uploadEnterpriseFile(file: File): Promise<BaseResponse<UploadEnterpriseFileResponse>> {
  const formData = new FormData()
  formData.append('file', file)

  return request({
    url: '/enterprise/enterprise/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
}

