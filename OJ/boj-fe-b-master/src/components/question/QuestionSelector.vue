<template>
  <el-select :model-value="modelValue" @update:modelValue="emit('update:modelValue', $event)" style="width: 100%">
    <el-option v-for="item in difficultyList" :key="item.difficulty" :label="item.difficultyName"
      :value="item.difficulty" />
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

const difficultyList = ref([
  {
    "difficulty": 1,
    "difficultyName": "简单",
  },
  {
    "difficulty": 2,
    "difficultyName": "中等",
  },
  {
    "difficulty": 3,
    "difficultyName": "困难",
  }
]) 
</script>