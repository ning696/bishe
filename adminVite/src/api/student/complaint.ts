import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface SubmitComplaintParams {
  enterpriseId: number
  jobId?: number | null
  title: string
  content: string
  attachment?: string
}

export interface ComplaintListQuery {
  pageNum?: number
  pageSize?: number
  handleStatus?: number
}

export interface ComplaintListItem {
  id: number
  enterpriseId: number
  enterpriseName?: string
  jobId?: number | null
  jobName?: string
  title: string
  content: string
  attachment?: string | null
  handleResult?: string | null
  handleRemark?: string | null
  handleStatus: number
  handleStatusName?: string
  handleTime?: string | null
  createTime?: string
}

export function submitComplaint(
  params: SubmitComplaintParams
): Promise<ApiResponse<number>> {
  return request({
    url: '/student/complaint/add',
    method: 'post',
    data: params
  })
}

export function fetchComplaintList(
  query: ComplaintListQuery
): Promise<ApiResponse<TableData<ComplaintListItem>>> {
  return request({
    url: '/student/complaint/list',
    method: 'get',
    params: query
  })
}

