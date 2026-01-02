import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface SubmitComplaintParams {
  studentId: number
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
  studentId: number
  studentName?: string
  jobId?: number | null
  jobName?: string
  title: string
  content: string
  handleStatus: number
  handleStatusName?: string
  handleResult?: string | null
  handleTime?: string | null
  createTime?: string
}

export function submitComplaint(
  params: SubmitComplaintParams
): Promise<ApiResponse<number>> {
  return request({
    url: '/enterprise/complaint/add',
    method: 'post',
    data: params
  })
}

export function fetchComplaintList(
  query: ComplaintListQuery
): Promise<ApiResponse<TableData<ComplaintListItem>>> {
  return request({
    url: '/enterprise/complaint/list',
    method: 'get',
    params: query
  })
}






