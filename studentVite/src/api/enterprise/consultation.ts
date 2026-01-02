import request from '@/utils/request'
import type { ApiResponse, TableData } from '@/api/types'

// ==================== 会话相关接口 ====================

/**
 * 会话信息
 */
export interface SessionInfo {
  id: number
  enterpriseId: number
  studentId: number
  studentName: string
  studentAvatar?: string
  jobId: number
  jobName: string
  lastMessageContent?: string
  lastMessageTime?: string
  unreadCount: number
  status: number
  statusName: string
  isOnline: boolean
  createTime: string
}

/**
 * 会话列表查询参数
 */
export interface SessionListQuery {
  pageNum: number
  pageSize: number
  searchKeyword?: string
}

/**
 * 会话详情查询响应
 */
export interface SessionDetail extends SessionInfo {
  // 扩展字段，如果需要更多详情
}

/**
 * 查询会话列表
 */
export function fetchSessionList(
  query: SessionListQuery
): Promise<ApiResponse<TableData<SessionInfo>>> {
  return request({
    url: '/enterprise/chat/session/list',
    method: 'get',
    params: {
      pageNum: query.pageNum,
      pageSize: query.pageSize,
      keyword: query.searchKeyword
    }
  })
}

/**
 * 查询会话详情
 */
export function fetchSessionDetail(
  sessionId: number
): Promise<ApiResponse<SessionDetail>> {
  return request({
    url: `/enterprise/chat/session/detail/${sessionId}`,
    method: 'get'
  })
}

// ==================== 消息相关接口 ====================

/**
 * 消息类型
 */
export type MessageType = 'text' | 'image' | 'file' | 'job'

/**
 * 发送消息请求参数
 */
export interface SendMessageParams {
  sessionId: number
  messageType: MessageType
  content: string
  relatedJobId?: number | null
}

/**
 * 消息信息
 */
export interface MessageInfo {
  messageId: number
  sessionId: number
  senderId: number
  senderName?: string
  senderAvatar?: string
  receiverId: number
  receiverName?: string
  receiverAvatar?: string
  messageType: MessageType
  content: string
  relatedJobId?: number | null
  isRead: boolean
  createTime: string
}

/**
 * 消息列表查询参数
 */
export interface MessageListQuery {
  sessionId: number
  pageNum: number
  pageSize: number
}

/**
 * 发送消息
 */
export function sendMessage(
  params: SendMessageParams
): Promise<ApiResponse<MessageInfo>> {
  return request({
    url: '/enterprise/chat/message/send',
    method: 'post',
    data: params
  })
}

/**
 * 查询消息列表
 */
export function fetchMessageList(
  query: MessageListQuery
): Promise<ApiResponse<TableData<MessageInfo>>> {
  return request({
    url: '/enterprise/chat/message/list',
    method: 'get',
    params: query
  })
}

/**
 * 标记消息为已读
 */
export function markMessagesAsRead(
  sessionId: number
): Promise<ApiResponse<void>> {
  return request({
    url: `/enterprise/chat/message/read/${sessionId}`,
    method: 'put'
  })
}

// ==================== 职位相关接口 ====================

/**
 * 发送职位信息请求参数
 */
export interface SendJobParams {
  sessionId: number
  jobId: number
}

/**
 * 发送职位信息
 */
export function sendJob(
  params: SendJobParams
): Promise<ApiResponse<MessageInfo>> {
  return request({
    url: '/enterprise/chat/message/send-job',
    method: 'post',
    data: params
  })
}

// ==================== 未读消息相关接口 ====================

/**
 * 未读消息总数响应
 */
export interface UnreadCountResponse {
  totalUnreadCount: number
}

/**
 * 查询未读消息总数
 */
export function fetchUnreadCount(): Promise<ApiResponse<UnreadCountResponse>> {
  return request({
    url: '/enterprise/chat/message/unread-count',
    method: 'get'
  })
}

// ==================== 反馈相关接口 ====================

/**
 * 提交反馈请求参数
 */
export interface SubmitFeedbackParams {
  feedbackType: string
  title: string
  content: string
  contactInfo?: string
}

/**
 * 提交反馈
 */
export function submitFeedback(
  params: SubmitFeedbackParams
): Promise<ApiResponse<number>> {
  return request({
    url: '/enterprise/feedback/add',
    method: 'post',
    data: params
  })
}



