<template>
  <div class="consultation-container">
    <!-- 左侧会话列表 -->
    <div class="session-list-panel">
      <!-- 搜索框 -->
      <div class="search-box">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索学生或职位"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <!-- 会话列表 -->
      <div class="session-list" v-loading="sessionLoading">
        <div
          v-for="session in filteredSessions"
          :key="session.id"
          class="session-item"
          :class="{ active: currentSessionId === session.id }"
          @click="selectSession(session)"
        >
          <div class="session-avatar">
            <el-avatar :src="session.studentAvatar" :size="50">
              <el-icon><User /></el-icon>
            </el-avatar>
            <span
              v-if="session.isOnline"
              class="online-indicator"
            ></span>
          </div>
          <div class="session-content">
            <div class="session-header">
              <span class="session-name">{{ session.studentName }}</span>
              <span class="session-time">{{ formatTime(session.lastMessageTime) }}</span>
            </div>
            <div class="session-meta">
              <span class="session-job">{{ session.jobName }}</span>
            </div>
            <div class="session-preview">
              {{ session.lastMessageContent || '暂无消息' }}
            </div>
          </div>
          <el-badge
            v-if="session.unreadCount > 0"
            :value="session.unreadCount"
            :max="99"
            class="session-badge"
          />
        </div>
        <el-empty
          v-if="!sessionLoading && filteredSessions.length === 0"
          description="暂无会话"
        />
      </div>
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-panel" v-if="currentSession">
      <!-- 聊天头部 -->
      <div class="chat-header">
        <div class="chat-header-info">
          <el-avatar :src="currentSession.studentAvatar" :size="40">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="chat-header-text">
            <div class="chat-header-name">
              {{ currentSession.studentName }}
