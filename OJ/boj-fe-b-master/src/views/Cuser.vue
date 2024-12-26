<template>
  <page-container title="用户管理">
    <!-- 表单 -->
    <template #default>
      <el-form inline="true" ref="formModel" :model="form">
        <el-form-item label="用户id">
          <el-input v-model="params.userId" placeholder="请您输入要搜索的用户id" style="" />
        </el-form-item>
        <el-form-item label="用户账号">
          <el-input v-model="params.userAccount" placeholder="请您输入要搜索的用户账号" style="" />
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input v-model="params.nickName" placeholder="请您输入要搜索的用户昵称" style="" />
        </el-form-item>
        <el-form-item>
          <el-button @click="onSearch" plain >搜索</el-button>
          <el-button @click="onReset" plain type="info">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- 表格 -->
      <el-table height="526px" :data="userList" v-loading="loading" class="el-table">
        <el-table-column prop="userId" label="用户id" width="180px"/>
        <el-table-column prop="userAccount" label="用户账号" width="140px"/>
        <el-table-column prop="nickName" label="用户昵称" />
        <el-table-column prop="phone" width="120px" label="手机号" />
        <el-table-column prop="email" width="120px" label="邮箱" />
        <el-table-column prop="wechat" width="120px" label="微信号" />
        <!-- <el-table-column label="联系方式" width="180px">
          <template #default="{ row }">
            <span class="block-span"> email: {{ row.email }}</span>
            <span class="block-span"> 电话: {{ row.phone }}</span>
            <span class="block-span"> 微信号: {{ row.wechat }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="学校/专业" width="150px">

          <template #default="{ row }">
            <span class="block-span"> 学校: {{ row.schoolName }}</span>
            <span class="block-span"> 专业: {{ row.majorName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="introduce" :show-overflow-tooltip="true" label="个人介绍" />
        <el-table-column prop="status" width="90px" align="center" label="用户状态">

          <template #default="{ row }">
            <el-tag type="success" v-if="row.status">正常</el-tag>
            <el-tag type="error" v-else>拉黑</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80px" fixed="right">

          <template #default="{ row }">
            <el-button class="red" v-if="row.status == 1" type="text" plain
              @click="onUpdateUserStatus(row.userId, 0)">拉黑</el-button>
            <el-button v-if="row.status == 0" type="text" plain
              @click="onUpdateUserStatus(row.userId, 1)">解禁</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination small v-model:current-page="params.pageNum" v-model:page-size="params.pageSize"
        :page-sizes="[5, 10, 15, 20, 30]" :background="true" layout="total, sizes, prev, pager, next, jumper"
        :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end" />

    </template>
  </page-container>
</template>

<script setup>
import { ref } from 'vue';
import { getUserListService, updateStatusService } from '@/apis/user'
import { CircleClose } from '@element-plus/icons-vue'

const params = ref({
  pageNum: 1,
  pageSize: 10,
  userId: '',
  userAccount: '',
  nickName: '',
  introduce: '',
})

const updateStatusParams = ref({
  userId: '',
  status: '',
})

const userList = ref([])
const total = ref(0)
const loading = ref(false)

const getUserList = async () => {
  const ref = await getUserListService(params.value)
  console.log('userlist:' ,ref)
  userList.value = ref.rows
  console.log(userList.value)
  total.value = ref.total
}
getUserList()

const onSearch = async () => {
  loading.value = true
  params.value.pageNum = 1
  await getUserList()
  loading.value = false
}

const onReset = async () => {
  loading.value = true
  params.value.pageNum = 1
  params.value.userId = ''
  params.value.userAccount = ''
  params.value.nickName = ''
  params.value.introduce = ''
  await getUserList()
  loading.value = false
}

const onUpdateUserStatus = async (userId, status) => {
  loading.value = true
  updateStatusParams.value.userId = userId
  updateStatusParams.value.status = status
  await updateStatusService(updateStatusParams.value)
  await getUserList()
  loading.value = false
}

</script>


<style lang="scss">
.block-span {
  display: block;
}
</style>