<template>
  <el-drawer v-model="visibleLocal" title="企业详情" size="520px" :destroy-on-close="true">
    <el-skeleton v-if="loading" :rows="6" animated />
    <div v-else-if="detail" class="detail">
      <div class="header">
        <el-avatar :size="64" :src="detail.logo" />
        <div class="meta">
          <div class="name">{{ detail.enterpriseName || detail.username }}</div>
          <div class="sub">ID: {{ detail.id }}</div>
        </div>
      </div>
      <el-descriptions :column="1" border class="mt12">
        <el-descriptions-item label="登录账号">{{ detail.username }}</el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ detail.enterpriseName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="法人代表">{{ detail.legalPerson || '-' }}</el-descriptions-item>
        <el-descriptions-item label="企业联系方式">{{ detail.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="企业邮箱">{{ detail.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="行业">{{ detail.industry || '-' }}</el-descriptions-item>
        <el-descriptions-item label="规模">{{ detail.scale || '-' }}</el-descriptions-item>
        <el-descriptions-item label="官网">{{ detail.website || '-' }}</el-descriptions-item>
        <el-descriptions-item label="简介">{{ detail.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="认证状态">
          <el-tag v-if="detail.certificationStatus === 1" type="success">已认证</el-tag>
          <el-tag v-else-if="detail.certificationStatus === 2" type="warning">认证中</el-tag>
          <el-tag v-else-if="detail.certificationStatus === 3" type="danger">认证失败</el-tag>
          <el-tag v-else type="info">未认证</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账号状态">
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
import { fetchEnterpriseDetail, type EnterpriseDetailVO } from '@/api/admin/enterprise'

const props = defineProps<{
  visible: boolean
  enterpriseId: number | null
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const loading = ref(false)
const detail = ref<EnterpriseDetailVO | null>(null)

const loadDetail = async () => {
  if (!props.enterpriseId) return
  loading.value = true
  try {
    const res = await fetchEnterpriseDetail(props.enterpriseId)
    detail.value = res.data || null
  } finally {
    loading.value = false
  }
}

watch(() => props.enterpriseId, (id) => {
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

