<script setup>
import { ref } from "vue";
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '@/apis/login'
import { setToken } from "@/utils/auth"
import router from "@/router"


const loginForm = ref({
  name: '',
  password: ''
})

const loginRules = {
  userAccount: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
}

const formRef = ref()
const loginSubmit = async () => {
  await formRef.value.validate() //提交之前的预校验
  console.log('login submit!')
  try {
    const { userAccount, password } = loginForm.value
    const loginRes = await login(userAccount, password)
    setToken(loginRes.data.access_token)
    router.push('/')
  } catch (err) {
    console.error('Login failed:', err)
    alert('登录失败');
  }
}
</script>

<template>
  <div class="login">
    <el-form ref="formRef" :model="loginForm" :rules="loginRules" label-position="top" class="login-form">
      <h3 class="title">欢迎使用</h3>
      <el-form-item label="用户账号" prop="userAccount">
        <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="loginForm.userAccount"></el-input>
      </el-form-item>
      <el-form-item label="用户密码" prop="password">
        <el-input v-model="loginForm.password" name="password" :prefix-icon="Lock" type="password"
          placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-button type="primary" class="login-button" style="width: 100%;" @click="loginSubmit(formRef)">登录</el-button>
    </el-form>
    <div class="el-login-footer">
      <span>Copyright © 2024 Liu All Rights Reserved.</span>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 790px;
  background-image: url('@/assets/login_bg.png');
  background-size: cover;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  font-size: 34px;
  font-weight: 600;
  color: #222222;
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
}

.login-button {
  background: #32C5FF;
  border-radius: 23px;
  font-size: 18px;
  font-weight: 500;
  color: #FFFFFF;
  letter-spacing: 4px;
}
</style>