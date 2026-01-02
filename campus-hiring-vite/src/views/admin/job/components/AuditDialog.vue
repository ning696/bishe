<template>
  <el-dialog v-model="visibleLocal" title="职位审核" width="480px" :close-on-click-modal="false">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="审核结果" prop="auditResult">
        <el-radio-group v-model="form.auditResult">
          <el-radio label="pass">通过</el-radio>
          <el-radio label="reject">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          placeholder="可填写拒绝原因或备注"
          type="textarea"
          :rows="3"
          maxlength="200"
          show-word-limit
        />
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
import { auditJob } from '@/api/admin/job'

const props = defineProps<{
  visible: boolean
  jobId: number | null
}>()
const emit = defineEmits<{
  (e: 'update:visible', v: boolean): void
  (e: 'success'): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const form = ref<{ auditResult: 'pass' | 'reject' | ''; remark?: string }>({ auditResult: '' })
const rules = {
  auditResult: [{ required: true, message: '请选择审核结果', trigger: 'change' }]
}
const formRef = ref()
const submitting = ref(false)

watch(visibleLocal, (open) => {
  if (open) {
    form.value = { auditResult: '', remark: '' }
  }
})

const onSubmit = async () => {
  if (!props.jobId) return
  await formRef.value?.validate()
  submitting.value = true
  try {
    await auditJob({
      jobId: props.jobId,
      status: form.value.auditResult === 'pass' ? 1 : 2,
      auditRemark: form.value.remark
    })
    ElMessage.success('审核成功')
    emit('success')
    visibleLocal.value = false
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
</style>


