import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface EnterpriseInterviewApplicationItem {
  id: number
  studentId: number
  studentName: string
  studentPhone?: string
  jobId: number
  jobName: string
  resumeId?: number
  applicationStatus: number
  applicationStatusName: string
  applicationTime: string
}

export interface EnterpriseInterviewApplicationQuery {
  pageNum: number
  pageSize: number
  jobId?: number | string
  applicationStatus?: number | string
}

export interface InterviewApplicationHandlePayload {
  applicationId: number
  applicationStatus: number
  handleRemark?: string
}

export interface InterviewArrangePayload {
  applicationId: number
  studentId: number
  jobId: number
  interviewTime: string
  interviewLocation: string
  interviewType?: string
  contactPerson?: string
  contactPhone?: string
  remark?: string
}

export interface EnterpriseInterviewItem {
  id: number
  studentId: number
  studentName: string
  jobId: number
  jobName: string
  applicationId?: number
  interviewTime: string
  interviewLocation: string
  interviewType: string
  interviewStatus: number
  interviewStatusName: string
  contactPerson: string
  contactPhone: string
  createTime: string
}

export interface EnterpriseInterviewQuery {
  pageNum: number
  pageSize: number
  jobId?: number | string
  interviewStatus?: number | string
}

// 查询职位申请列表（job_application表）
export function fetchInterviewApplications(
  params: EnterpriseInterviewApplicationQuery
): Promise<TableData<EnterpriseInterviewApplicationItem>> {
  return request({
    url: '/enterprise/interview/application/list',
    method: 'get',
    params
  }).then((res: any) => {
    // 后端返回 R<TableDataInfo>，数据在 data 字段中
    // 提取 data 部分，如果没有 data 则返回整个 res（保持兼容性）
    return res.data || res
  })
}

// 处理面试申请（通过/拒绝）
export function handleInterviewApplication(
  payload: InterviewApplicationHandlePayload
): Promise<ApiResponse<void>> {
  return request({
    url: '/enterprise/interview/application/handle',
    method: 'put',
    data: payload
  })
}

// 安排面试（创建interview记录）
export function arrangeInterview(
  payload: InterviewArrangePayload
): Promise<ApiResponse<number>> {
  return request({
    url: '/enterprise/interview/arrange',
    method: 'post',
    data: payload
  })
}

// 查询面试列表（interview表）
export function fetchInterviewList(
  params: EnterpriseInterviewQuery
): Promise<TableData<EnterpriseInterviewItem>> {
  return request({
    url: '/enterprise/interview/list',
    method: 'get',
    params
  }).then((res: any) => {
    // 后端返回 R<TableDataInfo>，数据在 data 字段中
    // 提取 data 部分，如果没有 data 则返回整个 res（保持兼容性）
    return res.data || res
  })
}


