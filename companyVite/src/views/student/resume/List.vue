<template>
  <div class="student-resume-list">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <span>我的简历</span>
            <el-button type="primary" link @click="loadResumes">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
          <el-button type="primary" @click="handleCreate">新建简历</el-button>
        </div>
      </template>
      <el-table
        v-loading="loading"
        :data="resumeList"
        border
        empty-text="暂无简历，请新建"
      >
        <el-table-column label="简历名称" min-width="200">
          <template #default="{ row }">
            <div class="resume-name">
              {{ row.resumeName }}
              <el-tag v-if="row.isDefault === 1" type="success" size="small">
                默认
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 0 ? '草稿' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="180" />
        <el-table-column label="更新时间" prop="updateTime" width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row.id)">
              查看/编辑
            </el-button>
            <el-divider direction="vertical" />
            <el-popconfirm
              title="确定删除该简历吗？"
              @confirm="handleDelete(row.id)"
            >
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  fetchResumeList,
  deleteResume,
  type ResumeListItem
} from '@/api/student/resume'
import { Refresh } from '@element-plus/icons-vue'

const router = useRouter()
const loading = ref(false)
const resumeList = ref<ResumeListItem[]>([])

const loadResumes = async () => {
  loading.value = true
  try {
    const res = await fetchResumeList()
    resumeList.value = res.data ?? []
  } catch (error) {
    console.error('加载简历列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  router.push({ path: '/student/resume/edit' })
}

const handleEdit = (id: number) => {
  router.push({ path: `/student/resume/edit/${id}` })
}

const handleDelete = async (id: number) => {
  try {
    await deleteResume(id)
    ElMessage.success('删除成功')
    loadResumes()
  } catch (error) {
    console.error('删除简历失败', error)
  }
}

onMounted(() => {
  loadResumes()
})
</script>

<style scoped lang="scss">
.student-resume-list {
  padding: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;
    font-weight: 600;
  }
}

.resume-name {
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>

