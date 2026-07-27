import { useRef, useEffect, useState } from 'react'
import { Bill } from '../types'
import { X, Printer, CheckCircle, QrCode } from 'lucide-react'
import { getBillQr } from '../services/billService'
import { getSettings } from '../services/settingsService'
import toast from 'react-hot-toast'

interface ReceiptModalProps {
  isOpen: boolean
  onClose: () => void
  bill: Bill | null
}

export default function ReceiptModal({ isOpen, onClose, bill }: ReceiptModalProps) {
  const [qrCode, setQrCode] = useState<string>('')
  const [companyInfo, setCompanyInfo] = useState({
    name: 'Billing & Inventory Store',
    address: '123 Main Street, Commerce City',
    phone: '+91 98765 43210',
    gstin: 'GSTIN123456789'
  })
  const printRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!isOpen || !bill) return
    loadBillQr()
    loadCompanySettings()
  }, [isOpen, bill])

  const loadBillQr = async () => {
    if (!bill) return
    try {
      const qrData = await getBillQr(bill.id)
      setQrCode(qrData)
    } catch {
      // Fallback
    }
  }

  const loadCompanySettings = async () => {
    try {
      const settings = await getSettings()
      if (settings) {
        setCompanyInfo({
          name: settings.companyName || 'Store',
          address: settings.companyAddress || '',
          phone: settings.companyPhone || '',
          gstin: settings.gstNumber || ''
        })
      }
    } catch {}
  }

  if (!isOpen || !bill) return null

  const handlePrint = () => {
    const content = printRef.current?.innerHTML
    if (!content) return

    const printWindow = window.open('', '_blank', 'width=700,height=800')
    if (!printWindow) {
      toast.error('Pop-up blocked. Please allow pop-ups to print receipt.')
      return
    }

    printWindow.document.write(`
      <!DOCTYPE html>
      <html>
        <head>
          <title>Receipt - ${bill.billNumber}</title>
          <style>
            body { font-family: 'Courier New', Courier, monospace; margin: 0; padding: 20px; color: #000; font-size: 13px; }
            .receipt { max-width: 380px; margin: 0 auto; border: 1px solid #ddd; padding: 15px; background: #fff; }
            .header { text-align: center; border-b: 1px dashed #000; pb: 10px; margin-bottom: 10px; }
            .header h2 { margin: 0 0 4px 0; font-size: 18px; }
            .header p { margin: 2px 0; font-size: 11px; color: #444; }
            .meta { font-size: 11px; margin-bottom: 10px; border-b: 1px dashed #000; padding-bottom: 8px; }
            .meta div { display: flex; justify-content: space-between; margin: 2px 0; }
            table { width: 100%; border-collapse: collapse; margin-bottom: 10px; font-size: 12px; }
            th { text-align: left; border-b: 1px solid #000; padding-bottom: 4px; }
            td { padding: 4px 0; }
            .totals { border-t: 1px dashed #000; pt: 8px; font-size: 12px; }
            .totals div { display: flex; justify-content: space-between; margin: 3px 0; }
            .grand-total { font-weight: bold; font-size: 15px; border-t: 1px solid #000; border-b: 1px solid #000; padding: 4px 0; margin-top: 6px; }
            .footer { text-align: center; margin-top: 15px; font-size: 11px; }
            .qr-code { width: 120px; height: 120px; margin: 10px auto 4px auto; display: block; }
          </style>
        </head>
        <body>
          <div class="receipt">
            ${content}
          </div>
          <script>
            window.onload = () => { window.print(); window.close(); }
          </script>
        </body>
      </html>
    `)
    printWindow.document.close()
  }

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4" onClick={onClose}>
      <div
        className="bg-white dark:bg-gray-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4 border border-gray-100 dark:border-gray-700 max-h-[90vh] overflow-y-auto"
        onClick={e => e.stopPropagation()}
      >
        <div className="flex items-center justify-between border-b border-gray-100 dark:border-gray-700 pb-3">
          <div className="flex items-center gap-2 text-green-600 font-bold text-lg">
            <CheckCircle className="w-5 h-5" />
            <span>Bill Generated</span>
          </div>
          <button onClick={onClose} className="p-1 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 rounded-lg">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Printable Area */}
        <div ref={printRef} className="bg-white text-black p-5 rounded-xl border border-gray-200 shadow-inner font-mono text-sm space-y-4">
          <div className="text-center border-b border-dashed border-gray-400 pb-3">
            <h2 className="font-bold text-lg text-black uppercase tracking-wide">{companyInfo.name}</h2>
            {companyInfo.address && <p className="text-xs text-gray-600">{companyInfo.address}</p>}
            {companyInfo.phone && <p className="text-xs text-gray-600">Ph: {companyInfo.phone}</p>}
            {companyInfo.gstin && <p className="text-xs font-semibold text-gray-700">GSTIN: {companyInfo.gstin}</p>}
          </div>

          <div className="text-xs border-b border-dashed border-gray-400 pb-2 space-y-1">
            <div className="flex justify-between">
              <span className="text-gray-600">Bill No:</span>
              <span className="font-bold">{bill.billNumber}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Date:</span>
              <span>{new Date(bill.createdAt).toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Cashier:</span>
              <span>{bill.cashierName}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-600">Payment:</span>
              <span className="font-bold">{bill.paymentMethod}</span>
            </div>
          </div>

          <div>
            <table className="w-full text-xs text-left">
              <thead>
                <tr className="border-b border-black">
                  <th className="py-1">Item</th>
                  <th className="py-1 text-center">Qty</th>
                  <th className="py-1 text-right">Price</th>
                  <th className="py-1 text-right">Total</th>
                </tr>
              </thead>
              <tbody>
                {bill.items?.map((item, idx) => (
                  <tr key={idx} className="border-b border-gray-100">
                    <td className="py-1 pr-1 font-sans">{item.productName}</td>
                    <td className="py-1 text-center">{item.quantity}</td>
                    <td className="py-1 text-right">Rs.{item.unitPrice.toFixed(2)}</td>
                    <td className="py-1 text-right font-semibold">Rs.{item.totalPrice.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="border-t border-dashed border-gray-400 pt-2 text-xs space-y-1">
            <div className="flex justify-between">
              <span>Subtotal:</span>
              <span>Rs.{bill.subtotal.toFixed(2)}</span>
            </div>
            {bill.discountAmount > 0 && (
              <div className="flex justify-between text-green-700">
                <span>Discount:</span>
                <span>-Rs.{bill.discountAmount.toFixed(2)}</span>
              </div>
            )}
            <div className="flex justify-between text-gray-600">
              <span>CGST:</span>
              <span>Rs.{bill.cgstAmount.toFixed(2)}</span>
            </div>
            <div className="flex justify-between text-gray-600">
              <span>SGST:</span>
              <span>Rs.{bill.sgstAmount.toFixed(2)}</span>
            </div>
            <div className="flex justify-between border-t border-b border-black py-1.5 font-bold text-sm text-black my-2">
              <span>GRAND TOTAL:</span>
              <span>Rs.{bill.grandTotal.toFixed(2)}</span>
            </div>
          </div>

          {qrCode && (
            <div className="text-center pt-1 border-t border-dashed border-gray-300">
              <img src={qrCode} alt="Bill QR Code" className="w-28 h-28 mx-auto object-contain" />
              <p className="text-[10px] text-gray-500 mt-1">Scan QR Code to verify bill details</p>
            </div>
          )}

          <div className="text-center text-[11px] text-gray-500 pt-1">
            <p className="font-sans font-medium">Thank you for your business!</p>
          </div>
        </div>

        <div className="pt-2 flex gap-3">
          <button onClick={handlePrint} className="btn-primary flex-1 flex items-center justify-center gap-2 py-2.5">
            <Printer className="w-4 h-4" /> Print Invoice Receipt
          </button>
          <button onClick={onClose} className="btn-secondary flex-1">
            Done
          </button>
        </div>
      </div>
    </div>
  )
}
