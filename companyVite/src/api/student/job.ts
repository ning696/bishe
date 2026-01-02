import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface JobListQuery {
  pageNum: number
  pageSize: number
  jobName?: string
  categoryId?: number | null
  workLocation?: string
  jobType?: string
  requiredEducation?: string
  salaryMin?: number | null
  salaryMax?: number | null
  sortField?: string
  sortOrder?: 'asc' | 'desc'
}

export interface StudentJobListItem {
  id: number
  enterpriseId: number
  enterpriseName: string
  jobName: string
  workLocation: string
  salaryMin: number | null
  salaryMax: number | null
  salaryType: string | null
  jobType: string | null
  recruitCount: number | null
  viewCount: number | null
  applyCount: number | null
  publishTime: string | null
  createTime: string | null
  isFavorite: boolean | null
}

export interface JobRecommendItem extends StudentJobListItem {
  matchScore?: number | null
}

export interface StudentJobDetail {
  id: number
  enterpriseId: number
  enterpriseName: string
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
  publishTime: string | null
  expireTime: string | null
  createTime: string | null
  isFavorite: boolean | null
  isApplied: boolean | null
}

export interface FavoriteJobListItem {
  id: number // 职位ID
  enterpriseId: number
  jobName: string
  enterpriseName: string
  workLocation: string
  salaryMin: number | null
  salaryMax: number | null
  salaryType: string | null
  jobType: string | null
  recruitCount: number | null
  viewCount: number | null
  applyCount: number | null
  publishTime: string | null
  createTime: string | null
  isFavorite: boolean | null
  favoriteTime?: string | null // 收藏时间（可选，如果后端返回）
}

export interface FavoriteJobQuery {
  pageNum: number
  pageSize: number
}

export function fetchStudentJobList(
  query: JobListQuery
): Promise<ApiResponse<TableData<StudentJobListItem>>> {
  return request({
    url: '/student/job/list',
    method: 'post',
    data: query
  })
}

export function fetchStudentJobDetail(
  jobId: number
): Promise<ApiResponse<StudentJobDetail>> {
  return request({
    url: `/student/job/detail/${jobId}`,
    method: 'get'
  })
}

export function favoriteStudentJob(jobId: number): Promise<ApiResponse<void>> {
  return request({
    url: '/student/job/favorite',
    method: 'post',
    data: { jobId }
  })
}

export function unfavoriteStudentJob(jobId: number): Promise<ApiResponse<void>> {
  return request({
    url: `/student/job/favorite/${jobId}`,
    method: 'delete'
  })
}

export function fetchStudentFavoriteJobList(
  query: FavoriteJobQuery
): Promise<ApiResponse<TableData<FavoriteJobListItem>>> {
  return request({
    url: '/student/job/favorite/list',
    method: 'post',
    data: query
  })
}

export function fetchRecommendedJobs(): Promise<ApiResponse<JobRecommendItem[]>> {
  return request({
    url: '/student/job/recommended',
    method: 'get'
  })
}

