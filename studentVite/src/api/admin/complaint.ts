import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface ComplaintListQuery {
  pageNum?: number
  pageSize?: number
  handleStatus?: number | string
  complaintType?: number | string
}

export interface ComplaintListItem {
  id: number
  complaintType: number
  complaintTypeName?: string
  complainerId: number
  complainerName?: string
  complainedId: number
  complainedName?: string
  title: string
  content: string
  handleStatus: number
  handleStatusName?: string
  createTime?: string
}

export interface ComplaintDetail {
  id: number
  complaintType: number
  complaintTypeName?: string
  complainerId: number
  complainerName?: string
  complainedId: number
  complainedName?: string
  jobId?: number | null
  jobName?: string | null
  title: string
  content: string
  attachment?: string | null
  handleStatus: number
  handleStatusName?: string
  handleResult?: string | null
  handleRemark?: string | null
  handleTime?: string | null
  handleBy?: number | null
  createTime?: string
}

export interface ComplaintHandleParams {
  complaintId: number
  handleStatus: number
  handleResult?: string
  handleRemark?: string
}

export function fetchComplaintList(
  query: ComplaintListQuery
): Promise<TableData<ComplaintListItem>> {
  return request({
    url: '/admin/complaint/list',
    method: 'get',
    params: query
  }).then((res: any) => res.data || res)
}

export function fetchComplaintDetail(
  complaintId: number
): Promise<ApiResponse<ComplaintDetail>> {
  return request({
    url: '/admin/complaint/detail',
    method: 'get',
    params: { complaintId }
  })
}

export function handleComplaint(
  params: ComplaintHandleParams
): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/complaint/handle',
    method: 'put',
    data: params
  })
}

