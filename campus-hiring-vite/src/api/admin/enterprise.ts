import request from '@/utils/request'
import type { TableData, ApiResponse } from '@/api/types'

export interface EnterpriseQueryParams {
  pageNum?: number
  pageSize?: number
  status?: number | string
  keyword?: string
}

export interface EnterpriseListItem {
  id: number
  username: string
  enterpriseName?: string
  phone?: string
  email?: string
  status?: number
  statusName?: string
  certificationStatus?: number
  certificationStatusName?: string
  createTime?: string
}

export interface EnterpriseDetailVO {
  id: number
  username: string
  enterpriseName?: string
  legalPerson?: string
  phone?: string
  email?: string
  logo?: string
  address?: string
  industry?: string
  scale?: string
  description?: string
  website?: string
  certificationStatus?: number
  certificationStatusName?: string
  status?: number
  statusName?: string
  createTime?: string
}

export interface UpdateEnterpriseStatusDTO {
  enterpriseId: number
  status: number
}

export function fetchEnterpriseList(params: EnterpriseQueryParams): Promise<TableData<EnterpriseListItem>> {
  return request({
    url: '/admin/enterprise/list',
    method: 'get',
    params
  }).then((res: any) => res.data || res)
}

export function fetchEnterpriseDetail(enterpriseId: number): Promise<ApiResponse<EnterpriseDetailVO>> {
  return request({
    url: '/admin/enterprise/detail',
    method: 'get',
    params: { enterpriseId }
  })
}

export function updateEnterpriseStatus(data: UpdateEnterpriseStatusDTO): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/enterprise/updateStatus',
    method: 'put',
    data
  })
}

