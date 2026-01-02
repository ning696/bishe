import request from '@/utils/request'
import type { TableData } from '@/api/types'

export interface EnterpriseResumeItem {
  id: number
  studentId: number
  studentName: string
  studentPhone: string
  jobId: number
  jobName: string
  resumeId: number
  resumeName: string
  resumeFile: string
  applicationStatus: number
  applicationStatusName: string
  applicationTime: string
}

export interface ResumeListQuery {
  pageNum: number
  pageSize: number
  jobId?: number | string
  applicationStatus?: number | string
  keyword?: string
}

export function fetchResumeList(
  params: ResumeListQuery
): Promise<TableData<EnterpriseResumeItem>> {
  return request({
    url: '/enterprise/resume/list',
    method: 'get',
    params
  }).then((res: any) => {
    // 后端返回 R<TableDataInfo>，数据在 data 字段中
    // 提取 data 部分，如果没有 data 则返回整个 res（保持兼容性）
    return res.data || res
  })
}

export interface EnterpriseResumeDetail {
  id: number
  studentId: number
  studentName?: string
  studentPhone?: string
  studentEmail?: string
  resumeName: string
  resumeFile: string | null
  personalInfo: string | null
  educationBackground: string | null
  workExperience: string | null
  projectExperience: string | null
  skills: string | null
  selfIntroduction: string | null
  deliveryStatus?: number
  deliveryStatusName?: string
  viewTime?: string
}

export function fetchResumeDetail(
  resumeId: number,
  applicationId?: number
): Promise<{ code: number; msg: string; data: EnterpriseResumeDetail }> {
  return request({
    url: '/enterprise/resume/detail',
    method: 'get',
    params: {
      resumeId,
      applicationId
    }
  })
}


