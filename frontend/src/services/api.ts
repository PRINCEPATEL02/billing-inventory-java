import axios from 'axios'
import { useAuthStore } from '../stores/authStore'
import toast from 'react-hot-toast'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    const isLoginRequest = error.config?.url?.includes('/auth/login')

    if (status === 401 && !isLoginRequest) {
      // Token expired or invalid session
      const currentToken = useAuthStore.getState().token
      if (currentToken) {
        toast.error('Session expired. Please login again.')
        useAuthStore.getState().logout()
        window.location.href = '/login'
      }
    } else if (status === 403) {
      // Access denied (e.g. Employee attempting Admin action)
      toast.error('Access Denied: You do not have permission for this action.')
    }

    return Promise.reject(error)
  }
)

export default api