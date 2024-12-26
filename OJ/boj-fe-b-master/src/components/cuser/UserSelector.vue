<template>
  <el-select :model-value="modelValue" @update:modelValue="emit('update:modelValue', $event)" style="width: 100%">
    <el-option v-for="item in userStatusList" :key="item.status" :label="item.statusName"
      :value="item.status" />
  </el-select>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  modelValue: {
    type: [Number, String]
  }
})

const emit = defineEmits(['update:modelValue'])

const userStatusList = ref([
  {
    "status": 0,
    "statusName": "拉黑",
  },
  {
    "status": 1,
    "statusName": "正常",
  }
]) 
</script>