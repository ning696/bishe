<template>
  <div title="竞赛管理">
      <el-form inline="true" ref="formModel" :model="form">
        <el-form-item label="创建日期" prop="datetimerange">
          <el-date-picker style="width: 240px" v-model="params.datetimerange" type="datetimerange" range-separator="至"
            start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="竞赛名称">
          <el-input v-model="params.title" placeholder="请您输入要搜索的竞赛名称" style="" />
        </el-form-item>
        <el-form-item>
          <el-button @click="onSearch" plain >搜索</el-button>
          <el-button @click="onReset" plain type="info">重置</el-button>
          <el-button type="primary" :icon="Plus" plain @click="onAddExam">添加竞赛</el-button>
        </el-form-item>
      </el-form>
      <!-- 表格 -->
      <el-table height="526px" :data="examList" style="width: 100%" v-loading="loading">
        <el-table-column prop="title" label="竞赛标题" min-width="160px" :show-overflow-tooltip="true"/>
        <el-table-column prop="startTime" width="180px" label="竞赛开始时间" />
        <el-table-column prop="endTime" width="180px" label="竞赛结束时间" />
        <el-table-column label="是否开赛" width="100px">
          <template #default="{ row }">
            <div v-if="!isNotStartExam(row)">
              <el-tag type="warning">已开赛</el-tag>
            </div>
            <div v-else>
              <el-tag type="info">未开赛</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" width="100px" label="是否发布">

          <template #default="{ row }">
            <div v-if="row.status == 0">
              <el-tag type="danger">未发布</el-tag>
            </div>
            <div v-if="row.status == 1">
              <el-tag type="success">已发布</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createName" width="140px" label="创建用户" />
        <el-table-column prop="createTime" width="180px" label="创建时间" />
        <el-table-column label="操作" width="180px" align="center"  fixed="right">

          <template #default="{ row }">
            <el-button v-if="isNotStartExam(row)" type="text" @click="onEdit(row)">编辑
            </el-button>
            <el-button v-if="isNotStartExam(row)" type="text"
              @click="onDelete(row)">删除
            </el-button>
            <el-button v-if="row.status == 1 && isNotStartExam(row)" type="text"
              @click="cancelPublishExamInList(row.examId)">撤销发布</el-button>
            <el-button v-if="row.status == 0 && isNotStartExam(row) && row.questionCount > 0"
              type="text" @click="publishExamInList(row.examId)">发布</el-button>
            <el-button type="text"  v-if="!isNotStartExam(row)" style="color: #999;">已开赛，不允许操作</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination small v-model:current-page="params.pageNum" v-model:page-size="params.pageSize"
        :page-sizes="[5, 10, 15, 20, 30]" :background="true" layout="total, sizes, prev, pager, next, jumper"
        :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end" />
  </div>
</template>

<script setup>
import { ref } from "vue"
import { Edit, Delete,Plus } from '@element-plus/icons-vue'
import { getExamListService, delExamService, publishService, cancelPublishService } from '@/apis/exam'
import { parseTime } from "@/utils/date"
import router from "@/router"
import { ElMessage, roleTypes } from "element-plus"


const examList = ref([]) //题目列表
const total = ref(0)
const loading = ref(false)

const params = ref({
  pageNum: 1,
  pageSize: 10,
  datetimerange: [],
  qStartTime: '',
  qEndTime: '',
  title: ''
})

const getExamList = async () => {
  const [qStartTime, qEndTime] = params.value.datetimerange
  params.value.qStartTime = parseTime(qStartTime)
  params.value.qEndTime = parseTime(qEndTime)
  const ref = await getExamListService(params.value)
  console.log(ref)
  examList.value = ref.rows
  total.value = ref.total
}
getExamList()


const examEditRef = ref()

// 添加
const onAddExam = () => {
  console.log('跳转至新增竞赛页面')
  router.push('/oj/exam/recruit/updateExam?type=add')
}

// 成功回调
const onSuccess = (str) => {
  loading.value = true
  console.log(str)
  if (str === 'add') {
    // 如果是添加，需要跳转渲染第一页，编辑直接渲染当前页
    params.value.pageNum = 1
  }
  getExamList()
  loading.value = false
}

const isNotStartExam = (exam) => {
  const now = new Date(); //当前时间
  return new Date(exam.startTime) > now
}

// 编辑
const onEdit = (row) => {
  // 获取当前时间戳
  let nowTime = new Date().getTime()
  if (new Date(row.startTime).getTime()<nowTime) {
    ElMessage.error('已经开始的竞赛不允许编辑')
  } else {
    console.log('跳转至编辑竞赛页面')
    router.push(`/oj/exam/recruit/updateExam?examId=${row.examId}&type=edit`)
  }
}

// 删除
const onDelete = async (row) => {
  console.log('删除：', row.examId)
  if (!isNotStartExam(row)) {
    ElMessage.error('已经开始的竞赛不允许删除')
  } else {
    loading.value = true
    await delExamService(row.examId)
    await getExamList()
    loading.value = false
  }
}

// 分页
const handleSizeChange = async (size) => {
  //size更新是需要回到第一页
  loading.value = true
  params.value.pageNum = 1
  params.value.pageSize = size
  await getExamList()
  loading.value = false
}
const handleCurrentChange = async (page) => {
  loading.value = true
  params.value.pageNum = page
  await getExamList()
  loading.value = false
}

// 搜索/重置
const onSearch = async () => {
  loading.value = true
  params.value.pageNum = 1
  await getExamList()
  loading.value = false
}

const onReset = async () => {
  loading.value = true
  params.value.pageNum = 1
  params.value.qStartTime = ''
  params.value.qEndTime = ''
  params.value.datetimerange = ''
  params.value.title = ''
  await getExamList()
  loading.value = false
}

const cancelPublishExamInList = async (examId) => {
  const examDto = ref({
    examId: examId
  })
  loading.value = true
  await cancelPublishService(examDto.value)
  await getExamList()
  loading.value = false
}

const publishExamInList = async (examId) => {
  const examDto = ref({
    examId: examId
  })
  loading.value = true
  await publishService(examDto.value)
  await getExamList()
  loading.value = false
}

</script>