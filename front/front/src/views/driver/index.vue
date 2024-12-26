<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" size="small" label-width="68px">
      <el-form-item label="" prop="customerId">
        <el-input
          v-model="queryParams.customerId"
          placeholder="请输入顾客编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item  prop="carId">
        <el-input
          v-model="queryParams.carId"
          placeholder="请输入试驾车辆编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="试驾时间" prop="testDriveTime">-->
<!--        <el-date-picker-->
<!--          v-model="queryParams.testDriveTime"-->
<!--          clearable-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择试驾时间"/>-->
<!--      </el-form-item>-->
      <!--      <el-form-item label="预约电话" prop="phone">-->
      <!--        <el-input-->
      <!--          v-model="queryParams.phone"-->
      <!--          placeholder="请输入预约电话"-->
      <!--          clearable-->
      <!--          @keyup.enter.native="handleQuery"-->
      <!--        />-->
      <!--      </el-form-item>-->
      <el-form-item label="" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入顾客姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--    <el-row :gutter="10" class="mb8">-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="primary"-->
    <!--          plain-->
    <!--          icon="el-icon-plus"-->
    <!--          size="mini"-->
    <!--          @click="handleAdd"-->
    <!--        >新增</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          :disabled="single"-->
    <!--          type="success"-->
    <!--          plain-->
    <!--          icon="el-icon-edit"-->
    <!--          size="mini"-->
    <!--          @click="handleUpdate"-->
    <!--        >修改</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          :disabled="multiple"-->
    <!--          type="danger"-->
    <!--          plain-->
    <!--          icon="el-icon-delete"-->
    <!--          size="mini"-->
    <!--          @click="handleDelete"-->
    <!--        >删除</el-button>-->
    <!--      </el-col>-->
    <!--      <el-col :span="1.5">-->
    <!--        <el-button-->
    <!--          type="warning"-->
    <!--          plain-->
    <!--          icon="el-icon-download"-->
    <!--          size="mini"-->
    <!--          @click="handleExport"-->
    <!--        >导出</el-button>-->
    <!--      </el-col>-->
    <!--      <right-toolbar :show-search.sync="showSearch" @queryTable="getList"/>-->
    <!--    </el-row>-->


    <el-table v-loading="loading" :data="driveList" @selection-change="handleSelectionChange">
      <el-table-column label="预约试驾编号" align="center" prop="id" />
      <el-table-column label="顾客编号" align="center" prop="customerId" />
      <el-table-column label="试驾车辆编号" align="center" prop="carId" />
      <el-table-column label="试驾时间" align="center" prop="testDriveTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.testDriveTime ? scope.row.testDriveTime : '审核暂未通过' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预约状态" align="center" prop="status">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.status === '0' ? '待审核' :
              scope.row.status === '1' ? '审核通过' :
              scope.row.status === '2' ? '已完成' :
              scope.row.status === '3' ? '已拒绝' :
              '未知状态'
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="预约电话" align="center" prop="phone" />
      <el-table-column label="顾客姓名" align="center" prop="customerName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >通过</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="updatastate(scope.row,'3')"
          >拒绝</el-button>
          <el-button
            v-if="scope.row.status === '1'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="updatastate(scope.row,'2')"
          >完成</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改drive对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      @close="handleBeforeClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="180px">
        <el-form-item label="设置试驾时间" prop="testDriveTime">
          <el-date-picker
            v-model="form.testDriveTime"
            clearable
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择试驾时间"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="settestTime">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDrive, addDrive, updateDrive, refuse } from '../../api/drive'
import { parseTime } from '../../utils'

export default {
  name: 'Drive',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // drive表格数据
      driveList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        customerId: null,
        carId: null,
        testDriveTime: null,
        status: null,
        phone: null,
        customerName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        testDriveTime: [
          { required: true, message: '请选择试驾时间', trigger: 'blur' },
          { validator: this.validateTestDriveTime, trigger: 'blur' } // 添加自定义验证规则
        ],
        customerId: [
          { required: true, message: '顾客编号不能为空', trigger: 'blur' }
        ],
        carId: [
          { required: true, message: '试驾车辆编号不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '预约状态不能为空', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '预约电话不能为空', trigger: 'blur' }
        ],
        customerName: [
          { required: true, message: '顾客姓名不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
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
      this.$refs.dialogRef.resetFields()
      done() // 关闭对话框
    },
    validateTestDriveTime(rule, value, callback) {
      if (!value) {
        return callback(new Error('请选择试驾时间!'))
      }
      if (new Date(value).getTime() < Date.now()) {
        return callback(new Error('试驾时间不能在当前时间之前!'))
      }
      callback() // 验证通过
    },
    parseTime,
    /** 查询drive列表 */
    getList() {
      this.loading = true
      listDrive(this.queryParams).then(response => {
        this.driveList = response.data.rows
        this.total = response.data.total
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
        customerId: null,
        carId: null,
        testDriveTime: null,
        status: null,
        phone: null,
        customerName: null,
        createTime: null
      }
      this.$refs.queryForm.resetFields()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.open = true
      this.form.id = row.id
    },
    settestTime() {
      this.$refs['formRef'].validate(valid => {
        if (valid) {
          this.form.status = 1
          updateDrive(this.form).then(response => {
            this.$message.success('修改成功')
            this.open = false
            this.getList()
          })
        }
      })
    },
    updatastate(row, statues) {
      this.form.status = statues
      this.form.id = row.id
      updateDrive(this.form).then(response => {
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['dialogRef'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDrive(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addDrive(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 拒绝按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认拒绝预约申请吗？').then(function() {
        return refuse(row.id)
      }).then(() => {
        this.getList()
        this.$message.error('已拒绝')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/drive/export', {
        ...this.queryParams
      }, `drive_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