<!--              <span-->
<!--                v-if="currentSession.isOnline"-->
<!--                class="online-status"-->
<!--              >在线</span>-->
<!--              <span v-else class="offline-status">离线</span>-->
            </div>
            <div class="chat-header-job">{{ currentSession.jobName }}</div>
          </div>
        </div>
        <div class="chat-header-actions">
          <el-button
            type="primary"
            link
            @click="showComplaintDialog = true"
          >
            投诉
          </el-button>
          <el-button
            type="primary"
            link
            @click="showFeedbackDialog = true"
          >
            反馈
          </el-button>
        </div>
      </div>

      <!-- 消息显示区域 -->
      <div
        class="message-list"
        ref="messageListRef"
        v-loading="messageLoading"
        @scroll="handleScroll"
      >
        <!-- 顶部加载更多指示器 -->
        <div v-if="loadingMore" class="loading-more-indicator">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        
        <div
          v-for="message in messages"
          :key="message.messageId"
          class="message-item"
          :class="{ 'message-sent': isSentMessage(message), 'message-received': !isSentMessage(message) }"
        >
          <el-avatar
            :src="isSentMessage(message) ? userAvatar : currentSession.studentAvatar"
            :size="36"
          >
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="message-content">
            <div class="message-bubble">
              <MessageContent
                :message="message"
                :is-sent="isSentMessage(message)"
              />
            </div>
            <div class="message-time">
              {{ formatMessageTime(message.createTime) }}
            </div>
          </div>
        </div>
        <el-empty
          v-if="!messageLoading && messages.length === 0"
          description="暂无消息，开始聊天吧"
        />
      </div>

      <!-- 输入区域 -->
      <div class="input-panel">
        <div class="input-toolbar">
          <el-button
            type="primary"
            link
            @click="showJobDialog = true"
          >
            <el-icon><Briefcase /></el-icon>
            发送职位
          </el-button>
          <el-upload
            :action="''"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleImageSelect"
            accept="image/*"
          >
            <el-button type="primary" link>
              <el-icon><Picture /></el-icon>
              图片
            </el-button>
          </el-upload>
          <el-upload
            :action="''"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleFileSelect"
          >
            <el-button type="primary" link>
              <el-icon><Paperclip /></el-icon>
              文件
            </el-button>
          </el-upload>
        </div>
        <div class="input-area">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            @keydown.ctrl.enter="handleSendMessage"
            @keydown.enter.exact.prevent="handleSendMessage"
          />
          <el-button
            type="primary"
            :loading="sending"
            @click="handleSendMessage"
            :disabled="!inputMessage.trim()"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-chat">
      <el-empty description="请选择一个会话开始聊天" />
    </div>

    <!-- 发送职位对话框 -->
    <el-dialog
      v-model="showJobDialog"
      title="选择职位"
      width="500px"
    >
      <el-radio-group v-model="selectedJobId">
        <el-radio
          v-for="job in jobList"
          :key="job.id"
          :label="job.id"
        >
          {{ job.jobName }}
          <el-tag v-if="job.status === 1" size="small" type="success" style="margin-left: 8px">
            已发布
          </el-tag>
        </el-radio>
      </el-radio-group>
      <template #footer>
        <el-button @click="showJobDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleSendJob"
          :disabled="!selectedJobId"
        >
          发送
        </el-button>
      </template>
    </el-dialog>

    <!-- 投诉对话框 -->
    <el-dialog
      v-model="showComplaintDialog"
      title="投诉"
      width="600px"
    >
      <el-form :model="complaintForm" label-width="100px">
        <el-form-item label="投诉标题" required>
          <el-input v-model="complaintForm.title" placeholder="请输入投诉标题" />
        </el-form-item>
        <el-form-item label="投诉内容" required>
          <el-input
            v-model="complaintForm.content"
            type="textarea"
            :rows="5"
            placeholder="请输入投诉内容"
          />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload
            :action="''"
            :auto-upload="false"
            :show-file-list="true"
            :on-change="handleComplaintFileSelect"
            :limit="3"
          >
            <el-button type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showComplaintDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleSubmitComplaint"
          :loading="submittingComplaint"
        >
          提交
        </el-button>
      </template>
    </el-dialog>

    <!-- 反馈对话框 -->
    <el-dialog
      v-model="showFeedbackDialog"
      title="意见反馈"
      width="600px"
    >
      <el-form :model="feedbackForm" label-width="100px">
        <el-form-item label="反馈类型" required>
          <el-select v-model="feedbackForm.feedbackType" placeholder="请选择反馈类型">
            <el-option label="功能建议" value="功能建议" />
            <el-option label="问题反馈" value="问题反馈" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="反馈标题" required>
          <el-input v-model="feedbackForm.title" placeholder="请输入反馈标题" />
        </el-form-item>
        <el-form-item label="反馈内容" required>
          <el-input
            v-model="feedbackForm.content"
            type="textarea"
            :rows="5"
            placeholder="请输入反馈内容"
          />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="feedbackForm.contactInfo" placeholder="请输入联系方式（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFeedbackDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleSubmitFeedback"
          :loading="submittingFeedback"
        >
          提交
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search,
  User,
  Briefcase,
  Document,
  Picture,
  Paperclip,
  Loading
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/modules/user'
import {
  fetchSessionList,
  fetchMessageList,
  sendMessage,
  sendJob,
  markMessagesAsRead,
  type SessionInfo,
  type MessageInfo
} from '@/api/enterprise/consultation'
import { fetchEnterpriseJobList, type EnterpriseJobListItem } from '@/api/enterprise/job'
import { uploadEnterpriseFile } from '@/api/enterprise/enterprise'
import { submitComplaint } from '@/api/enterprise/complaint'
import { submitFeedback } from '@/api/enterprise/consultation'
import { createWebSocketClient, type WebSocketMessage } from '@/utils/websocket'
import MessageContent from './components/MessageContent.vue'

const route = useRoute()
const userStore = useUserStore()

// 会话列表
const sessions = ref<SessionInfo[]>([])
const sessionLoading = ref(false)
const searchKeyword = ref('')
const currentSessionId = ref<number | null>(null)
const currentSession = computed(() => {
  return sessions.value.find(s => s.id === currentSessionId.value) || null
})

// 消息列表
const messages = ref<MessageInfo[]>([])
const messageLoading = ref(false)
const loadingMore = ref(false) // 加载更多状态
const messageListRef = ref<HTMLElement | null>(null)
const hasMoreMessages = ref(true)
const currentPage = ref(1)
const pageSize = 20

// 输入
const inputMessage = ref('')
const sending = ref(false)

