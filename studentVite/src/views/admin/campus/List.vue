<template>
  <div class="admin-campus-list-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>校园列表</span>
          <el-button type="primary" @click="openAdd">新增校园</el-button>
        </div>
      </template>
      <el-form :inline="true" :model="query" class="mb12">
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 160px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="校园名称/编码/地址/联系人" clearable style="width: 240px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="list" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="campusName" label="校园名称" min-width="160" />
        <el-table-column prop="campusCode" label="校园编码" min-width="120" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contactPerson" label="联系人" min-width="100" />
        <el-table-column prop="contactPhone" label="联系电话" min-width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="openDetail(row)">详情</el-button>
            <el-button size="small" type="warning" link @click="openEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt12 flex-end">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="query.pageNum"
          :page-size="query.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @size-change="onSizeChange"
          @current-change="onCurrentChange"
        />
      </div>
    </el-card>

    <AddDialog v-model:visible="addVisible" :edit-data="editData" @success="fetchList" />
    <DetailDrawer v-model:visible="detailVisible" :campus-id="currentId" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCampusPage, deleteCampus, type CampusQueryParams, type CampusVO } from '@/api/admin/campus'
import AddDialog from './components/AddDialog.vue'
import DetailDrawer from './components/DetailDrawer.vue'

const loading = ref(false)
const list = ref<CampusVO[]>([])
const total = ref(0)
const query = reactive<CampusQueryParams>({
  pageNum: 1,
  pageSize: 10,
  status: undefined,
  keyword: ''
})

const addVisible = ref(false)
const detailVisible = ref(false)
const currentId = ref<number | null>(null)
const editData = ref<CampusVO | null>(null)

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getCampusPage(query)
    list.value = res.rows || []
    total.value = res.total || 0
  } catch (e) {
    // 已统一处理
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.pageNum = 1
  fetchList()
}

const resetQuery = () => {
  query.status = undefined
  query.keyword = ''
  handleSearch()
}

const onSizeChange = (size: number) => {
  query.pageSize = size
  query.pageNum = 1
  fetchList()
}
const onCurrentChange = (page: number) => {
  query.pageNum = page
  fetchList()
}

const openAdd = () => {
  editData.value = null
  addVisible.value = true
}

const openEdit = (row: CampusVO) => {
  editData.value = row
  addVisible.value = true
}

const openDetail = (row: CampusVO) => {
  currentId.value = row.id
  detailVisible.value = true
}

const handleDelete = async (row: CampusVO) => {
  try {
    await ElMessageBox.confirm('确定要删除该校园吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCampus({ campusId: row.id })
    ElMessage.success('删除成功')
    fetchList()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '删除失败')
    }
  }
}

onMounted(fetchList)
</script>

<style scoped>
.admin-campus-list-page {
  padding: 16px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.mb12 { margin-bottom: 12px; }
.mt12 { margin-top: 12px; }
.flex-end {
  display: flex;
  justify-content: flex-end;
}
</style>

