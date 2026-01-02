<template>
  <el-dialog v-model="visibleLocal" title="更新学生状态" width="420px" :close-on-click-modal="false">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择" style="width: 200px">
          <el-option label="正常" :value="1" />
          <el-option label="禁用" :value="2" />
          <el-option label="待审核" :value="3" />
          <el-option label="已拉黑" :value="0" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visibleLocal = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="onSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { updateStudentStatus } from '@/api/admin/student'

const props = defineProps<{
  visible: boolean
  studentId: number | null
  currentStatus: number | null
}>()

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'success'): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const form = ref<{ status: number | null }>({ status: null })
const rules = {
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}
const formRef = ref()
const submitting = ref(false)

watch(() => props.currentStatus, (status) => {
  form.value.status = status ?? null
}, { immediate: true })

const onSubmit = async () => {
  if (!props.studentId) return
  await formRef.value?.validate()
  submitting.value = true
  try {
    await updateStudentStatus({ studentId: props.studentId, status: form.value.status as number })
    ElMessage.success('更新成功')
    emit('success')
    visibleLocal.value = false
  } finally {
    submitting.value = false
  }
}
</script>