// 职位列表
const jobList = ref<EnterpriseJobListItem[]>([])
const showJobDialog = ref(false)
const selectedJobId = ref<number | null>(null)

// 投诉与反馈
const showComplaintDialog = ref(false)
const showFeedbackDialog = ref(false)
const submittingComplaint = ref(false)
const submittingFeedback = ref(false)
const complaintForm = ref({
  title: '',
  content: '',
  attachment: null as File | null
})
const feedbackForm = ref({
  feedbackType: '功能建议',
  title: '',
  content: '',
  contactInfo: ''
})

// WebSocket
let wsClient: ReturnType<typeof createWebSocketClient> | null = null

// 用户头像
const userAvatar = computed(() => userStore.userInfo?.avatar || '')

// 过滤后的会话列表
const filteredSessions = computed(() => {
  if (!searchKeyword.value) {
    return sessions.value
  }
  const keyword = searchKeyword.value.toLowerCase()
  return sessions.value.filter(session =>
    session.studentName.toLowerCase().includes(keyword) ||
    session.jobName.toLowerCase().includes(keyword)
  )
})

// 加载会话列表
const loadSessions = async () => {
  sessionLoading.value = true
  try {
    const response = await fetchSessionList({
      pageNum: 1,
      pageSize: 100,
      searchKeyword: searchKeyword.value || undefined
    })
    if (response.code === 1000 && response.data) {
      sessions.value = response.data.rows || []
      
      // 如果从路由参数中传入了会话ID，自动选中
      const sessionIdFromRoute = route.query.sessionId
      if (sessionIdFromRoute && !currentSessionId.value) {
        const sessionId = Number(sessionIdFromRoute)
        if (sessions.value.find(s => s.id === sessionId)) {
          selectSessionById(sessionId)
        }
      } else if (sessions.value.length > 0 && !currentSessionId.value) {
        // 默认选中第一个会话
        selectSession(sessions.value[0])
      }
    }
  } catch (error) {
    console.error('加载会话列表失败:', error)
  } finally {
    sessionLoading.value = false
  }
}

// 选择会话
const selectSession = (session: SessionInfo) => {
  currentSessionId.value = session.id
  messages.value = []
  currentPage.value = 1
  hasMoreMessages.value = true
  loadMessages()
  markAsRead()
}

// 根据ID选择会话
const selectSessionById = (sessionId: number) => {
  const session = sessions.value.find(s => s.id === sessionId)
  if (session) {
    selectSession(session)
  }
}

// 加载消息列表
const loadMessages = async (loadMore = false) => {
  if (!currentSessionId.value) return
  
  if (!loadMore) {
    messageLoading.value = true
    currentPage.value = 1
    hasMoreMessages.value = true
  } else {
    loadingMore.value = true // 设置加载更多状态
  }
  
  try {
    const response = await fetchMessageList({
      sessionId: currentSessionId.value,
      pageNum: currentPage.value,
      pageSize: pageSize
    })
    
    if (response.code === 1000 && response.data) {
      const newMessages = response.data.rows || []
      // 前端日志：加载消息列表
      console.debug?.('[CONSULT][loadMessages]', {
        loadMore,
        currentSessionId: currentSessionId.value,
        page: currentPage.value,
        pageSize,
        rows: newMessages.map(m => ({
          messageId: m.messageId,
          senderId: m.senderId,
          sessionId: m.sessionId,
          createTime: m.createTime
        })),
        currentUserId: userStore.userInfo?.id
      })
      
      if (loadMore) {
        // 加载更多时，将新消息插入到前面
        // 由于后端返回 DESC，需要反转后再插入
        messages.value = [...newMessages.reverse(), ...messages.value]
      } else {
        // 首次加载或刷新时，替换消息列表
        // 由于后端返回 DESC，需要反转以显示为从旧到新
        messages.value = newMessages.reverse()
        // 滚动到底部
        nextTick(() => {
          scrollToBottom()
        })
      }
      
      // 判断是否还有更多消息
      hasMoreMessages.value = newMessages.length === pageSize
      
      if (loadMore) {
        currentPage.value++
      }
    }
  } catch (error) {
    console.error('加载消息列表失败:', error)
  } finally {
    messageLoading.value = false
    loadingMore.value = false // 重置加载更多状态
  }
}

