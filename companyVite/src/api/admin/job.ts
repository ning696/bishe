import request from '@/utils/request'
import type { TableData, ApiResponse } from '@/api/types'

export interface JobQueryParams {
  pageNum?: number
  pageSize?: number
  status?: number | string
  enterpriseId?: number
  jobName?: string
}

export interface JobVO {
  id: number
  enterpriseId: number
  enterpriseName?: string
  jobName: string
  city?: string
  salaryMin?: number
  salaryMax?: number
  status?: number
  createTime?: string
}

export interface JobDetailParams {
  id: number
}

export interface JobDetailVO extends JobVO {
  description?: string
  requirement?: string
  education?: string
  experience?: string
}

export interface AuditJobDTO {
  jobId: number
  status: 1 | 2
  auditRemark?: string
}

export function getJobPage(params: JobQueryParams): Promise<TableData<JobVO>> {
  return request({
    url: '/admin/job/list',
    method: 'get',
    params
  }).then((res: any) => {
    // 后端返回 R<TableDataInfo>，数据在 data 字段中
    // 提取 data 部分，如果没有 data 则返回整个 res（保持兼容性）
    return res.data || res
  })
}

export function getJobDetail(params: JobDetailParams): Promise<ApiResponse<JobDetailVO>> {
  return request({
    url: '/admin/job/detail',
    method: 'get',
    params
  })
}

export function auditJob(data: AuditJobDTO): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/job/audit',
    method: 'put',
    data
  })
}


