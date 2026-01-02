import request from '@/utils/request'

export interface AnalysisParams {
  startDate?: string
  endDate?: string
}

export interface TopPerformingJob {
  jobId: number
  jobTitle: string
  views: number
  applications: number
  conversionRate: number
}

export interface Recommendation {
  type: string
  content: string
  priority: 'high' | 'medium' | 'low'
}

export interface AnalysisResponse {
  code: number
  msg: string
  data: {
    totalJobs: number
    totalViews: number
    totalApplications: number
    totalInterviews: number
    totalHires: number
    conversionRate: number
    topPerformingJobs: TopPerformingJob[]
    recommendations: Recommendation[]
  }
}

/**
 * 获取企业数据分析
 */
export function getAnalysis(params?: AnalysisParams): Promise<AnalysisResponse> {
  return request({
    url: '/enterprise/statistics/analysis',
    method: 'get',
    params
  })
}