// 发送消息
const handleSendMessage = async () => {
  if (!inputMessage.value.trim() || !currentSessionId.value || sending.value) {
    return
  }
  
  const content = inputMessage.value.trim()
  inputMessage.value = ''
  sending.value = true
  
  try {
    const response = await sendMessage({
      sessionId: currentSessionId.value,
      messageType: 'text',
      content: content
    })
    
    if (response.code === 1000 && response.data) {
      // 前端日志：发送消息成功
      console.debug?.('[CONSULT][sendMessage][success]', {
        sessionId: currentSessionId.value,
        messageId: response.data.messageId,
        senderId: response.data.senderId,
        currentUserId: userStore.userInfo?.id
      })
      // 添加消息到列表
      messages.value.push(response.data)
      // 滚动到底部
      nextTick(() => {
        scrollToBottom()
      })
      // 刷新会话列表以更新最后消息
      loadSessions()
    }
  } catch (error) {
    ElMessage.error('发送消息失败')
    inputMessage.value = content
  } finally {
    sending.value = false
  }
}

// 发送图片
const handleImageSelect = async (file: any) => {
  if (!currentSessionId.value) {
    ElMessage.warning('请先选择一个会话')
    return
  }
  
  try {
    // 上传图片
    const uploadRes = await uploadEnterpriseFile(file.raw)
    if (!uploadRes.data?.name) {
      throw new Error('上传失败')
    }
    
    // 发送图片消息
    const response = await sendMessage({
      sessionId: currentSessionId.value,
      messageType: 'image',
      content: uploadRes.data.name
    })
    
    if (response.code === 1000 && response.data) {
      messages.value.push(response.data)
      nextTick(() => {
        scrollToBottom()
      })
      loadSessions()
    }
  } catch (error) {
    ElMessage.error('发送图片失败')
  }
}

// 发送文件
const handleFileSelect = async (file: any) => {
  if (!currentSessionId.value) {
    ElMessage.warning('请先选择一个会话')
    return
  }
  
  try {
    // 上传文件
    const uploadRes = await uploadEnterpriseFile(file.raw)
    if (!uploadRes.data?.name) {
      throw new Error('上传失败')
    }
    
    // 发送文件消息
    const fileInfo = {
      url: uploadRes.data.name,
      fileName: file.name
    }
    
    const response = await sendMessage({
      sessionId: currentSessionId.value,
      messageType: 'file',
      content: JSON.stringify(fileInfo)
    })
    
    if (response.code === 1000 && response.data) {
      messages.value.push(response.data)
      nextTick(() => {
        scrollToBottom()
      })
      loadSessions()
    }
  } catch (error) {
    ElMessage.error('发送文件失败')
  }
}

// 发送职位
const handleSendJob = async () => {
  if (!currentSessionId.value || !selectedJobId.value) {
    return
  }
  
  try {
    const response = await sendJob({
      sessionId: currentSessionId.value,
      jobId: selectedJobId.value
    })
    
    if (response.code === 1000 && response.data) {
      messages.value.push(response.data)
      nextTick(() => {
        scrollToBottom()
      })
      showJobDialog.value = false
      selectedJobId.value = null
      loadSessions()
      ElMessage.success('职位发送成功')
    }
  } catch (error) {
    ElMessage.error('发送职位失败')
  }
}

// 加载职位列表
const loadJobList = async () => {
  try {
    const response = await fetchEnterpriseJobList({
      pageNum: 1,
      pageSize: 100,
      status: 1 // 只加载已发布的职位
    })
    if (response.code === 1000 && response.data) {
      jobList.value = response.data.rows || []
    }
  } catch (error) {
    console.error('加载职位列表失败:', error)
  }
}

// 标记为已读
const markAsRead = async () => {
  if (!currentSessionId.value) return
  
  try {
    await markMessagesAsRead(currentSessionId.value)
    // 更新会话列表中的未读数
    const session = sessions.value.find(s => s.id === currentSessionId.value)
    if (session) {
      session.unreadCount = 0
    }
  } catch (error) {
    console.error('标记已读失败:', error)
  }
}

