<template>
  <div class="app-container">
    <el-row :gutter="24">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper" style="padding-bottom:5px;">
          <el-header>
            <h3>添加库存</h3>
            <hr>
          </el-header>
          <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px" style="width: 400px; margin-left:40px;">
            <el-form-item label="品牌 & 车系" prop="seriesId">
              <el-cascader :options="options" v-model="ruleForm.seriesId" placeholder="品牌/车系" style="width: 220px"/>
            </el-form-item>
            <el-form-item label="型号" prop="type">
              <el-input v-model="ruleForm.type"/>
            </el-form-item>
            <el-form-item label="颜色" prop="color">
              <el-input v-model="ruleForm.color"/>
            </el-form-item>
            <el-form-item label="进价" prop="price">
              <el-input v-model.number="ruleForm.price"/>
            </el-form-item>
            <el-form-item label="售价" prop="salePrice">
              <el-input v-model.number="ruleForm.salePrice"/>
            </el-form-item>
            <el-form-item label="数量" prop="repertory">
              <el-input-number v-model="ruleForm.repertory" :min="0"/>
            </el-form-item>
            <el-form-item label="上传图片">
              <el-upload
                :show-file-list="true"
                :on-change="handleFileChange"
                :auto-upload="false"
                class="avatar-uploader"
                action=""
              >
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"/>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitStore">添加</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <el-header>
            <h3>添加品牌</h3>
            <hr>
          </el-header>
          <el-form ref="addBrand" :model="addBrand" :inline="true" label-width="90px" style="width: 420px; margin-left:50px;">
            <el-form-item label="品牌名称" prop="brand" required>
              <el-input v-model="addBrand.brand"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitBrand">添加</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="chart-wrapper">
          <el-header>
            <h3>删除品牌</h3>
            <hr>
          </el-header>
          <el-form ref="delBrand" :model="delBrand" :inline="true" label-width="90px" style="width: 420px; margin-left:50px;">
            <el-form-item label="品牌名称" required>
              <el-select v-model="delBrand.brandId" class="filter-item" placeholder="请选择">
                <el-option v-for="item in brandOptions" :key="item.brandId" :label="item.brandName" :value="item.brandId"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="danger" @click="submitDelBrand">删除</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="chart-wrapper">
          <el-header>
            <h3>添加车系</h3>
            <hr>
          </el-header>
          <el-form ref="addSeries" :model="addSeries" :inline="true" label-width="90px" style="width: 420px; margin-left:50px;">
            <el-form-item label="车辆品牌" required>
              <el-select v-model="addSeries.brandId" class="filter-item" placeholder="请选择">
                <el-option v-for="item in brandOptions" :key="item.brandId" :label="item.brandName" :value="item.brandId"/>
              </el-select>
            </el-form-item>
            <br>
            <el-form-item label="车系名称" required>
              <el-input v-model="addSeries.series" style="width: 220px"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitSeries">添加</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="chart-wrapper">
          <el-header>
            <h3>删除车系</h3>
            <hr>
          </el-header>
          <el-form ref="delSeries" :model="delSeries" :inline="true" label-width="100px" style="width: 420px; margin-left:50px;">
            <el-form-item label="品牌 & 车系" required>
              <el-cascader :options="options" v-model="delSeries.series" placeholder="品牌/车系" style="width: 220px"/>
            </el-form-item>
            <el-form-item>
              <el-button type="danger" @click="submitDelSeries">删除</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { addBrand, delBrand, addSeries, delSeries, addStore } from '../../api/store'
import { fetchBrand, fetchSeries } from '@/api/init'

export default {
  data() {
    return {
      formData: {
        name: '',
        image: null
      },
      imageUrl: null,
      selectedFiles: [],
      ruleForm: {
        seriesId: [],
        type: '答复',
        color: '撒发',
        price: 123,
        salePrice: 654,
        repertory: undefined
      },
      addBrand: {
        brand: ''
      },
      brandOptions: [],
      seriesOptions: [],
      delBrand: {
        brandId: undefined
      },
      addSeries: {
        brandId: undefined,
        series: ''
      },
      delSeries: {
        series: []
      },
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
      options: []
    }
  },
  created() {
    this.getSeriesOpt()
    this.getBrandOpt()
  },
  methods: {
    handleFileChange(file) {
      // file 是当前变化的文件对象
      // fileList 是所有已选中的文件列表

      // 添加文件到 selectedFiles
      this.selectedFiles.push(file.raw)
      this.previewImage(file.raw)
    },
    previewImage(file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.imageUrl = e.target.result
      }
      reader.readAsDataURL(file)
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
    submitStore() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.selectedFiles.length === 0) {
            alert('请选择文件后再提交！')
            return
          }
          this.imageUrl = this.selectedFiles[0].result
          const formData = new FormData()
          for (let i = 0; i < this.selectedFiles.length; i++) {
            formData.append('files', this.selectedFiles[i])
          }
          const tempData = Object.assign({}, this.ruleForm)
          tempData.seriesId = this.ruleForm.seriesId[1]
          JSON.stringify(tempData)
          formData.append('car', JSON.stringify(tempData))
          addStore(formData).then(response => {
            this.message(response)
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    submitBrand() {
      this.$refs['addBrand'].validate((valid) => {
        if (valid) {
          addBrand(this.addBrand.brand).then(response => {
            this.message(response)
            this.getBrandOpt()
            this.getSeriesOpt()
          })
        } else {
          return false
        }
      })
    },
    submitDelBrand() {
      this.$refs['delBrand'].validate((valid) => {
        if (valid) {
          delBrand(this.delBrand.brandId).then(response => {
            this.message(response)
            this.getBrandOpt()
            this.getSeriesOpt()
          })
        } else {
          return false
        }
      })
    },
    submitSeries() {
      this.$refs['addSeries'].validate((valid) => {
        if (valid) {
          addSeries(this.addSeries.brandId, this.addSeries.series).then(response => {
            this.message(response)
            this.getSeriesOpt()
          })
        } else {
          return false
        }
      })
    },
    submitDelSeries() {
      if (this.delSeries.series[1] != null) {
        delSeries(this.delSeries.series[1]).then(response => {
          this.message(response)
          this.getSeriesOpt()
        })
      } else {
        console.log('error del')
        return false
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    message(response) {
      if (response.data.code === 20000) {
        this.$notify({
          title: '成功',
          // message: '删除成功',
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
    }
    // handleAvatarSuccess(res, file) {
    //   this.imageUrl = URL.createObjectURL(file.raw)
    // },
    // beforeAvatarUpload(file) {
    //   const isJPG = file.type === 'image/jpeg'
    //   const isLt2M = file.size / 1024 / 1024 < 2
    //
    //   if (!isJPG) {
    //     this.$message.error('上传头像图片只能是 JPG 格式!')
    //   }
    //   if (!isLt2M) {
    //     this.$message.error('上传头像图片大小不能超过 2MB!')
    //   }
    //   return isJPG && isLt2M
    // }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
hr {
  border: 0.5px solid #dcdfe6;
}
h3 {
  font-family: Microsoft YaHei;
}
.app-container {
  padding: 24px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 8px 8px 0;
    margin-bottom: 16px;
  }
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
