import { useEffect, useState } from 'react'
import { Download, FileText } from 'lucide-react'
import { ReportSummary } from '../types'
import { exportReport, getReport } from '../services/reportService'
import toast from 'react-hot-toast'

const types = ['daily', 'weekly', 'monthly', 'yearly', 'gst', 'sales', 'inventory', 'purchase', 'profit', 'loss']
export default function Reports() {
  const [type, setType] = useState('monthly'); const [from, setFrom] = useState(''); const [to, setTo] = useState(''); const [report, setReport] = useState<ReportSummary | null>(null)
  const load = async () => { try { setReport(await getReport(type, { from: from || undefined, to: to || undefined })) } catch { toast.error('Unable to generate report') } }
  useEffect(() => { load() }, [type])
  const money = (value: number) => `Rs.${Number(value || 0).toLocaleString(undefined, { minimumFractionDigits: 2 })}`
  const fields: [string, string | number][] = report ? [['Sales', money(report.sales)], ['Purchase cost', money(report.purchaseCost)], ['Profit / loss', money(report.profit)], ['Inventory value', money(report.inventoryValue)], ['GST on purchases', money(report.gstOnPurchases)], ['Sales invoices', report.salesCount], ['Purchases', report.purchaseCount]] : []
  return <div className="space-y-6"><div><h1 className="text-2xl font-bold">Reports</h1><p className="text-sm text-gray-500">Generate performance, GST, purchase and inventory summaries.</p></div><div className="card flex flex-wrap gap-3 items-end"><div><label className="text-sm block mb-1">Report</label><select className="input-field" value={type} onChange={e => setType(e.target.value)}>{types.map(t => <option key={t} value={t}>{t[0].toUpperCase() + t.slice(1)} report</option>)}</select></div><div><label className="text-sm block mb-1">From</label><input type="date" className="input-field" value={from} onChange={e => setFrom(e.target.value)} /></div><div><label className="text-sm block mb-1">To</label><input type="date" className="input-field" value={to} onChange={e => setTo(e.target.value)} /></div><button className="btn-primary" onClick={load}>Generate</button><button className="btn-secondary" onClick={() => exportReport(type, 'xlsx', { from: from || undefined, to: to || undefined })}><Download className="inline w-4 h-4 mr-1" />Excel</button><button className="btn-secondary" onClick={() => exportReport(type, 'pdf', { from: from || undefined, to: to || undefined })}><FileText className="inline w-4 h-4 mr-1" />PDF</button></div>{report && <><div className="text-sm text-gray-500">{report.reportType} · {report.from} to {report.to}</div><div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">{fields.map(([label, value]) => <div className="card" key={label}><p className="text-sm text-gray-500">{label}</p><p className="text-xl font-bold mt-1">{value}</p></div>)}</div></>}</div>
}
