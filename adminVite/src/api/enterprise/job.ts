import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface EnterpriseJobListQuery {
  pageNum: number
  pageSize: number
  status?: number | null
  jobName?: string
}

export interface EnterpriseJobListItem {
  id: number
  jobName: string
  workLocation: string
  salaryMin: number | null
  salaryMax: number | null
  salaryType: string | null
  recruitCount: number | null
  viewCount: number | null
  applyCount: number | null
  status: number
  statusName: string
  publishTime: string | null
  createTime: string | null
}

export interface EnterpriseJobDetail {
  id: number
  categoryId: number | null
  categoryName: string | null
  jobName: string
  jobDescription: string | null
  requiredMajor: string | null
  requiredSkills: string | null
  requiredEducation: string | null
  requiredExperience: number | null
  workLocation: string
  salaryMin: number | null
  salaryMax: number | null
  salaryType: string | null
  jobType: string | null
  recruitCount: number | null
  viewCount: number | null
  applyCount: number | null
  status: number
  statusName: string
  auditRemark: string | null
  publishTime: string | null
  expireTime: string | null
  createTime: string | null
}

export interface EnterpriseJobPayload {
  categoryId?: number | null
  jobName: string
  jobDescription: string
  requiredMajor?: string | null
  requiredSkills?: string | null
  requiredEducation?: string | null
  requiredExperience?: number | null
  workLocation: string
  salaryMin?: number | null
  salaryMax?: number | null
  salaryType?: string | null
  jobType?: string | null
  recruitCount?: number | null
  expireTime?: string | null
  campusIds?: number[]
}

export interface EnterpriseJobUpdatePayload extends EnterpriseJobPayload {
  jobId: number
}

export interface CampusListQuery {
  pageNum: number
  pageSize: number
  campusName?: string
}

export interface CampusItem {
  id: number
  campusName: string
  campusCode: string | null
  address: string | null
  contactPerson: string | null
  contactPhone: string | null
  status: number | null
  statusName?: string | null
}

export interface CampusJobRelation {
  id: number
  campusId: number
  campusName: string
  jobId: number
  jobName: string
}

export function fetchEnterpriseJobList(
  query: EnterpriseJobListQuery
): Promise<ApiResponse<TableData<EnterpriseJobListItem>>> {
  return request({
    url: '/enterprise/job/list',
    method: 'get',
    params: query
  })
}

export function fetchEnterpriseJobDetail(
  jobId: number
): Promise<ApiResponse<EnterpriseJobDetail>> {
  return request({
    url: '/enterprise/job/detail',
    method: 'get',
    params: { jobId }
  })
}

export function createEnterpriseJob(
  payload: EnterpriseJobPayload
): Promise<ApiResponse<number>> {
  return request({
    url: '/enterprise/job/add',
    method: 'post',
    data: payload
  })
}

export function updateEnterpriseJob(
  payload: EnterpriseJobUpdatePayload
): Promise<ApiResponse<void>> {
  return request({
    url: '/enterprise/job/edit',
    method: 'put',
    data: payload
  })
}

export function deleteEnterpriseJob(jobId: number): Promise<ApiResponse<void>> {
  return request({
    url: '/enterprise/job/delete',
    method: 'delete',
    params: { jobId }
  })
}

export function offlineEnterpriseJob(
  jobId: number
): Promise<ApiResponse<void>> {
  return request({
    url: '/enterprise/job/offline',
    method: 'put',
    params: { jobId }
  })
}

export function fetchCampusList(
  query: CampusListQuery
): Promise<ApiResponse<TableData<CampusItem>>> {
  return request({
    url: '/enterprise/campus/list',
    method: 'get',
    params: query
  })
}

export function fetchJobCampusRelations(
  jobId: number
): Promise<ApiResponse<CampusJobRelation[]>> {
  return request({
    url: '/enterprise/campus/job/list',
    method: 'get',
    params: { jobId }
  })
}

