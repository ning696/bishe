import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

export interface PersonalApplicationItem {
	id: number
	jobId: number
	jobName: string
	enterpriseId: number
	enterpriseName: string
	applicationStatus: number
	applicationStatusName: string
	applicationTime: string
}

export interface PersonalApplicationQuery {
	pageNum: number
	pageSize: number
	status?: number
}

export function fetchPersonalApplicationList(
	params: PersonalApplicationQuery
): Promise<ApiResponse<TableData<PersonalApplicationItem>>> {
	return request({
		url: '/student/personal/application/list',
		method: 'get',
		params
	})
}


