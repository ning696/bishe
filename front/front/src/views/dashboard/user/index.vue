<template>
  <div class="app-container">
    <!-- 单个商品展示区域 -->
    <div v-for="(item, index) in list" :key="index" class="product-display">
      <!-- 左半部分：图片区域 -->
      <div class="product-image-section">
        <el-image
          :src="getImageUrl(item.carImageUrl)"
          fit="cover"
          class="product-image"
          alt="商品图片"
        />
      </div>

      <!-- 右半部分：品牌、车系、型号、宣传词 -->
      <div class="product-info-section">
        <h1 class="brand-name">{{ item.brandName }}</h1>
        <h2 class="series-name">{{ item.seriesName }}</h2>
        <p class="car-type">{{ item.carType }}</p>
        <p class="promotion-text">{{ item.promotionText }}</p>
<!--        <el-button type="primary" size="large" @click="goToBrandSite">品牌官网</el-button>-->
      </div>
    </div>
  </div>
</template>

<script>
import waves from '@/directive/waves' // Waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import checkPermission from '@/utils/permission' // 权限判断函数
import { fetchBrand, fetchSeries } from '@/api/init'
import { insertPromotion, updatepromotion, updateStore } from '../../../api/store'
import { listPromotion } from '../../../api/promotion'

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
        brandId: undefined,
        seriesId: undefined,
        promotionText: ''
      },
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 5,
        id: undefined,
        brandId: undefined,
        seriesId: undefined,
        status: undefined,
        orderBy: undefined
      },
      carStatusOptions,
      temp: {
        promotionText: '',
        promotionId: '',
      },
      dialogFormVisible: false,
      options: [],
      seriesOptions: [],
      downloadLoading: false,
      rules:{
        promotionText: [
          { required: true, message: '请输入宣传词', trigger: 'blur' },
          { min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getSeriesOpt()
  },
  methods: {
    // 打开设为宣传弹框
    handleSetPromotion(row) {
      // if(row.status !== '1'){
      //   this.$message({
      //     message: '请先将车辆状态设置为在售',
      //     type: 'error'
      //   })
      //   return;
      // }
      // this.promotionData.promotionText = ''; // 清空宣传词输入框
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
      listPromotion(this.listQuery).then(response => {
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
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = {
            id: this.temp.promotionId,
            promotionText: this.temp.promotionText,
          }
          updatepromotion(tempData).then(response => {
            if (response.data.code === 20000) {
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
            this.getList()
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


<style scoped>
.app-container {
  background-color: #e0e0e0; /* 整体背景灰色 */
  padding: 20px;
}

.product-display {
  display: flex;
  width: 100%;
  height: 60vh;
  margin: 20px 0; /* 每个商品之间的垂直间距 */
  background-color: #fff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  border-radius: 8px; /* 圆角 */
  overflow: hidden;
}

.product-image-section {
  width: 50%;
  padding: 20px; /* 给图片和边缘增加一些内边距 */
  background-color: #f5f5f5; /* 背景色 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.product-info-section {
  width: 50%;
  padding: 40px; /* 增加右半部分的内边距 */
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: left;
  margin-left: 10px; /* 左右半部分的间距 */
  color: #333;
  /* 将背景色改为透明，让整体背景色展示出来 */
  background-color: transparent;
}

.brand-name {
  font-size: 48px;
  font-weight: bold;
  color: #333;
  font-family: 'Arial', sans-serif;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.2); /* 轻微的艺术字效果 */
  margin-bottom: 20px;
}

.series-name {
  font-size: 36px;
  color: #333;
  margin-bottom: 10px;
}

.car-type {
  font-size: 28px;
  color: #666;
  margin-bottom: 20px;
}

.promotion-text {
  font-size: 24px;
  color: #666;
  margin-bottom: 40px;
}

.el-button {
  background-color: #ff6347;
  color: #fff;
  border-radius: 20px;
  width: 150px;
}


</style>
