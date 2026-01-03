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

    <!-- 职位消息 -->
    <div v-else-if="message.messageType === 'job'" class="job-message">
      <el-card shadow="hover" style="cursor: pointer;" @click="handleViewJob">
        <div class="job-info">
          <el-icon :size="24"><OfficeBuilding /></el-icon>
          <div class="job-details">
            <div class="job-name">{{ jobInfo.jobName || '职位信息' }}</div>
            <div class="job-hint">点击查看职位详情</div>
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
import { useRouter } from 'vue-router'
import { Document, OfficeBuilding } from '@element-plus/icons-vue'
import type { MessageInfo } from '@/api/student/consultation'

interface Props {
  message: MessageInfo
  isSent: boolean
}

const props = defineProps<Props>()
const router = useRouter()

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

// 解析职位信息
const jobInfo = computed(() => {
  try {
    return JSON.parse(props.message.content)
  } catch {
    return {
      jobId: props.message.relatedJobId || props.message.content,
      jobName: '职位信息'
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
  const resumeId = resumeInfo.value.resumeId
  if (!resumeId) {
    console.error('简历ID不存在:', resumeInfo.value)
    return
  }
  router.push({
    path: `/student/resume/edit/${resumeId}`
  })
}

// 查看职位
const handleViewJob = () => {
  const jobId = jobInfo.value.jobId || props.message.relatedJobId
  if (!jobId) {
    console.error('职位ID不存在:', jobInfo.value)
    return
  }
  router.push({
    path: `/student/job/${jobId}`
  })
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
.resume-message,
.job-message {
  .file-info,
  .resume-info,
  .job-info {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .file-details,
  .resume-details,
  .job-details {
    flex: 1;
  }

  .file-name,
  .resume-name,
  .job-name {
    font-weight: 600;
    margin-bottom: 4px;
  }

  .file-size,
  .resume-hint,
  .job-hint {
    font-size: 12px;
    color: #999;
  }
}
</style>



