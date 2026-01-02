<template>
  <el-dialog v-model="visibleLocal" :title="isEdit ? '编辑校园' : '新增校园'" width="600px" :close-on-click-modal="false">
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="校园名称" prop="campusName">
        <el-input v-model="form.campusName" placeholder="请输入校园名称" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="校园编码" prop="campusCode">
        <el-input v-model="form.campusCode" placeholder="请输入校园编码" maxlength="50" />
      </el-form-item>
      <el-form-item label="校园地址" prop="address">
        <el-input
          v-model="form.address"
          placeholder="请输入校园地址"
          type="textarea"
          :rows="2"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="联系人" prop="contactPerson">
        <el-input v-model="form.contactPerson" placeholder="请输入联系人" maxlength="50" />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="form.contactPhone" placeholder="请输入联系电话" maxlength="20" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          placeholder="请输入备注"
          type="textarea"
          :rows="3"
          maxlength="500"
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
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { addCampus, updateCampus, type CampusAddDTO, type CampusUpdateDTO, type CampusVO } from '@/api/admin/campus'

const props = defineProps<{
  visible: boolean
  editData?: CampusVO | null
}>()
const emit = defineEmits<{
  (e: 'update:visible', v: boolean): void
  (e: 'success'): void
}>()

const visibleLocal = ref(false)
watch(() => props.visible, v => { visibleLocal.value = v })
watch(visibleLocal, v => emit('update:visible', v))

const isEdit = computed(() => !!props.editData)

const form = ref<CampusAddDTO & { id?: number }>({
  campusName: '',
  campusCode: '',
  address: '',
  contactPerson: '',
  contactPhone: '',
  status: 1,
  remark: ''
})

const rules = {
  campusName: [{ required: true, message: '请输入校园名称', trigger: 'blur' }],
  contactPhone: [
    { 
      validator: (rule: any, value: string, callback: any) => {
        if (value && !/^1[3-9]\d{9}$/.test(value)) {
          callback(new Error('请输入正确的手机号'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

const formRef = ref()
const submitting = ref(false)

watch(visibleLocal, (open) => {
  if (open) {
    if (props.editData) {
      // 编辑模式，填充表单
      form.value = {
        id: props.editData.id,
        campusName: props.editData.campusName || '',
        campusCode: props.editData.campusCode || '',
        address: props.editData.address || '',
        contactPerson: props.editData.contactPerson || '',
        contactPhone: props.editData.contactPhone || '',
        status: props.editData.status ?? 1,
        remark: ''
      }
    } else {
      // 新增模式，重置表单
      form.value = {
        campusName: '',
        campusCode: '',
        address: '',
        contactPerson: '',
        contactPhone: '',
        status: 1,
        remark: ''
      }
    }
    formRef.value?.clearValidate()
  }
})

const onSubmit = async () => {
  await formRef.value?.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateCampus(form.value as CampusUpdateDTO)
      ElMessage.success('更新成功')
    } else {
      await addCampus(form.value as CampusAddDTO)
      ElMessage.success('新增成功')
    }
    emit('success')
    visibleLocal.value = false
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
</style>

