import request from '@/utils/request'

export interface OverviewResponse {
  code: number
  msg: string
  data: {
    totalUsers: number
    totalJobs: number
    totalApplications: number
    totalInterviews: number
    totalConsultations: number
    totalComplaints: number
    todayActiveUsers: number
    todayNewUsers: number
    todayNewJobs: number
    todayNewApplications: number
  }
}

/**
 * 获取管理员概览统计
 */
export function getOverviewa(): Promise<OverviewResponse> {
    return request({
    url: '/admin/statistics/overview',
    method: 'get'
  })
}

