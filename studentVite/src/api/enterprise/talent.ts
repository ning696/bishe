import request from '@/utils/request'
import type { ApiResponse } from '@/api/types'

export interface TalentRecommendItem {
  id: number
  nickName: string | null
  realName: string | null
  headImage: string | null
  campusId: number | null
  campusName: string | null
  major: string | null
  education: string | null
  grade: string | null
  skills: string | null
  experience: number | null
  expectedSalary: number | null
  expectedLocation: string | null
  matchScore: number | null
  matchReason: string | null
  createTime: string | null
  updateTime: string | null
}

export interface TalentRecommendParams {
  jobId: number
  limit?: number
}

export function fetchTalentRecommendations(
  params: TalentRecommendParams
): Promise<ApiResponse<TalentRecommendItem[]>> {
  return request({
    url: '/enterprise/talent/recommended',
    method: 'get',
    params
  })
}

