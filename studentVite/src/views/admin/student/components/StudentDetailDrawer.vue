<template>
  <el-drawer v-model="visibleLocal" title="学生详情" size="520px" :destroy-on-close="true">
    <el-skeleton v-if="loading" :rows="6" animated />
    <div v-else-if="detail" class="detail">
      <div class="header">
        <el-avatar :size="64" :src="detail.headImage" />
        <div class="meta">
          <div class="name">{{ detail.nickName || detail.username }}</div>
          <div class="sub">ID: {{ detail.id }}</div>
        </div>
      </div>
      <el-descriptions :column="1" border class="mt12">
        <el-descriptions-item label="用户名">{{ detail.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ detail.nickName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ detail.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detail.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="所在校园">{{ detail.campusName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ detail.major || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ detail.education || '-' }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ detail.grade || '-' }}</el-descriptions-item>
        <el-descriptions-item label="技能">{{ detail.skills || '-' }}</el-descriptions-item>
        <el-descriptions-item label="经历">{{ detail.experience || '-' }}</el-descriptions-item>
        <el-descriptions-item label="期望薪资">{{ detail.expectedSalary || '-' }}</el-descriptions-item>
        <el-descriptions-item label="期望地点">{{ detail.expectedLocation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail.status === 1" type="success">正常</el-tag>
          <el-tag v-else-if="detail.status === 2" type="danger">禁用</el-tag>
          <el-tag v-else-if="detail.status === 3" type="warning">待审核</el-tag>
          <el-tag v-else type="info">已拉黑</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ detail.createTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div v-else class="empty">暂无数据</div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { fetchStudentDetail, type StudentDetailVO } from '@/api/admin/student'

const props = defineProps<{
  visible: boolean
  studentId: number | null
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const loading = ref(false)
const detail = ref<StudentDetailVO | null>(null)

const loadDetail = async () => {
  if (!props.studentId) return
  loading.value = true
  try {
    const res = await fetchStudentDetail(props.studentId)
    detail.value = res.data || null
  } finally {
    loading.value = false
  }
}

watch(() => props.studentId, (id) => {
  if (visibleLocal.value && id) {
    loadDetail()
  }
})

watch(visibleLocal, (open) => {
  if (open) {
    loadDetail()
  } else {
    detail.value = null
  }
})
</script>

<style scoped>
.detail .header {
  display: flex;
  align-items: center;
}
.detail .meta {
  margin-left: 12px;
}
.detail .name {
  font-size: 16px;
  font-weight: 600;
}
.mt12 { margin-top: 12px; }
.empty {
  color: #999;
}
</style>

