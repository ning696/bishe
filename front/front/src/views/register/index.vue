<template>
  <div class="background">
    <div class="app-container">
      <el-header>
        <h3 class="title">用户注册</h3>
        <hr class="divider">
      </el-header>
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px" class="demo-ruleForm">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="ruleForm.name" placeholder="请输入您的姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="ruleForm.phone" placeholder="请输入您的联系电话" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="ruleForm.idCard" placeholder="请输入您的身份证号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button  plain @click="submitForm('ruleForm')">添加</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
          <el-button class="login-btn" @click="$router.push('/login')" >返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { validateIdCard } from '../../utils/validate'
import { addCustomer } from '../../api/customer'

export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于 6 位数字'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        name: '',
        phone: '',
        idCard: '',
        password: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入客户姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入客户联系电话', trigger: 'blur' }
        ],
        idCard: [
          { required: true, validator: validateIdCard, trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addCustomer(this.ruleForm).then(response => {
            if (response.data.code === 20000) {
              this.$notify({
                title: '成功',
                message: '添加成功',
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
            this.resetForm(formName)
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>
.background {
  background-image: url('../../assets/bg.jpg'); /* 背景图片路径 */
  background-size: cover; /* 图片覆盖整个容器 */
  background-position: center; /* 图片居中 */
  background-repeat: no-repeat; /* 图片不重复 */
  height: 100vh; /* 占满整个视窗高度 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.app-container {
  background-color: rgba(255, 255, 255, 0.9); /* 半透明白色背景 */
  padding: 30px;
  max-width: 500px;
  width: 90%;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  font-family: 'Microsoft YaHei', sans-serif;
  font-size: 24px;
  color: #333;
  text-align: center;
  margin: 0;
}

.divider {
  border: none;
  height: 1px;
  background-color: #dcdfe6;
  margin: 10px 0 20px;
}

.demo-ruleForm {
  padding: 20px;
}
.login-btn{
  border-color: transparent;
  color: #000000;
  background: 0 0;
  padding-left: 0;
  padding-right: 0;
}
.el-form-item {
  margin-bottom: 20px;
}

.el-button--primary {
  background-color: #409eff;
  border-color: #409eff;
}

.el-button--primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}
</style>
