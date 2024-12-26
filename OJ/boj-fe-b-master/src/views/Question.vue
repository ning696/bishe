<template>
  <!-- 表单 -->
  <el-form inline="true" ref="formModel" :model="form">
    <el-form-item label="题目难度" style="">
      <selector v-model="params.difficulty" placeholder="请选择题目难度"></selector>
    </el-form-item>
    <el-form-item label="题目名称">
      <el-input v-model="params.title" placeholder="请您输入要搜索的题目标题" style="" />
    </el-form-item>
    <el-form-item>
      <el-button @click="onSearch" plain>搜索</el-button>
      <el-button @click="onReset" plain type="info">重置</el-button>
      <el-button type="primary" :icon="Plus" plain @click="onAddQuestion">添加题目</el-button>
    </el-form-item>
  </el-form>
  <!-- 表格 -->
  <el-table height="526px" :data="questionList" v-loading="loading" class="el-table ">
    <el-table-column prop="questionId" width="180px" label="题目id" />
    <el-table-column prop="title" label="题目标题" :show-overflow-tooltip="true" />
    <el-table-column prop="difficulty" label="题目难度" width="90px">
      <template #default="{ row }">
        <div v-if="row.difficulty === 1" style="color:#3EC8FF;">简单</div>
        <div v-if="row.difficulty === 2" style="color:#FE7909;">中等</div>
        <div v-if="row.difficulty === 3" style="color:#FD4C40;">困难</div>
      </template>
    </el-table-column>
    <el-table-column prop="createName" label="创建人" width="140px" />
    <el-table-column prop="createTime" label="创建时间" width="180px" />
    <el-table-column label="操作" width="100px" fixed="right">
      <template #default="{ row }">
        <el-button class="red" type="text" @click="onEdit(row.questionId)">编辑
        </el-button>
        <el-button type="text" @click="onDelete(row.questionId)">删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页区域 -->
  <el-pagination small v-model:current-page="params.pageNum" v-model:page-size="params.pageSize"
    :page-sizes="[5, 10, 15, 20, 30]" :background="true" layout="total, sizes, prev, pager, next, jumper" :total="total"
    @size-change="handleSizeChange" @current-change="handleCurrentChange"
    style="margin-top: 20px; justify-content: flex-end" />

  <!-- 抽屉 -->
  <question-edit ref="questionEditRef" @success="onSuccess($event, str)">
  </question-edit>
</template>

<script setup>
import { ref } from "vue"
import QuestionEdit from '@/components/question/QuestionEdit.vue'
import { Edit, Delete, Plus } from '@element-plus/icons-vue'
import Selector from "@/components/question/QuestionSelector.vue"
import { getQuestionListService, delQuestionService } from '@/apis/question'

const questionList = ref([]) //题目列表
const total = ref(0)
const loading = ref(false)

const params = ref({
  pageNum: 1,
  pageSize: 10,
  difficulty: '',
  title: ''
})

const getQuestionList = async () => {
  const ref = await getQuestionListService(params.value)
  questionList.value = ref.rows
  console.log(questionList.value)
  total.value = ref.total
}
getQuestionList()

// 删除
const onDelete = async (questionId) => {
  loading.value = true
  const delRef = await delQuestionService(questionId)
  console.log(delRef)
  await getQuestionList()
  loading.value = false
}

const questionEditRef = ref()

// 添加
const onAddQuestion = () => {
  questionEditRef.value.open({})
}

// 成功回调
const onSuccess = (str) => {
  loading.value = true
  console.log(str)
  if (str === 'add') {
    // 如果是添加，需要跳转渲染第一页，编辑直接渲染当前页
    params.value.pageNum = 1
  }
  getQuestionList()
  loading.value = false
}

// 编辑
const onEdit = (row) => {
  // console.log('row', row)
  questionEditRef.value.open(row)
}

// 分页
const handleSizeChange = async (size) => {
  //size更新是需要回到第一页
  loading.value = true
  params.value.pageNum = 1
  params.value.pageSize = size
  await getQuestionList()
  loading.value = false
}
const handleCurrentChange = async (page) => {
  loading.value = true
  params.value.pageNum = page
  await getQuestionList()
  loading.value = false
}

// 搜索/重置
const onSearch = async () => {
  loading.value = true
  params.value.pageNum = 1
  await getQuestionList()
  loading.value = false
}

const onReset = async () => {
  loading.value = true
  params.value.pageNum = 1
  params.value.difficulty = ''
  params.value.title = ''
  await getQuestionList()
  loading.value = false
}


</script>