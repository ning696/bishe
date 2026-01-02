import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface InterviewApplicationItem {
  id: number
  jobId: number
  jobName: string
  enterpriseId: number
  enterpriseName: string
  interviewStatus: number | null
  interviewStatusName: string | null
  interviewTime: string | null
  interviewLocation: string | null
  interviewType: string | null
  remark: string | null
  createTime: string | null
}

export interface InterviewApplyPayload {
  jobId: number
  resumeId?: number
}

export function applyInterview(
  payload: InterviewApplyPayload
): Promise<ApiResponse<void>> {
  return request({
    url: '/student/interview/apply',
    method: 'post',
    data: payload
  })
}

export function fetchInterviewApplications(): Promise<
  ApiResponse<InterviewApplicationItem[]>
> {
  return request({
    url: '/student/interview/application/list',
    method: 'get'
  })
}

export interface InterviewApplicationQuery {
  pageNum: number
  pageSize: number
  status?: number
}

export function fetchInterviewApplicationsPage(
  params: InterviewApplicationQuery
): Promise<TableData<InterviewApplicationItem>> {
  return request({
    url: '/student/interview/application/page',
    method: 'get',
    params
  }).then((res: any) => {
    // 后端返回 R<TableDataInfo>，数据在 data 字段中
    // 提取 data 部分，如果没有 data 则返回整个 res（保持兼容性）
    return res.data || res
  })
}

