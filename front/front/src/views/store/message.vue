<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.id" placeholder="车辆编号" clearable style="width: 160px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.brandId" class="filter-item" clearable placeholder="品牌" style="width: 160px;" @change="changeSeries" @keyup.enter.native="handleFilter">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
      <el-select v-model="listQuery.seriesId" class="filter-item" clearable placeholder="车系" style="width: 160px;" @keyup.enter.native="handleFilter">
        <el-option v-for="item in seriesOptions" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
      <el-select v-model="listQuery.status" placeholder="状态" clearable class="filter-item" style="width: 110px" @keyup.enter.native="handleFilter">
        <el-option v-for="item in carStatusOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :key="tableKey"
      :data="list"
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange">
      <el-table-column label="车辆编号" prop="id" sortable="custom" align="center" min-width="80px">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="品牌/车系/型号" align="center" min-width="215px">
        <template slot-scope="scope">
          <span>{{ scope.row.brandName }}/{{ scope.row.seriesName }}</span><br>
          <span>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column label="图片" align="center" min-width="120px">
        <template slot-scope="scope">
          <el-image :src="getImageUrl(scope.row.imgPath)"/>
        </template>
      </el-table-column>
      <el-table-column label="颜色" align="center" min-width="75px">
        <template slot-scope="scope">
          <span>{{ scope.row.color }}</span>
        </template>
      </el-table-column>
      <el-table-column label="进价" sortable="custom" prop="price" align="center" min-width="105px">
        <template slot-scope="scope">
          <span>￥ {{ scope.row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="售价" sortable="custom" prop="salePrice" align="center" min-width="105px">
        <template slot-scope="scope">
          <span>￥ {{ scope.row.salePrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="库存数" prop="repertory" min-width="85px" sortable="custom" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.repertory }}</span>
        </template>
      </el-table-column>
      <el-table-column label="入库时间" prop="createTime" sortable="custom" min-width="135px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="90px">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | typeFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column v-if="checkPermission(['admin'])" label="操作" align="center" min-width="180px">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-permission="['admin']" size="mini" type="primary" @click="handleSetPromotion(scope.row)">设为宣传</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog v-if="checkPermission(['admin'])" :visible.sync="dialogFormVisible" title="修改库存信息">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="品牌 & 车系" prop="series">
          <el-cascader :options="options" v-model="temp.series" placeholder="品牌/车系" style="width: 220px"/>
        </el-form-item>
        <el-form-item label="型号" prop="type">
          <el-input v-model="temp.type"/>
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="temp.color"/>
        </el-form-item>
        <el-form-item label="进价" prop="price">
          <el-input v-model.number="temp.price"/>
        </el-form-item>
        <el-form-item label="售价" prop="salePrice">
          <el-input v-model.number="temp.salePrice"/>
        </el-form-item>
        <el-form-item label="数量" prop="repertory">
          <el-input-number v-model="temp.repertory" :min="0"/>
        </el-form-item>
        <el-form-item label="状态" required>
          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
            <el-option v-for="item in carStatusOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog v-if="checkPermission(['admin'])" :visible.sync="promotionDialogVisible" title="设为宣传">
      <el-form ref="promotionForm" :model="promotionData" label-position="right" label-width="100px" style="width: 400px;">
        <el-form-item label="宣传词" prop="promotionText" :rules="promotionRules">
          <el-input v-model="promotionData.promotionText" type="text" placeholder="请输入宣传词（5-20字）" maxlength="20"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="promotionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPromotion">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // Waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import checkPermission from '@/utils/permission' // 权限判断函数
import { fetchBrand, fetchSeries } from '@/api/init'
import { fetchList, insertPromotion, updateStore } from '../../api/store'

const carStatusOptions = [
  { key: '0', display_name: '停售' },
  { key: '1', display_name: '在售' },
  { key: '2', display_name: '缺货' }
]

const calendarTypeKeyValue = carStatusOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  components: { Pagination },
  directives: {
    waves
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'info',
        1: 'success',
        2: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      promotionDialogVisible: false, // 控制弹框显示
      promotionData: {
        promotionText: ''
      },
      promotionRules: {
        promotionText: [
          { required: true, message: '请输入宣传词', trigger: 'blur' },
          { min: 5, max: 20, message: '宣传词必须在 5 到 20 字之间', trigger: 'blur' }
        ]
      },
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 5,
        id: '24110800003',
        brandId: undefined,
        seriesId: undefined,
        status: undefined,
        orderBy: undefined
      },
      carStatusOptions,
      temp: {
        id: undefined,
        series: [],
        seriesId: undefined,
        type: '',
        color: '',
        price: undefined,
        salePrice: undefined,
        repertory: undefined,
        status: undefined
      },
      // dialogFormVisible：编辑或添加Form的可见性
      dialogFormVisible: false,
      options: [],
      seriesOptions: [],
      rules: {
        seriesId: [
          { required: true, message: '请输入品牌 & 车系' }
        ],
        type: [
          { required: true, message: '请输入车辆型号', trigger: 'blur' }
        ],
        color: [
          { required: true, message: '请输入车辆颜色', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入车辆进价', trigger: 'blur' },
          { type: 'number', message: '进价必须为数字值' }
        ],
        salePrice: [
          { required: true, message: '请输入车辆售价', trigger: 'blur' },
          { type: 'number', message: '售价必须为数字值' }
        ],
        repertory: [
          { required: true, message: '请输入车辆库存' }
        ]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getSeriesOpt()
  },
  methods: {
    // 打开设为宣传弹框
    handleSetPromotion(row) {
      if(row.status !== '1'){
        this.$message({
          message: '请先将车辆状态设置为在售',
          type: 'error'
        })
        return;
      }
      this.selectedRow = row; // 保存选择的行数据，用于后续提交
      this.promotionData.promotionText = ''; // 清空宣传词输入框
      this.promotionDialogVisible = true; // 显示弹框
    },
    submitPromotion() {
      this.$refs['promotionForm'].validate((valid) => {
        if (valid) {
          // 调用接口将宣传词提交
          const promotionInfo = {
            id: this.selectedRow.id,
            promotionText: this.promotionData.promotionText
          };

          // 假设你有一个 API 方法 `updatePromotion` 用于更新宣传信息
          insertPromotion(promotionInfo).then(response => {
            if (response.data.code === 20000) {
              // 更新本地数据（例如直接修改表格中的数据）
              this.selectedRow.promotionText = this.promotionData.promotionText;
              this.$notify({
                title: '成功',
                message: '宣传词设置成功',
                type: 'success',
                duration: 2000
              });
              this.promotionDialogVisible = false;
            } else {
              this.$notify({
                title: '错误',
                message: response.data.message,
                type: 'error',
                duration: 2000
              });
            }
          });
        }
      });
    },
    // 获取图片
    getImageUrl(imageId) {
      // 发送 AJAX 请求获取图片 URL
      // 这里使用 axios 作为例子，你可能需要根据你的实际情况替换为你的 AJAX 库
      // return new Promise((resolve, reject) => {
      //   axios.get(`http://localhost:8080/upload/imgs/${imageId}`)
      //     .then(response => {
      //       resolve(response.data.url)
      //     })
      //     .catch(error => {
      //       reject(error)
      //     })
      // })
      return `http://localhost:8080/upload/imgs/${imageId}`
    },
    // 获取series
    getSeriesOpt() {
      fetchSeries().then(response => {
        this.options = response.data.data
      })
    },
    // 获取brand
    getBrandOpt() {
      fetchBrand().then(response => {
        this.brandOptions = response.data.data
      })
    },
    checkPermission,
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        if (response.data.code === 20000) {
          this.list = response.data.data.items
          this.total = response.data.data.total
        } else {
          this.$message({
            message: response.data.message,
            type: 'error'
          })
        }
        this.listLoading = false

        // Just to simulate the time of the request
        // setTimeout(() => {
        //   this.listLoading = false
        // }, 0.5 * 1000)
      })
    },
    changeSeries() {
      this.listQuery.seriesId = null
      const brand = this.listQuery.brandId
      if (brand === '' || brand === null) {
        this.seriesOptions = []
      } else {
        let index
        for (const v of this.options) {
          if (v.value === brand) {
            index = this.options.indexOf(v)
            break
          }
        }
        this.seriesOptions = this.options[index].children
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    sortChange(data) {
      const { prop, order } = data
      if (order === 'ascending') {
        this.listQuery.orderBy = prop + ' asc'
      } else {
        this.listQuery.orderBy = prop + ' desc'
      }
      this.handleFilter()
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.series = [row.brandId, row.seriesId]
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.seriesId = this.temp.series[1]
          tempData.createTime = null
          tempData.series = null
          updateStore(tempData).then(response => {
            if (response.data.code === 20000) {
              if (tempData.seriesId === this.temp.seriesId) {
                for (const v of this.list) {
                  if (v.id === this.temp.id) {
                    const index = this.list.indexOf(v)
                    this.list.splice(index, 1, this.temp)
                    break
                  }
                }
              } else {
                this.getList()
              }
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: '错误',
                message: response.data.message,
                type: 'error',
                duration: 2000
              })
            }
            this.dialogFormVisible = false
          })
        }
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['库存编号', '品牌', '车系', '型号', '颜色', '进价', '售价', '入库时间', '状态']
        const filterVal = ['id', 'brandName', 'seriesName', 'type', 'color', 'price', 'salePrice', 'createTime', 'status']
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: '库存信息'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'createTime') {
          return parseTime(v[j])
        } else if (j === 'status') {
          return calendarTypeKeyValue[v[j]]
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>
