import api from './api'
export const login = async (username: string, password: string) => {
  const { data } = await api.post('/auth/login', { username, password })
  return data.data
}
export const register = async (userData: any) => {
  const { data } = await api.post('/auth/register', userData)
  return data
}