import request from '@/utils/request'
import type { TableData, ApiResponse } from '@/api/types'

export interface StudentQueryParams {
  pageNum?: number
  pageSize?: number
  status?: number | string
  keyword?: string
}

export interface StudentListItem {
  id: number
  username: string
  nickName?: string
  phone?: string
  email?: string
  status?: number
  statusName?: string
  createTime?: string
}

export interface StudentDetailVO {
  id: number
  username: string
  nickName?: string
  realName?: string
  phone?: string
  email?: string
  headImage?: string
  gender?: number
  birthday?: string
  campusId?: number
  campusName?: string
  major?: string
  education?: string
  grade?: string
  skills?: string
  experience?: string
  expectedSalary?: string
  expectedLocation?: string
  status?: number
  statusName?: string
  createTime?: string
}

export interface UpdateStudentStatusDTO {
  studentId: number
  status: number
}

export function fetchStudentList(params: StudentQueryParams): Promise<TableData<StudentListItem>> {
  return request({
    url: '/admin/student/list',
    method: 'get',
    params
  }).then((res: any) => res.data || res)
}

export function fetchStudentDetail(studentId: number): Promise<ApiResponse<StudentDetailVO>> {
  return request({
    url: '/admin/student/detail',
    method: 'get',
    params: { studentId }
  })
}

export function updateStudentStatus(data: UpdateStudentStatusDTO): Promise<ApiResponse<void>> {
  return request({
    url: '/admin/student/updateStatus',
    method: 'put',
    data
  })
}

