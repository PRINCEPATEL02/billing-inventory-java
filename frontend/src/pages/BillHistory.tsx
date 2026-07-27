import { useEffect, useState } from 'react'
import { getBills, searchBills, getBill } from '../services/billService'
import { Bill } from '../types'
import { Search, Eye, Printer, QrCode } from 'lucide-react'
import toast from 'react-hot-toast'
import ReceiptModal from '../components/ReceiptModal'

export default function BillHistory() {
  const [bills, setBills] = useState<Bill[]>([])
  const [searchQuery, setSearchQuery] = useState('')
  const [loading, setLoading] = useState(true)
  const [selectedBill, setSelectedBill] = useState<Bill | null>(null)
  const [receiptModalOpen, setReceiptModalOpen] = useState(false)

  useEffect(() => {
    loadBills()
  }, [])

  const loadBills = async () => {
    try {
      const res = await getBills()
      setBills(res)
    } catch {
      toast.error('Failed to load bills')
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = async () => {
    if (!searchQuery.trim()) {
      loadBills()
      return
    }
    try {
      const res = await searchBills(searchQuery)
      setBills(res)
    } catch {
      toast.error('Search failed')
    }
  }

  const handleViewReceipt = async (billItem: Bill) => {
    try {
      const fullBill = await getBill(billItem.id)
      setSelectedBill(fullBill)
    } catch {
      setSelectedBill(billItem)
    }
    setReceiptModalOpen(true)
  }

  const formatDate = (dateStr: string) => {
    if (!dateStr) return 'N/A'
    return new Date(dateStr).toLocaleString('en-IN', {
      day: '2-digit', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    })
  }

  if (loading) return <div className="text-center py-20 text-gray-500">Loading bills history...</div>

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">Bill History & Receipts</h1>
          <p className="text-sm text-gray-500">View past sales, verify invoice QR codes, and re-print receipts.</p>
        </div>
      </div>

      <div className="card">
        <div className="flex gap-2 mb-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
              placeholder="Search by bill number or cashier name..."
              className="input-field pl-10"
            />
          </div>
          <button onClick={handleSearch} className="btn-primary">Search</button>
          <button onClick={() => { setSearchQuery(''); loadBills(); }} className="btn-secondary">Reset</button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-700 text-sm font-medium text-gray-500">
                <th className="text-left py-3 px-4">Bill #</th>
                <th className="text-left py-3 px-4">Date & Time</th>
                <th className="text-left py-3 px-4">Cashier</th>
                <th className="text-left py-3 px-4">Items</th>
                <th className="text-left py-3 px-4">Payment</th>
                <th className="text-right py-3 px-4">Grand Total</th>
                <th className="text-center py-3 px-4">Actions</th>
              </tr>
            </thead>
            <tbody>
              {bills.map(bill => (
                <tr key={bill.id} className="border-b border-gray-100 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition">
                  <td className="py-3 px-4 font-bold text-gray-900 dark:text-white font-mono">{bill.billNumber}</td>
                  <td className="py-3 px-4 text-sm text-gray-600 dark:text-gray-400">{formatDate(bill.createdAt)}</td>
                  <td className="py-3 px-4 text-sm">{bill.cashierName}</td>
                  <td className="py-3 px-4 text-sm">{bill.totalItems || bill.items?.length || 0}</td>
                  <td className="py-3 px-4">
                    <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${
                      bill.paymentMethod === 'CASH' ? 'bg-green-100 text-green-700 dark:bg-green-950/60 dark:text-green-300' :
                      bill.paymentMethod === 'CARD' ? 'bg-blue-100 text-blue-700 dark:bg-blue-950/60 dark:text-blue-300' :
                      'bg-purple-100 text-purple-700 dark:bg-purple-950/60 dark:text-purple-300'
                    }`}>
                      {bill.paymentMethod}
                    </span>
                  </td>
                  <td className="py-3 px-4 text-right font-extrabold text-gray-900 dark:text-white">
                    Rs.{bill.grandTotal ? bill.grandTotal.toFixed(2) : '0.00'}
                  </td>
                  <td className="py-3 px-4 text-center">
                    <div className="flex items-center justify-center gap-1">
                      <button
                        onClick={() => handleViewReceipt(bill)}
                        className="btn-secondary py-1 px-2.5 text-xs flex items-center gap-1"
                        title="View & Print Receipt with QR Code"
                      >
                        <Eye className="w-3.5 h-3.5 text-primary-600" />
                        <span>Receipt</span>
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {bills.length === 0 && (
            <p className="text-center py-12 text-gray-500">No bills found in history</p>
          )}
        </div>
      </div>

      {/* Printable Receipt Modal with QR */}
      <ReceiptModal
        isOpen={receiptModalOpen}
        onClose={() => { setReceiptModalOpen(false); setSelectedBill(null); }}
        bill={selectedBill}
      />
    </div>
  )
}