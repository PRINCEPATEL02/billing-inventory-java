import { useState, useRef, useEffect } from 'react'
import { X, Download, Printer, QrCode, Check, Copy, RefreshCw } from 'lucide-react'
import toast from 'react-hot-toast'
import { generateCustomQrCode } from '../services/qrCodeService'

interface QRCodeModalProps {
  isOpen: boolean
  onClose: () => void
  title: string
  subtitle?: string
  qrCodeUrl: string
  barcodeText?: string
  productDetails?: {
    id?: number
    name: string
    price: number
    category?: string
    gstPercentage?: number
    hsnCode?: string
  }
}

export default function QRCodeModal({
  isOpen,
  onClose,
  title,
  subtitle,
  qrCodeUrl,
  barcodeText,
  productDetails
}: QRCodeModalProps) {
  const [copied, setCopied] = useState(false)
  const [displayQr, setDisplayQr] = useState<string>('')
  const [loading, setLoading] = useState<boolean>(false)
  const printRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    if (!isOpen) return

    const isValid = qrCodeUrl && (qrCodeUrl.startsWith('data:image/') || qrCodeUrl.startsWith('http://') || qrCodeUrl.startsWith('https://'))

    if (isValid) {
      setDisplayQr(qrCodeUrl)
    } else {
      fetchFreshQrCode()
    }
  }, [isOpen, qrCodeUrl, barcodeText])

  const fetchFreshQrCode = async () => {
    setLoading(true)
    try {
      const textToEncode = barcodeText || productDetails?.name || title || 'QR-CODE'
      const generated = await generateCustomQrCode(textToEncode, 250, 250)
      if (generated && generated.qrCode) {
        setDisplayQr(generated.qrCode)
      } else if (typeof generated === 'string' && generated.startsWith('data:image/')) {
        setDisplayQr(generated)
      }
    } catch {
      // quiet fallback
    } finally {
      setLoading(false)
    }
  }

  if (!isOpen) return null

  const handleDownload = () => {
    try {
      const link = document.createElement('a')
      link.href = displayQr
      const safeName = (title || 'qrcode').toLowerCase().replace(/[^a-z0-9]/g, '_')
      link.download = `${safeName}_qr.png`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      toast.success('QR Code image downloaded!')
    } catch {
      toast.error('Download failed')
    }
  }

  const handleCopyText = () => {
    if (barcodeText) {
      navigator.clipboard.writeText(barcodeText)
      setCopied(true)
      toast.success('Barcode copied to clipboard!')
      setTimeout(() => setCopied(false), 2000)
    }
  }

  const handlePrintLabel = () => {
    const content = printRef.current?.innerHTML
    if (!content) return

    const printWindow = window.open('', '_blank', 'width=600,height=600')
    if (!printWindow) {
      toast.error('Pop-up blocked. Please allow pop-ups to print.')
      return
    }

    printWindow.document.write(`
      <!DOCTYPE html>
      <html>
        <head>
          <title>Print Label - ${title}</title>
          <style>
            body { font-family: system-ui, sans-serif; margin: 0; padding: 20px; display: flex; justify-content: center; align-items: center; background: #fff; }
            .label-card { border: 2px dashed #333; padding: 20px; border-radius: 12px; text-align: center; max-width: 300px; width: 100%; box-sizing: border-box; }
            .label-title { font-size: 18px; font-weight: bold; margin-bottom: 4px; color: #111; }
            .label-sub { font-size: 12px; color: #666; margin-bottom: 12px; }
            .label-price { font-size: 22px; font-weight: 800; color: #0284c7; margin: 10px 0; }
            .label-qr { width: 180px; height: 180px; margin: 0 auto; }
            .label-barcode { font-family: monospace; font-size: 14px; letter-spacing: 2px; font-weight: bold; background: #f1f5f9; padding: 4px 8px; border-radius: 4px; display: inline-block; margin-top: 8px; }
          </style>
        </head>
        <body>
          <div class="label-card">
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
        className="bg-white dark:bg-gray-800 rounded-2xl max-w-sm w-full p-6 shadow-2xl space-y-5 border border-gray-100 dark:border-gray-700 animate-in fade-in zoom-in duration-200"
        onClick={e => e.stopPropagation()}
      >
        <div className="flex items-center justify-between border-b border-gray-100 dark:border-gray-700 pb-3">
          <div className="flex items-center gap-2 text-gray-900 dark:text-white font-bold text-lg">
            <QrCode className="w-5 h-5 text-primary-600" />
            <span>{title}</span>
          </div>
          <button onClick={onClose} className="p-1 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 rounded-lg">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Printable Label View */}
        <div ref={printRef} className="flex flex-col items-center justify-center space-y-3 p-4 bg-gray-50 dark:bg-gray-700/50 rounded-xl border border-gray-100 dark:border-gray-700">
          {productDetails ? (
            <>
              <h3 className="label-title font-bold text-gray-900 dark:text-white text-center text-lg">{productDetails.name}</h3>
              {subtitle && <p className="label-sub text-xs text-gray-500 text-center">{subtitle}</p>}
              <p className="label-price text-2xl font-extrabold text-primary-600">Rs.{productDetails.price.toFixed(2)}</p>
            </>
          ) : (
            subtitle && <p className="label-sub text-sm font-medium text-gray-600 dark:text-gray-300 text-center">{subtitle}</p>
          )}

          <div className="bg-white p-3 rounded-xl shadow-md border border-gray-100 flex items-center justify-center min-h-[200px] min-w-[200px] relative">
            {loading ? (
              <div className="flex flex-col items-center gap-2 text-gray-400 text-xs py-8">
                <RefreshCw className="w-6 h-6 animate-spin text-primary-600" />
                <span>Generating QR Code...</span>
              </div>
            ) : displayQr ? (
              <img
                src={displayQr}
                alt="QR Code"
                className="label-qr w-48 h-48 object-contain"
                onError={() => fetchFreshQrCode()}
              />
            ) : (
              <div className="flex flex-col items-center gap-2 text-gray-400 text-xs py-8">
                <RefreshCw className="w-6 h-6 animate-spin text-primary-600" />
                <span>Loading QR Code...</span>
              </div>
            )}
          </div>

          {barcodeText && (
            <div className="flex items-center gap-2 mt-1">
              <span className="label-barcode font-mono text-sm font-semibold tracking-wider text-gray-800 dark:text-gray-200 bg-white dark:bg-gray-800 px-3 py-1 rounded-md border border-gray-200 dark:border-gray-600">
                {barcodeText}
              </span>
              <button onClick={handleCopyText} title="Copy code" className="p-1 hover:bg-gray-200 dark:hover:bg-gray-600 rounded">
                {copied ? <Check className="w-4 h-4 text-green-600" /> : <Copy className="w-4 h-4 text-gray-500" />}
              </button>
            </div>
          )}
        </div>

        <div className="grid grid-cols-2 gap-3 pt-2">
          <button onClick={handleDownload} disabled={!displayQr || loading} className="btn-primary flex items-center justify-center gap-2 py-2.5 text-sm disabled:opacity-50">
            <Download className="w-4 h-4" /> Download PNG
          </button>
          <button onClick={handlePrintLabel} disabled={!displayQr || loading} className="btn-secondary flex items-center justify-center gap-2 py-2.5 text-sm disabled:opacity-50">
            <Printer className="w-4 h-4" /> Print Label
          </button>
        </div>
      </div>
    </div>
  )
}
