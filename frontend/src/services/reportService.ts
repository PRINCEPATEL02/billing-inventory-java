import api from './api'
import { ReportSummary } from '../types'
export const getReport = (type: string, params: Record<string, string | undefined> = {}) => api.get(`/reports/${type}`, { params }).then(r => r.data.data) as Promise<ReportSummary>
export const exportReport = async (type: string, format: 'pdf' | 'xlsx', params: Record<string, string | undefined> = {}) => {
  const response = await api.get(`/reports/${type}/export`, { params: { ...params, format }, responseType: 'blob' })
  const url = URL.createObjectURL(response.data); const link = document.createElement('a'); link.href = url; link.download = `${type}-report.${format}`; link.click(); URL.revokeObjectURL(url)
}