// 提交反馈
const handleSubmitFeedback = async () => {
  if (!feedbackForm.value.title || !feedbackForm.value.content) {
    ElMessage.warning('请填写反馈标题和内容')
    return
  }
  
  submittingFeedback.value = true
  try {
    await submitFeedback({
      feedbackType: feedbackForm.value.feedbackType,
      title: feedbackForm.value.title,
      content: feedbackForm.value.content,
      contactInfo: feedbackForm.value.contactInfo || undefined
    })
    ElMessage.success('反馈已提交，感谢您的建议')
    showFeedbackDialog.value = false
    feedbackForm.value = {
      feedbackType: '功能建议',
      title: '',
      content: '',
      contactInfo: ''
    }
  } catch (error) {
    ElMessage.error('提交反馈失败')
  } finally {
    submittingFeedback.value = false
  }
}

// 提交投诉
const handleSubmitComplaint = async () => {
  if (!complaintForm.value.title || !complaintForm.value.content) {
    ElMessage.warning('请填写投诉标题和内容')
    return
  }
  if (!currentSession.value) {
    ElMessage.warning('请选择要投诉的会话')
    return
  }

  submittingComplaint.value = true
  try {
    let attachmentUrl: string | undefined
    if (complaintForm.value.attachment) {
      const uploadRes = await uploadEnterpriseFile(complaintForm.value.attachment)
      if (uploadRes.code === 1000 && uploadRes.data?.success && uploadRes.data.name) {
        attachmentUrl = uploadRes.data.name
      } else {
        throw new Error('附件上传失败，请重试')
      }
    }

    const response = await submitComplaint({
      studentId: currentSession.value.studentId,
      jobId: currentSession.value.jobId,
      title: complaintForm.value.title,
      content: complaintForm.value.content,
      attachment: attachmentUrl
    })

    if (response.code !== 1000) {
      throw new Error(response.msg || '提交投诉失败')
    }
    ElMessage.success('投诉已提交，投诉已提交，管理员将尽快处理')
    showComplaintDialog.value = false
    complaintForm.value = { title: '', content: '', attachment: null }
  } catch (error) {
    console.error('提交投诉失败:', error)
    const errMsg = error instanceof Error ? error.message : '提交投诉失败'
    ElMessage.error(errMsg)
  } finally {
    submittingComplaint.value = false
  }
}

// 投诉文件选择
const handleComplaintFileSelect = (file: any) => {
  complaintForm.value.attachment = file.raw
}

// 搜索
const handleSearch = () => {
  loadSessions()
}

// 滚动处理
const handleScroll = (e: Event) => {
  const target = e.target as HTMLElement
  // 向上滚动到底部时加载更多
  if (target.scrollTop === 0 && hasMoreMessages.value && !messageLoading.value) {
    loadMessages(true)
  }
  
  // 滚动到底部时标记为已读
  const isAtBottom = target.scrollHeight - target.scrollTop - target.clientHeight < 50
  if (isAtBottom) {
    markAsRead()
  }
}

// 滚动到底部
const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight
  }
}

// 判断是否是自己发送的消息
const isSentMessage = (message: MessageInfo) => {
  const result = message.senderId === userStore.userInfo?.id
  return result
}

// 格式化时间
const formatTime = (time?: string) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (minutes < 1440) return `${Math.floor(minutes / 60)}小时前`
  if (minutes < 10080) return `${Math.floor(minutes / 1440)}天前`
  
  return date.toLocaleDateString()
}

