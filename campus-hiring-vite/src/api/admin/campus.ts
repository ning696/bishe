import request from '@/utils/request'
import type { TableData, ApiResponse } from '@/api/types'

export interface CampusQueryParams {
  pageNum?: number
  pageSize?: number
  status?: number | string
  keyword?: string
}

export interface CampusVO {
  id: number
  campusName: string
  campusCode?: string
  address?: string
  contactPerson?: string
  contactPhone?: string
  status?: number
  statusName?: string
  createTime?: string
}

export interface CampusDetailVO extends CampusVO {
  remark?: string
  createBy?: number
  updateBy?: number
  updateTime?: string
}

export interface CampusAddDTO {
  campusName: string
  campusCode?: string
  address?: string
  contactPerson?: string
  contactPhone?: string
  status?: number
  remark?: string
}

export interface CampusUpdateDTO {
  id: number
  campusName: string
  campusCode?: string
  address?: string
  contactPerson?: string
  contactPhone?: string
  status?: number
  remark?: string
}

export function getCampusPage(params: CampusQueryParams): Promise<TableData<CampusVO>> {
  return request({
    url: '/admin/campus/list',
    method: 'get',
    params
  }).then((res: any) => {
    // 后端返回 R<TableDataInfo>，数据在 data 字段中
    return res.data || res
  })
}

export function getCampusDetail(params: { campusId: number }): Promise<ApiResponse<CampusDetailVO>> {
  return request({
    url: '/admin/campus/detail',
    method: 'get',
    params
  })
}

export function addCampus(data: CampusAddDTO): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/campus/add',
    method: 'post',
    data
  })
}

export function updateCampus(data: CampusUpdateDTO): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/campus/update',
    method: 'put',
    data
  })
}

export function deleteCampus(params: { campusId: number }): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/campus/delete',
    method: 'delete',
    params
  })
}

