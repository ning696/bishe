<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="68px">
      <!--      <el-form-item label="省编码" prop="provinceCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.provinceCode"-->
      <!--          placeholder="请输入省编码"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="市编码" prop="cityCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.cityCode"-->
      <!--          placeholder="请输入市编码"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <!--      <el-form-item label="区编码" prop="districtCode">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.districtCode"-->
      <!--          placeholder="请输入区编码"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item label="地址" prop="location">
        <el-cascader
          :options="pcaTextArr"
          v-model="selectedOptions"
          size="large"
          @change="setselectedOptions"/>
      </el-form-item>
      <el-form-item label="具体位置" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入具体位置"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phoneNumber">
        <el-input
          v-model="queryParams.phoneNumber"
          placeholder="请输入门店联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="single"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="multiple"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <el-table v-loading="loading" :data="addressList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="地址id" align="center" prop="id" width="100"/>
      <el-table-column label="省" align="center" prop="provinceCode" width="120"/>
      <el-table-column label="市" align="center" prop="cityCode" width="120"/>
      <el-table-column label="区" align="center" prop="districtCode" width="120"/>
      <el-table-column label="具体位置" align="center" prop="location" width="120"/>
      <el-table-column label="联系电话" align="center" prop="phoneNumber" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="更新时间" align="center" prop="updatedAt" width="120">-->
      <!--        <template slot-scope="scope">-->
      <!--          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--    <pagination-->
    <!--      v-show="total>0"-->
    <!--      :total="total"-->
    <!--      :page.sync="queryParams.pageNum"-->
    <!--      :limit.sync="queryParams.pageSize"-->
    <!--      @pagination="getList"-->
    <!--    />-->

    <!-- 添加或修改门店对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      @close="handleBeforeClose">

      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <!--        <el-form-item label="省编码" prop="provinceCode">-->
        <!--          <el-input v-model="form.provinceCode" placeholder="请输入省编码" />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="市编码" prop="cityCode">-->
        <!--          <el-input v-model="form.cityCode" placeholder="请输入市编码" />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="区编码" prop="districtCode">-->
        <!--          <el-input v-model="form.districtCode" placeholder="请输入区编码" />-->
        <!--        </el-form-item>-->
        <el-form-item label="具体位置" prop="location">
          <el-cascader
            :options="pcaTextArr"
            v-model="selectedOptions"
            size="large"
            @change="setselectedOptions"/>
        </el-form-item>
        <el-form-item label="具体位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入具体位置" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNumber">
          <el-input v-model="form.phoneNumber" placeholder="请输入门店联系电话" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAddress, getAddress, delAddress, addAddress, updateAddress } from '../../api/address'
import { pcaTextArr } from 'element-china-area-data'
import { parseTime } from '../../utils'

export default {
  name: 'Address',
  data() {
    return {
      pcaTextArr,
      selectedOptions: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 门店表格数据
      addressList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        provinceCode: null,
        cityCode: null,
        districtCode: null,
        location: null,
        phoneNumber: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        provinceCode: [
          { required: true, message: '省编码不能为空', trigger: 'blur' }
        ],
        cityCode: [
          { required: true, message: '市编码不能为空', trigger: 'blur' }
        ],
        districtCode: [
          { required: true, message: '区编码不能为空', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '具体位置不能为空', trigger: 'blur' }
        ],
        phoneNumber: [
          { required: true, message: '门店联系电话不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleBeforeClose(done) {
      // 在关闭之前执行的操作，比如你可以添加确认提示
      this.reset()
      done() // 关闭对话框
    },
    parseTime,
    setselectedOptions() {
      this.form.provinceCode = this.selectedOptions[0]
      this.form.districtCode = this.selectedOptions[2]
      this.form.cityCode = this.selectedOptions[1]
    },
    /** 查询门店列表 */
    getList() {
      this.loading = true
      listAddress(this.queryParams).then(response => {
        this.addressList = response.data.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        provinceCode: null,
        cityCode: null,
        districtCode: null,
        location: null,
        phoneNumber: null,
        createdAt: null,
        updatedAt: null
      }
      this.selectedOptions = []
      this.$refs.queryForm.resetFields()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.reset()
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加门店'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getAddress(id).then(response => {
        const hdata = response.data.data
        this.form = hdata
        this.open = true
        this.title = '修改门店'
        this.selectedOptions = [hdata.provinceCode, hdata.districtCode, hdata.cityCode]
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAddress(this.form).then(response => {
              this.$message.success('修改成功')
              this.reset()
              this.open = false
              this.getList()
            })
          } else {
            addAddress(this.form).then(response => {
              this.$message.success('新增成功')
              this.reset()
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$confirm('是否确认删除门店编号为"' + ids + '"的数据项？').then(function() {
        return delAddress(ids)
      }).then(() => {
        this.getList()
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/address/export', {
        ...this.queryParams
      }, `address_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
