import request from '@/utils/request'
import type { ApiResponse } from '@/api/types'

export interface ResumeListItem {
  id: number
  resumeName: string
  resumeFile: string | null
  isDefault: number
  status: number | null
  createTime: string | null
  updateTime: string | null
}

export interface ResumeDetail {
  id: number
  studentId: number
  resumeName: string
  resumeFile: string | null
  personalInfo: string | null
  educationBackground: string | null
  workExperience: string | null
  projectExperience: string | null
  skills: string | null
  selfIntroduction: string | null
  isDefault: number
  status: number | null
  createTime: string | null
  updateTime: string | null
}

export interface ResumeDeliveryPayload {
  jobId: number
  resumeId: number
}

export interface ResumeCreatePayload {
  resumeName: string
  resumeFile?: string | null
  personalInfo?: string | null
  educationBackground?: string | null
  workExperience?: string | null
  projectExperience?: string | null
  skills?: string | null
  selfIntroduction?: string | null
  isDefault?: number
}

export interface ResumeUpdatePayload extends ResumeCreatePayload {
  resumeId: number
}

export function fetchResumeList(): Promise<ApiResponse<ResumeListItem[]>> {
  return request({
    url: '/student/resume/list',
    method: 'get'
  })
}

export function fetchResumeDetail(
  resumeId: number
): Promise<ApiResponse<ResumeDetail>> {
  return request({
    url: `/student/resume/detail/${resumeId}`,
    method: 'get'
  })
}

export function createResume(
  payload: ResumeCreatePayload
): Promise<ApiResponse<void>> {
  return request({
    url: '/student/resume/add',
    method: 'post',
    data: payload
  })
}

export function updateResume(
  payload: ResumeUpdatePayload
): Promise<ApiResponse<void>> {
  return request({
    url: '/student/resume/update',
    method: 'put',
    data: payload
  })
}

export function deleteResume(resumeId: number): Promise<ApiResponse<void>> {
  return request({
    url: `/student/resume/delete/${resumeId}`,
    method: 'delete'
  })
}

export function deliveryResume(
  payload: ResumeDeliveryPayload
): Promise<ApiResponse<void>> {
  return request({
    url: '/student/resume/delivery',
    method: 'post',
    data: payload
  })
}

