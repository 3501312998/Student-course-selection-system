// Axios HTTP 请求封装
// 配置请求拦截器（自动携带 JWT Token）和响应拦截器（统一错误处理）
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getToken, clearAuth } from '@/utils/storage'

// 创建 Axios 实例，配置基础路径和超时时间
const request = axios.create({
  baseURL: '/api',  // 后端接口前缀，通过 Vite proxy 转发
  timeout: 15000,  // 请求超时 15 秒
})

// 请求拦截器
// 请求拦截器：自动在请求头中携带 JWT Token
request.interceptors.request.use(
  (config) => {
    const token = getToken()  // 从 localStorage 获取 Token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`  // 设置 Authorization 请求头
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
// 响应拦截器：统一处理响应数据和 HTTP 错误
request.interceptors.response.use(
  (response) => {
    const res = response.data  // 取出响应体
    if (res.code !== 200) {  // 业务状态码非 200 视为失败
      ElMessage.error(res.message || '请求失败')  // 提示错误信息
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  (error) => {
    if (error.response) {  // 有 HTTP 响应（服务器返回了错误）
      const { status } = error.response  // HTTP 状态码
      if (status === 401) {  // 未授权，清除登录信息并跳转登录页
        clearAuth()  // 清除本地存储的认证信息
        router.push('/login')  // 跳转到登录页
        ElMessage.error('登录已过期，请重新登录')
      } else if (status === 403) {  // 权限不足
        ElMessage.error('无权限访问')
      } else {  // 网络错误（无响应）  // 其他服务器错误
        ElMessage.error(error.response.data?.message || '服务器错误')
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request
