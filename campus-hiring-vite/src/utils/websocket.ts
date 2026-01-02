import { Client } from '@stomp/stompjs'
import type { IFrame, IMessage, StompSubscription } from '@stomp/stompjs'
import { ElMessage } from 'element-plus'
import { getToken } from './cookie'

/**
 * WebSocket 消息类型
 */
export interface WebSocketMessage<T = any> {
  type: string
  data: T
  timestamp?: number
}

/**
 * WebSocket 连接状态
 */
export enum WebSocketStatus {
  CONNECTING = 'CONNECTING',
  CONNECTED = 'CONNECTED',
  DISCONNECTED = 'DISCONNECTED',
  ERROR = 'ERROR'
}

/**
 * WebSocket 配置选项
 */
export interface WebSocketOptions {
  url?: string
  destinations?: string | string[]
  reconnectInterval?: number
  heartbeatInterval?: number
  onOpen?: (event: Event) => void
  onMessage?: (message: WebSocketMessage) => void
  onClose?: (event: CloseEvent) => void
  onError?: (event: Event) => void
}

/**
 * WebSocket（STOMP）客户端封装
 */
export class WebSocketClient {
  private client: Client | null = null
  private readonly url: string
  private readonly reconnectInterval: number
  private readonly heartbeatInterval: number
  private destinations: string[] = []
  private subscriptions: StompSubscription[] = []
  private status: WebSocketStatus = WebSocketStatus.DISCONNECTED
  private onOpenCallback?: (event: Event) => void
  private onMessageCallback?: (message: WebSocketMessage) => void
  private onCloseCallback?: (event: CloseEvent) => void
  private onErrorCallback?: (event: Event) => void

  constructor(options: WebSocketOptions = {}) {
    const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
    const wsHost =
      import.meta.env.VITE_WS_HOST ||
      (import.meta.env.DEV ? 'localhost:8080' : window.location.host)
    const wsPath = import.meta.env.VITE_WS_PATH || '/ws'

    this.url = options.url || `${wsProtocol}//${wsHost}${wsPath}`
    this.destinations = this.normalizeDestinations(options.destinations)
    this.reconnectInterval = options.reconnectInterval ?? 5000
    this.heartbeatInterval = options.heartbeatInterval ?? 20000
    this.onOpenCallback = options.onOpen
    this.onMessageCallback = options.onMessage
    this.onCloseCallback = options.onClose
    this.onErrorCallback = options.onError
  }

  /**
   * 更新订阅目的地
   */
  setDestinations(destinations: string | string[]): void {
    this.destinations = this.normalizeDestinations(destinations)
    if (this.client?.connected) {
      this.subscribeDestinations()
    }
  }

  /**
   * 发起连接
   */
  connect(): void {
    if (this.client?.connected) {
      return
    }

    const token = getToken()
    const urlWithToken = token ? `${this.url}?token=${encodeURIComponent(token)}` : this.url

    this.status = WebSocketStatus.CONNECTING
    this.client = new Client({
      brokerURL: urlWithToken,
      reconnectDelay: this.reconnectInterval,
      heartbeatIncoming: this.heartbeatInterval,
      heartbeatOutgoing: this.heartbeatInterval,
      debug: () => undefined
    })

    this.client.onConnect = () => {
      this.status = WebSocketStatus.CONNECTED
      this.subscribeDestinations()
      this.onOpenCallback?.(new Event('open'))
    }

    this.client.onStompError = (frame: IFrame) => {
      console.error('STOMP error', frame.headers['message'], frame.body)
      this.status = WebSocketStatus.ERROR
      this.onErrorCallback?.(new Event('error'))
    }

    this.client.onWebSocketError = (event: Event) => {
      console.error('WebSocket transport error', event)
      this.status = WebSocketStatus.ERROR
      this.onErrorCallback?.(event)
      ElMessage.error('WebSocket 连接异常')
    }

    this.client.onWebSocketClose = (event: CloseEvent) => {
      this.status = WebSocketStatus.DISCONNECTED
      this.clearSubscriptions()
      this.onCloseCallback?.(event)
    }

    this.client.onUnhandledMessage = (message: IMessage) => {
      this.handleMessage(message)
    }

    this.client.activate()
  }

  /**
   * 断开连接
   */
  disconnect(): void {
    this.clearSubscriptions()
    if (this.client) {
      this.client.deactivate()
      this.client = null
    }
    this.status = WebSocketStatus.DISCONNECTED
  }

  /**
   * 获取连接状态
   */
  getStatus(): WebSocketStatus {
    return this.status
  }

  /**
   * 是否已连接
   */
  isConnected(): boolean {
    return Boolean(this.client?.connected)
  }

  private handleMessage(message: IMessage) {
    if (!message.body) {
      return
    }
    try {
      const payload = JSON.parse(message.body) as WebSocketMessage
      this.onMessageCallback?.(payload)
    } catch (error) {
      console.error('Failed to parse STOMP message', error, message.body)
    }
  }

  private subscribeDestinations(): void {
    if (!this.client?.connected) {
      return
    }
    this.clearSubscriptions()
    this.destinations.forEach(destination => {
      const subscription = this.client!.subscribe(destination, msg => this.handleMessage(msg))
      this.subscriptions.push(subscription)
    })
  }

  private clearSubscriptions(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe())
    this.subscriptions = []
  }

  private normalizeDestinations(destinations?: string | string[]): string[] {
    if (!destinations) {
      return []
    }
    return Array.isArray(destinations) ? destinations : [destinations]
  }
}

/**
 * 创建 WebSocket 客户端实例
 */
export function createWebSocketClient(options: WebSocketOptions = {}): WebSocketClient {
  return new WebSocketClient(options)
}

