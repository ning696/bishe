<template>
  <div class="message-content-wrapper">
    <!-- 文本消息 -->
    <div v-if="message.messageType === 'text'" class="text-message">
      {{ message.content }}
    </div>

    <!-- 图片消息 -->
    <div v-else-if="message.messageType === 'image'" class="image-message">
      <el-image
        :src="message.content"
        :preview-src-list="[message.content]"
        fit="cover"
        style="max-width: 200px; max-height: 200px; border-radius: 4px; cursor: pointer;"
      />
    </div>

    <!-- 文件消息 -->
    <div v-else-if="message.messageType === 'file'" class="file-message">
      <el-card shadow="hover" style="cursor: pointer;" @click="handleDownload">
        <div class="file-info">
          <el-icon :size="24"><Document /></el-icon>
          <div class="file-details">
            <div class="file-name">{{ fileInfo.fileName }}</div>
            <div class="file-size">{{ fileSize }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 简历消息 -->
    <div v-else-if="message.messageType === 'resume'" class="resume-message">
      <el-card shadow="hover" style="cursor: pointer;" @click="handleViewResume">
        <div class="resume-info">
          <el-icon :size="24"><Document /></el-icon>
          <div class="resume-details">
            <div class="resume-name">{{ resumeInfo.resumeName || '简历' }}</div>
            <div class="resume-hint">点击查看完整简历</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 未知消息类型 -->
    <div v-else class="unknown-message">
      未知消息类型
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Document } from '@element-plus/icons-vue'
import type { MessageInfo } from '@/api/student/consultation'

interface Props {
  message: MessageInfo
  isSent: boolean
}

const props = defineProps<Props>()

// 解析文件信息
const fileInfo = computed(() => {
  try {
    return JSON.parse(props.message.content)
  } catch {
    return {
      url: props.message.content,
      fileName: '文件'
    }
  }
})

// 解析简历信息
const resumeInfo = computed(() => {
  try {
    return JSON.parse(props.message.content)
  } catch {
    return {
      resumeId: props.message.content,
      resumeName: '简历'
    }
  }
})

// 文件大小格式化
const fileSize = computed(() => {
  // 这里可以根据实际需求格式化文件大小
  return '文件'
})

// 下载文件
const handleDownload = () => {
  window.open(fileInfo.value.url, '_blank')
}

// 查看简历
const handleViewResume = () => {
  // TODO: 实现查看简历功能
  console.log('查看简历:', resumeInfo.value)
}
</script>

<style scoped lang="scss">
.message-content-wrapper {
  width: 100%;
}

.text-message {
  white-space: pre-wrap;
  word-break: break-word;
}

.image-message {
  img {
    max-width: 100%;
    height: auto;
  }
}

.file-message,
.resume-message {
  .file-info,
  .resume-info {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .file-details,
  .resume-details {
    flex: 1;
  }

  .file-name,
  .resume-name {
    font-weight: 600;
    margin-bottom: 4px;
  }

  .file-size,
  .resume-hint {
    font-size: 12px;
    color: #999;
  }
}
</style>



