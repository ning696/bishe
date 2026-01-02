<template>
  <el-drawer v-model="visibleLocal" title="校园详情" size="480px" :destroy-on-close="true">
    <el-skeleton v-if="loading" :rows="6" animated />
    <div v-else-if="detail" class="detail">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="校园ID">{{ detail.id }}</el-descriptions-item>
        <el-descriptions-item label="校园名称">{{ detail.campusName }}</el-descriptions-item>
        <el-descriptions-item label="校园编码">{{ detail.campusCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="校园地址">{{ detail.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detail.contactPerson || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.contactPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detail.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div v-else class="empty">暂无数据</div>
  </el-drawer>
</template>

<script setup lang="ts">
import { watch, ref } from 'vue'
import { getCampusDetail, type CampusDetailVO } from '@/api/admin/campus'

const props = defineProps<{
  visible: boolean
  campusId: number | null
}>()
const emit = defineEmits<{
  (e: 'update:visible', v: boolean): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const loading = ref(false)
const detail = ref<CampusDetailVO | null>(null)

watch(() => props.campusId, async (id) => {
  if (visibleLocal.value && id) {
    loading.value = true
    try {
      const res = await getCampusDetail({ campusId: id })
      detail.value = res.data
    } finally {
      loading.value = false
    }
  }
}, { immediate: true })

watch(visibleLocal, (open) => {
  if (open && props.campusId) {
    // refetch on open
    (async () => {
      loading.value = true
      try {
        const res = await getCampusDetail({ campusId: props.campusId! })
        detail.value = res.data
      } finally {
        loading.value = false
      }
    })()
  }
})
</script>

<style scoped>
.empty {
  color: #999;
}
</style>

