import api from './api'
export const createBill = (data: any) => api.post('/bills', data).then(r => r.data.data)
export const getBills = () => api.get('/bills').then(r => r.data.data)
export const getBill = (id: number) => api.get(`/bills/${id}`).then(r => r.data.data)
export const searchBills = (q: string) => api.get(`/bills/search?q=${q}`).then(r => r.data.data)
export const getBillQr = (id: number) => api.get(`/bills/${id}/qr`).then(r => r.data.data)
export const getUpiPaymentQr = (amount: number, note?: string) => api.get(`/bills/upi-qr?amount=${amount}&note=${encodeURIComponent(note || 'POS_BILL')}`).then(r => r.data.data)