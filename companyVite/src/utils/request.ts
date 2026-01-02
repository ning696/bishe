import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from './cookie'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: '/dev-api', // 通过 vite 代理转发
  timeout: 10000, // 请求超时时间
})

// 设置默认请求头
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

// 请求拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 在请求发送之前做些什么
    const token = getToken()
    if (token && config.headers) {
      // 让每个请求携带 token
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 对响应数据做点什么
    const res = response.data
    const code = res.code
    const msg = res.msg || '请求失败'

    // 未授权，需要重新登录
    if (code === 3001) {
      ElMessage.error(msg)
      console.error('test006')

      removeToken()
      // 跳转到登录页
      console.warn('[request] 未授权(code=3001)，即将重定向到 /enterprise/login', {
        url: response.config.url,
        method: response.config.method
      })
      window.location.href = '/enterprise/login'
      return Promise.reject(new Error(msg))
    }

    // 操作失败
    if (code !== 1000) {
      ElMessage.error(msg)
      return Promise.reject(new Error(msg))
    }

    // 操作成功
    return Promise.resolve(res)
  },
  (error) => {
    // 对响应错误做点什么
    console.error('Response error:', error)
    
    let message = '请求失败'
    if (error.response) {
      // 服务器返回了错误状态码
      const status = error.response.status
      switch (status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          console.error('test004')
          removeToken()
          // 跳转到登录页
          console.warn('[request] 未授权(401)，即将重定向到 /enterprise/login', {
            url: error.config?.url,
            method: error.config?.method
          })
          window.location.href = '/enterprise/login'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用'
          break
        case 504:
          message = '网关超时'
          break
        default:
          message = `请求失败，状态码：${status}`
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      message = '网络连接失败，请检查网络'
    } else {
      // 发送请求时出了点问题
      message = error.message || '请求失败'
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service

