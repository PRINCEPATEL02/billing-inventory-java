import api from './api'
export const getAiAnalytics = () => api.get('/analytics/ai').then(r => r.data.data)