// 格式化消息时间
const formatMessageTime = (time: string) => {
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  
  if (minutes < 5) return '刚刚'
  
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const messageDate = new Date(date.getFullYear(), date.getMonth(), date.getDate())
  
  if (messageDate.getTime() === today.getTime()) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
  
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 初始化 WebSocket
const initWebSocket = () => {
  const userId = userStore.userInfo?.id
  if (!userId) {
    console.warn('[CONSULT][WS] Missing user id, skip websocket init')
    return
  }

  const wsUrl = import.meta.env.DEV
    ? 'ws://localhost:8083/ws'
    : `${window.location.protocol === 'https:' ? 'wss:' : 'ws:'}//${window.location.hostname}:8083/ws`

  wsClient = createWebSocketClient({
    url: wsUrl,
    destinations: `/queue/chat/2/${userId}`,
    onMessage: (message: WebSocketMessage) => {
      // 前端日志：收到 WebSocket 消息
      console.debug?.('[CONSULT][WS][onMessage]', message)
      if (message.type === 'message') {
        const msg = message.data as MessageInfo
        // 如果是当前会话的消息，添加到消息列表
        if (msg.sessionId === currentSessionId.value) {
          messages.value.push(msg)
          nextTick(() => {
            scrollToBottom()
          })
          markAsRead()
        }
        // 刷新会话列表
        loadSessions()
      }
    },
    onError: () => {
      console.error('WebSocket 连接错误')
    }
  })
  
  wsClient.connect()
}

// 组件挂载
onMounted(async () => {
  // 前端日志：用户上下文
  console.debug?.('[CONSULT][mounted][user]', {
    role: userStore.role,
    userInfo: userStore.userInfo
  })
  await loadJobList()
  await loadSessions()
  initWebSocket()
})

// 组件卸载
onUnmounted(() => {
  if (wsClient) {
    wsClient.disconnect()
  }
})
</script>

<style scoped lang="scss">
.consultation-container {
  display: flex;
  height: calc(100vh - 60px);
  background-color: #f5f5f5;
}

.session-list-panel {
  width: 300px;
  background-color: #fff;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
}

.search-box {
  padding: 12px;
  border-bottom: 1px solid #e0e0e0;
}

.session-list {
  flex: 1;
  overflow-y: auto;
}

.session-item {
  display: flex;
  padding: 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  position: relative;
  transition: background-color 0.2s;

  &:hover {
    background-color: #f5f5f5;
  }

  &.active {
    background-color: #e6f7ff;
    border-left: 3px solid #409eff;
  }
}

.session-avatar {
  position: relative;
  margin-right: 12px;

  .online-indicator {
    position: absolute;
    bottom: 0;
    right: 0;
    width: 12px;
    height: 12px;
    background-color: #67c23a;
    border: 2px solid #fff;
    border-radius: 50%;
  }
}

.session-content {
  flex: 1;
  min-width: 0;
}

.session-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.session-name {
  font-weight: 600;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.session-time {
  font-size: 12px;
  color: #999;
  flex-shrink: 0;
  margin-left: 8px;
}

.session-meta {
  margin-bottom: 4px;
}

.session-job {
  font-size: 12px;
  color: #666;
}

.session-preview {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.session-badge {
  margin-left: 8px;
}

.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fff;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e0e0e0;
}

.chat-header-info {
  display: flex;
  align-items: center;
}

.chat-header-text {
  margin-left: 12px;
}

.chat-header-name {
  font-weight: 600;
  font-size: 16px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.online-status {
  font-size: 12px;
  color: #67c23a;
  font-weight: normal;
}

.offline-status {
  font-size: 12px;
  color: #999;
  font-weight: normal;
}

.chat-header-job {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.chat-header-actions {
  display: flex;
  gap: 8px;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background-color: #f5f5f5;
  position: relative;
}

.loading-more-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  color: #909399;
  font-size: 14px;
  
  .el-icon {
    font-size: 16px;
  }
}

.message-item {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-start;

  &.message-sent {
    flex-direction: row-reverse;

    .message-content {
      align-items: flex-end;
    }

    .message-bubble {
      background-color: #409eff;
      color: #fff;
    }
  }

  &.message-received {
    .message-bubble {
      background-color: #fff;
      color: #333;
    }
  }
}

.message-content {
  display: flex;
  flex-direction: column;
  max-width: 60%;
  margin: 0 12px;
}

.message-bubble {
  padding: 10px 14px;
  border-radius: 8px;
  word-wrap: break-word;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
  padding: 0 4px;
}

.input-panel {
  border-top: 1px solid #e0e0e0;
  padding: 12px;
  background-color: #fff;
}

.input-toolbar {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.input-area {
  display: flex;
  gap: 8px;
  align-items: flex-end;

  .el-textarea {
    flex: 1;
  }
}

.empty-chat {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
}

@media (max-width: 768px) {
  .session-list-panel {
    width: 250px;
  }
}
</style>

