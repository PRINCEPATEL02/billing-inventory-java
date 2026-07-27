import api from './api'
import { InventoryAlert, Purchase } from '../types'
const payload = (res: any) => res.data.data
export const getPurchases = (params: Record<string, string | number | undefined> = {}) => api.get('/purchases', { params }).then(payload) as Promise<Purchase[]>
export const createPurchase = (data: Omit<Purchase, 'id' | 'productName' | 'totalCost'>) => api.post('/purchases', data).then(payload) as Promise<Purchase>
export const getInventoryAlerts = () => api.get('/purchases/alerts').then(payload) as Promise<InventoryAlert[]>
