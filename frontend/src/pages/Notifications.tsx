import { useEffect, useMemo, useState } from 'react'
import { Bell, CheckCircle2, PackagePlus, ShieldAlert } from 'lucide-react'
import toast from 'react-hot-toast'
import { getInventoryAlerts, getPurchases } from '../services/purchaseService'
import { InventoryAlert, Purchase } from '../types'

type Notification = { id: string; title: string; message: string; date: string; critical?: boolean; icon: typeof Bell }

export default function Notifications() {
  const [alerts, setAlerts] = useState<InventoryAlert[]>([])
  const [purchases, setPurchases] = useState<Purchase[]>([])
  const [loading, setLoading] = useState(true)

  const load = async () => {
    setLoading(true)
    try {
      const [nextAlerts, nextPurchases] = await Promise.all([getInventoryAlerts(), getPurchases()])
      setAlerts(nextAlerts); setPurchases(nextPurchases)
    } catch { toast.error('Unable to load notifications') } finally { setLoading(false) }
  }
  useEffect(() => { void load() }, [])

  const notifications = useMemo<Notification[]>(() => [
    ...alerts.map((a, index) => ({ id: `alert-${a.productId}-${a.type}-${index}`, title: a.type === 'NEAR_EXPIRY' ? 'Near expiry' : a.type === 'OUT_OF_STOCK' ? 'Out of stock' : 'Low stock', message: `${a.productName}: ${a.message}`, date: a.expiryDate || '', critical: a.severity === 'critical', icon: ShieldAlert })),
    ...purchases.slice(0, 10).map(p => ({ id: `purchase-${p.id}`, title: 'New purchase · completed', message: `${p.productName} · ${p.quantity} units from ${p.supplierName}`, date: p.purchaseDate, icon: CheckCircle2 })),
  ], [alerts, purchases])

  return <div className="space-y-6">
    <div className="flex items-center justify-between"><div><h1 className="text-2xl font-bold">Notification center</h1><p className="text-sm text-gray-500">Inventory alerts and recent purchase activity in one place.</p></div><button className="btn-secondary" onClick={load}>Refresh</button></div>
    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4"><div className="card"><p className="text-sm text-gray-500">Active alerts</p><p className="text-2xl font-bold text-red-600">{alerts.length}</p></div><div className="card"><p className="text-sm text-gray-500">Critical alerts</p><p className="text-2xl font-bold text-red-600">{alerts.filter(a => a.severity === 'critical').length}</p></div><div className="card"><p className="text-sm text-gray-500">Recent purchases</p><p className="text-2xl font-bold">{purchases.length}</p></div></div>
    <div className="card divide-y divide-gray-100 dark:divide-gray-700">{loading ? <p className="py-10 text-center text-gray-500">Loading notifications…</p> : notifications.length ? notifications.map(n => { const Icon = n.icon; return <div key={n.id} className="flex items-start gap-3 py-4"><Icon className={`w-5 h-5 mt-0.5 ${n.critical ? 'text-red-600' : 'text-blue-600'}`} /><div className="flex-1"><p className="font-medium">{n.title}</p><p className="text-sm text-gray-500">{n.message}</p></div><span className="text-xs text-gray-500">{n.date}</span></div> }) : <div className="py-10 text-center text-gray-500"><PackagePlus className="w-8 h-8 mx-auto mb-2 text-green-600" />You’re all caught up.</div>}</div>
  </div>
}
