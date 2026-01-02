import request from '@/utils/request'
import type { ApiResponse } from '@/api/types'

export interface CreateSessionDTO {
  enterpriseId: number
  jobId?: number
}

export interface ChatSessionDetailVO {
  id: number
  enterpriseId?: number
  enterpriseName?: string
  enterpriseLogo?: string
  jobId?: number
  jobName?: string
  isOnline?: boolean
}

export function createOrGetSession(
  data: CreateSessionDTO
): Promise<ApiResponse<ChatSessionDetailVO>> {
  return request({
    url: '/student/chat/session/create',
    method: 'post',
    data
  })
}


