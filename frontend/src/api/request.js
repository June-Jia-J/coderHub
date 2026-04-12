import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const baseURL = import.meta.env.VITE_API_BASE || ''

export const request = axios.create({
  baseURL,
  timeout: 60000,
  headers: { 'Content-Type': 'application/json' }
})

request.interceptors.request.use((config) => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

request.interceptors.response.use(
  (res) => {
    const d = res.data
    if (d && d.code !== undefined && d.code !== 200) {
      ElMessage.error(d.message || '请求失败')
      return Promise.reject(new Error(d.message))
    }
    return res.data
  },
  (err) => {
    const status = err.response?.status
    const data = err.response?.data
    const isRefused = err.code === 'ERR_NETWORK' || err.message?.includes('Network Error')
    const serverMsg = data?.message
    const msg = status === 401
      ? (serverMsg || '请先登录')
      : isRefused
        ? '无法连接后端服务，请确认已执行 docker compose up 且后端已启动'
        : serverMsg || err.message || '网络错误'
    ElMessage.error(msg)
    if (status === 401) {
      const userStore = useUserStore()
      userStore.logout()
    }
    return Promise.reject(err)
  }
)
