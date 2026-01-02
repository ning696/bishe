export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

export interface TableData<T = any> {
  total: number
  rows: T[]
  code?: number
  msg?: string
}

