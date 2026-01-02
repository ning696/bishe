<template>
  <div class="profile-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>

      <div class="profile-content" v-if="studentDetail">
        <div class="avatar-section">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :before-upload="beforeAvatarUpload"
            :http-request="handleAvatarUpload"
            :disabled="uploading"
          >
            <el-avatar
              :size="100"
              :src="studentDetail.headImage"
              :icon="UserFilled"
              :class="{ 'avatar-loading': uploading }"
            />
            <div class="avatar-mask" v-if="uploading">
              <el-icon><Loading /></el-icon>
            </div>
          </el-upload>
          <div class="avatar-info">
            <h3>{{ studentDetail.nickName || '未设置昵称' }}</h3>
            <p class="username">用户名：{{ studentDetail.username || '-' }}</p>
          </div>
        </div>

        <div class="action-section">
          <el-button @click="goToEdit">编辑信息</el-button>
          <el-button @click="goToChangePassword">修改密码</el-button>
        </div>

        <div class="detail-section">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="真实姓名">{{ studentDetail.realName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ studentDetail.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ studentDetail.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="性别">
              {{ studentDetail.gender != null ? genderMap[studentDetail.gender] ?? '-' : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="生日">{{ studentDetail.birthday || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属校园">{{ studentDetail.campusName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ studentDetail.major || '-' }}</el-descriptions-item>
            <el-descriptions-item label="学历">{{ studentDetail.education || '-' }}</el-descriptions-item>
            <el-descriptions-item label="年级">{{ studentDetail.grade || '-' }}</el-descriptions-item>
            <el-descriptions-item label="技能">{{ studentDetail.skills || '-' }}</el-descriptions-item>
            <el-descriptions-item label="工作经验">{{ studentDetail.experience ? studentDetail.experience + '个月' : '-' }}</el-descriptions-item>
            <el-descriptions-item label="期望薪资">
              {{ studentDetail.expectedSalary ? '¥' + studentDetail.expectedSalary.toLocaleString() : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="期望地点">{{ studentDetail.expectedLocation || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              {{ studentDetail.status != null ? statusMap[studentDetail.status] ?? '-' : '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ studentDetail.createTime || '-' }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>

      <el-empty v-else description="暂无个人信息" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading, UserFilled } from '@element-plus/icons-vue'
import { getDetail, updateHeadImage, uploadStudentFile, type StudentDetail } from '@/api/student/student'
import type { UploadRequestOptions } from 'element-plus'

const router = useRouter()

const genderMap: Record<number, string> = {
  0: '女',
  1: '男'
}

const statusMap: Record<number, string> = {
  0: '已拉黑',
  1: '正常',
  2: '已禁用',
  3: '待审核'
}

const loading = ref(false)
const studentDetail = ref<StudentDetail | null>(null)
const uploading = ref(false)

const loadStudentDetail = async () => {
  loading.value = true
  try {
    const response = await getDetail()
    if (response.code === 1000 && response.data) {
      studentDetail.value = response.data
    } else {
      ElMessage.error(response.msg || '获取信息失败')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '获取信息失败，请重试')
  } finally {
    loading.value = false
  }
}

const goToEdit = () => {
  router.push('/student/profile/edit')
}

const goToChangePassword = () => {
  router.push('/student/profile/change-password')
}

const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB')
    return false
  }
  return true
}

const handleAvatarUpload = async (options: UploadRequestOptions) => {
  if (uploading.value) {
    return
  }
  uploading.value = true
  const file = options.file as File
  try {
    const uploadRes = await uploadStudentFile(file)
    if (uploadRes.code !== 1000 || !uploadRes.data?.name) {
      throw new Error(uploadRes.msg || '头像上传失败')
    }

    const updateRes = await updateHeadImage({ headImage: uploadRes.data.name })
    if (updateRes.code !== 1000) {
      throw new Error(updateRes.msg || '头像更新失败')
    }

    ElMessage.success('头像更新成功')
    await loadStudentDetail()
    options.onSuccess?.(updateRes, file)
  } catch (error: any) {
    ElMessage.error(error.message || '头像更新失败，请重试')
    options.onError?.(error)
  } finally {
    uploading.value = false
  }
}

onMounted(() => {
  loadStudentDetail()
})
</script>

<style scoped lang="scss">
.profile-container {
  padding: 20px;

  .card-header {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .profile-content {
    .avatar-section {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 20px;
      margin-bottom: 30px;
      padding-bottom: 30px;
      border-bottom: 1px solid #ebeef5;

      .avatar-uploader {
        position: relative;
        width: 100px;
        height: 100px;
        cursor: pointer;

        :deep(.el-upload) {
          width: 100%;
          height: 100%;
        }

        .avatar-loading {
          opacity: 0.6;
        }

        .avatar-mask {
          position: absolute;
          top: 0;
          right: 0;
          bottom: 0;
          left: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          background-color: rgba(0, 0, 0, 0.3);
          color: #fff;
          border-radius: 50%;
        }
      }

      .avatar-tip {
        font-size: 12px;
        color: #909399;
      }

      .avatar-info {
        h3 {
          margin: 0 0 10px 0;
          font-size: 20px;
          font-weight: 600;
          color: #303133;
        }

        .username {
          margin: 0;
          font-size: 14px;
          color: #909399;
        }
      }
    }

    .action-section {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
    }

    .detail-section {
      margin-top: 10px;
    }
  }
}
</style>

