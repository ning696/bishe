import request from '@/utils/request'

export interface StatisticsOverviewResponse {
  code: number
  msg: string
  data: {
    totalApplications: number
    pendingApplications: number
    passedApplications: number
    totalInterviews: number
    scheduledInterviews: number
    completedInterviews: number
    totalFavorites: number
    totalResumes: number
    defaultResumeId: number | null
    resumeCompleteness: number
    headImage?: string
  }
}

/**
 * 获取学生统计概览
 */
export function getOverview(): Promise<StatisticsOverviewResponse> {
  return request({
    url: '/student/statistics/overview',
    method: 'get'
  })
}

