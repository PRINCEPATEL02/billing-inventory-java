import { useState, useEffect, useRef, useCallback } from 'react'
import { searchProducts, getByBarcode } from '../services/productService'
import { createBill, getUpiPaymentQr } from '../services/billService'
import { Product, Bill } from '../types'
import { Search, Trash2, Plus, Minus, Printer, ShoppingCart, CreditCard, Banknote, Smartphone, Camera, QrCode, Maximize2, Sparkles, X } from 'lucide-react'
import toast from 'react-hot-toast'
import CameraScannerModal from '../components/CameraScannerModal'
import ReceiptModal from '../components/ReceiptModal'

interface CartItem extends Product {
  cartQuantity: number
}

export default function Billing() {
  const [searchQuery, setSearchQuery] = useState('')
  const [searchResults, setSearchResults] = useState<Product[]>([])
  const [cart, setCart] = useState<CartItem[]>([])
  const [discount, setDiscount] = useState(0)
  const [paymentMethod, setPaymentMethod] = useState<'CASH' | 'CARD' | 'UPI'>('CASH')
  const [showResults, setShowResults] = useState(false)
  const [processing, setProcessing] = useState(false)

  // Scanner & Receipt Modal States
  const [scannerOpen, setScannerOpen] = useState(false)
  const [createdBill, setCreatedBill] = useState<Bill | null>(null)
  const [receiptModalOpen, setReceiptModalOpen] = useState(false)

  // UPI Payment QR State
  const [upiQrCode, setUpiQrCode] = useState<string>('')
  const [showLargeUpiModal, setShowLargeUpiModal] = useState(false)
  const [upiLoading, setUpiLoading] = useState(false)

  const searchRef = useRef<HTMLDivElement>(null)

  // Search Debouncing
  useEffect(() => {
    if (searchQuery.trim().length < 2) {
      setSearchResults([])
      setShowResults(false)
      return
    }

    const timer = setTimeout(async () => {
      try {
        const res = await searchProducts(searchQuery)
        setSearchResults(res)
        setShowResults(true)
      } catch {
        // quiet fallback
      }
    }, 250)

    return () => clearTimeout(timer)
  }, [searchQuery])

  // Cart calculations
  const subtotal = cart.reduce((sum, item) => sum + item.sellingPrice * item.cartQuantity, 0)
  const gstAmount = cart.reduce((sum, item) => {
    const itemTotal = item.sellingPrice * item.cartQuantity
    return sum + (itemTotal * item.gstPercentage / 100)
  }, 0)
  const cgst = gstAmount / 2
  const sgst = cgst
  const grandTotal = Math.max(0, subtotal - discount + gstAmount)

  // Generate UPI payment QR when amount or payment method changes
  useEffect(() => {
    if (paymentMethod === 'UPI' && grandTotal > 0) {
      loadUpiQr(grandTotal)
    } else {
      setUpiQrCode('')
    }
  }, [paymentMethod, grandTotal])

  const loadUpiQr = async (amount: number) => {
    setUpiLoading(true)
    try {
      const qrBase64 = await getUpiPaymentQr(amount, `INV_${Date.now().toString().slice(-6)}`)
      setUpiQrCode(qrBase64)
    } catch {
      toast.error('Could not generate UPI QR Code')
    } finally {
      setUpiLoading(false)
    }
  }

  const handleBarcodeScan = async (code: string) => {
    if (!code || !code.trim()) return
    const rawCode = code.trim()
    const cleanCode = rawCode.replace(/^QR-/, '')

    try {
      let product: Product | null = null

      try {
        product = await getByBarcode(rawCode)
      } catch {
        if (cleanCode !== rawCode) {
          try {
            product = await getByBarcode(cleanCode)
          } catch {}
        }
      }

      if (!product) {
        const searchRes = await searchProducts(cleanCode)
        if (searchRes && searchRes.length > 0) {
          product = searchRes.find((p: Product) => p.barcode === cleanCode || p.barcode === rawCode) || searchRes[0]
        }
      }

      if (product) {
        addToCart(product)
        setSearchQuery('')
        setShowResults(false)
        toast.success(`Added ${product.name} to cart!`)
      } else {
        toast.error(`No product found for barcode: "${rawCode}"`)
      }
    } catch {
      toast.error(`Scan lookup failed for code: "${rawCode}"`)
    }
  }

  const addToCart = (product: Product) => {
    if (product.quantity <= 0) {
      toast.error(`${product.name} is Out of Stock!`)
      return
    }

    setCart(prev => {
      const existing = prev.find(p => p.id === product.id)
      if (existing) {
        if (existing.cartQuantity >= product.quantity) {
          toast.error(`Insufficient stock for ${product.name} (Max: ${product.quantity})`)
          return prev
        }
        return prev.map(p => p.id === product.id ? { ...p, cartQuantity: p.cartQuantity + 1 } : p)
      }
      return [...prev, { ...product, cartQuantity: 1 }]
    })
    setShowResults(false)
    setSearchQuery('')
  }

  const updateQty = (id: number, delta: number) => {
    setCart(prev => prev.map(p => {
      if (p.id === id) {
        const newQty = Math.max(1, Math.min(p.cartQuantity + delta, p.quantity))
        if (newQty === p.quantity && delta > 0) {
          toast.error(`Maximum available stock reached (${p.quantity})`)
        }
        return { ...p, cartQuantity: newQty }
      }
      return p
    }))
  }

  const handleDirectQtyChange = (id: number, val: number) => {
    setCart(prev => prev.map(p => {
      if (p.id === id) {
        const validQty = Math.max(1, Math.min(val, p.quantity))
        return { ...p, cartQuantity: validQty }
      }
      return p
    }))
  }

  const removeItem = (id: number) => {
    setCart(prev => prev.filter(p => p.id !== id))
  }

  const handleCheckout = async () => {
    if (cart.length === 0) {
      toast.error('Cart is empty')
      return
    }

    setProcessing(true)
    try {
      const bill: Bill = await createBill({
        items: cart.map(item => ({ productId: item.id, quantity: item.cartQuantity })),
        discountAmount: discount,
        paymentMethod
      })

      toast.success(`Bill ${bill.billNumber} created successfully!`)
      setCreatedBill(bill)
      setReceiptModalOpen(true)
      setCart([])
      setDiscount(0)
    } catch (err: any) {
      toast.error(err.response?.data?.message || 'Failed to generate bill')
    } finally {
      setProcessing(false)
    }
  }

  // Keyboard Hotkeys support (F9 to checkout, Esc to clear search)
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'F9') {
        e.preventDefault()
        if (cart.length > 0 && !processing) {
          handleCheckout()
        }
      } else if (e.key === 'Escape') {
        setShowResults(false)
        setShowLargeUpiModal(false)
      }
    }
    window.addEventListener('keydown', handleKeyDown)
    return () => window.removeEventListener('keydown', handleKeyDown)
  }, [cart, processing, handleCheckout])

  useEffect(() => {
    const handleClick = (e: MouseEvent) => {
      if (searchRef.current && !searchRef.current.contains(e.target as Node)) {
        setShowResults(false)
      }
    }
    document.addEventListener('mousedown', handleClick)
    return () => document.removeEventListener('mousedown', handleClick)
  }, [])

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">POS Billing Terminal</h1>
          <p className="text-sm text-gray-500">Scan barcodes, manage cart, and generate UPI payment QR codes.</p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={() => setScannerOpen(true)}
            className="btn-primary flex items-center gap-2"
          >
            <Camera className="w-4 h-4" />
            <span>Scan Camera</span>
          </button>
          {cart.length > 0 && (
            <button
              onClick={() => setCart([])}
              className="btn-secondary text-red-600 hover:text-red-700"
            >
              Clear Cart
            </button>
          )}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Product Search & Cart */}
        <div className="lg:col-span-2 space-y-4">
          <div className="card" ref={searchRef}>
            <div className="flex gap-2">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="text"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                      handleBarcodeScan(searchQuery)
                    }
                  }}
                  placeholder="Scan barcode or type product name... (Press Enter to scan)"
                  className="input-field pl-10"
                  autoFocus
                />
              </div>
              <button
                onClick={() => setScannerOpen(true)}
                className="btn-secondary flex items-center gap-1.5 shrink-0"
                title="Use webcam to scan barcode"
              >
                <Camera className="w-4 h-4 text-blue-600 dark:text-blue-400" />
                <span className="hidden sm:inline">Camera</span>
              </button>
            </div>

            {showResults && searchResults.length > 0 && (
              <div className="mt-2 border border-gray-200 dark:border-gray-700 rounded-xl max-h-64 overflow-y-auto shadow-lg bg-white dark:bg-gray-800 divide-y divide-gray-100 dark:divide-gray-700 z-20 relative">
                {searchResults.map(product => (
                  <button
                    key={product.id}
                    onClick={() => addToCart(product)}
                    className="w-full flex items-center justify-between p-3.5 hover:bg-primary-50/50 dark:hover:bg-gray-700/60 text-left transition-colors"
                  >
                    <div>
                      <p className="font-semibold text-gray-900 dark:text-white">{product.name}</p>
                      <p className="text-xs text-gray-500">
                        {product.category} | Barcode: <span className="font-mono">{product.barcode}</span> | Stock: {product.quantity}
                      </p>
                    </div>
                    <div className="text-right">
                      <span className="font-bold text-primary-600 block">Rs.{product.sellingPrice.toFixed(2)}</span>
                      <span className="text-[10px] text-gray-400">GST {product.gstPercentage}%</span>
                    </div>
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* Cart Items Table */}
          <div className="card space-y-4">
            <div className="flex items-center justify-between border-b border-gray-100 dark:border-gray-700 pb-3">
              <div className="flex items-center gap-2">
                <ShoppingCart className="w-5 h-5 text-primary-600" />
                <h3 className="text-lg font-bold">Shopping Cart ({cart.length} items)</h3>
              </div>
              <span className="text-xs text-gray-400 hidden sm:inline">Shortcut: Press F9 to Checkout</span>
            </div>

            {cart.length === 0 ? (
              <div className="text-center py-12 space-y-3">
                <ShoppingCart className="w-12 h-12 text-gray-300 mx-auto" />
                <p className="text-gray-500 font-medium">Cart is empty. Scan barcodes or search products to begin billing.</p>
              </div>
            ) : (
              <div className="space-y-3">
                {cart.map(item => (
                  <div
                    key={item.id}
                    className="flex flex-col sm:flex-row sm:items-center justify-between p-3.5 bg-gray-50 dark:bg-gray-700/50 rounded-xl border border-gray-100 dark:border-gray-700 gap-3"
                  >
                    <div className="flex-1">
                      <p className="font-semibold text-gray-900 dark:text-white">{item.name}</p>
                      <div className="flex items-center gap-3 text-xs text-gray-500 mt-1">
                        <span>Unit: Rs.{item.sellingPrice.toFixed(2)}</span>
                        <span>GST: {item.gstPercentage}%</span>
                        <span className="font-mono text-gray-400">Code: {item.barcode}</span>
                      </div>
                    </div>

                    <div className="flex items-center justify-between sm:justify-end gap-4">
                      <div className="flex items-center gap-1 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg p-1">
                        <button
                          onClick={() => updateQty(item.id, -1)}
                          className="p-1 hover:bg-gray-100 dark:hover:bg-gray-700 rounded text-gray-600 dark:text-gray-300"
                        >
                          <Minus className="w-4 h-4" />
                        </button>
                        <input
                          type="number"
                          value={item.cartQuantity}
                          onChange={e => handleDirectQtyChange(item.id, Number(e.target.value))}
                          className="w-12 text-center font-bold text-sm bg-transparent border-0 focus:ring-0 p-0"
                          min="1"
                          max={item.quantity}
                        />
                        <button
                          onClick={() => updateQty(item.id, 1)}
                          className="p-1 hover:bg-gray-100 dark:hover:bg-gray-700 rounded text-gray-600 dark:text-gray-300"
                        >
                          <Plus className="w-4 h-4" />
                        </button>
                      </div>

                      <div className="text-right min-w-[80px]">
                        <p className="font-bold text-gray-900 dark:text-white">
                          Rs.{(item.sellingPrice * item.cartQuantity).toFixed(2)}
                        </p>
                      </div>

                      <button
                        onClick={() => removeItem(item.id)}
                        className="p-1.5 text-red-500 hover:bg-red-50 dark:hover:bg-red-900/30 rounded-lg transition"
                        title="Remove item"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Checkout & UPI Payment Panel */}
        <div className="space-y-4">
          <div className="card space-y-4">
            <h3 className="text-lg font-bold border-b border-gray-100 dark:border-gray-700 pb-3">
              Payment Summary
            </h3>

            <div className="space-y-2.5 text-sm">
              <div className="flex justify-between text-gray-600 dark:text-gray-400">
                <span>Items Subtotal</span>
                <span className="font-medium text-gray-900 dark:text-white">Rs.{subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-gray-600 dark:text-gray-400">
                <span>CGST</span>
                <span>Rs.{cgst.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-gray-600 dark:text-gray-400">
                <span>SGST</span>
                <span>Rs.{sgst.toFixed(2)}</span>
              </div>
              <div className="flex justify-between items-center pt-1">
                <span className="text-gray-600 dark:text-gray-400">Discount Amount</span>
                <div className="relative w-28">
                  <span className="absolute left-2.5 top-1/2 -translate-y-1/2 text-xs text-gray-400">Rs.</span>
                  <input
                    type="number"
                    value={discount}
                    onChange={(e) => setDiscount(Math.max(0, Number(e.target.value)))}
                    className="input-field text-right py-1 pl-7 pr-2 font-semibold"
                    min="0"
                  />
                </div>
              </div>
              <div className="border-t border-gray-200 dark:border-gray-700 pt-3 flex justify-between items-baseline">
                <span className="font-bold text-base">Grand Total</span>
                <span className="text-2xl font-extrabold text-primary-600 dark:text-primary-400">
                  Rs.{grandTotal.toFixed(2)}
                </span>
              </div>
            </div>

            {/* Payment Method Selector */}
            <div className="space-y-2 pt-2">
              <label className="text-xs font-bold uppercase tracking-wider text-gray-500">Payment Method</label>
              <div className="grid grid-cols-3 gap-2">
                {[
                  { key: 'CASH' as const, icon: Banknote, label: 'Cash' },
                  { key: 'CARD' as const, icon: CreditCard, label: 'Card' },
                  { key: 'UPI' as const, icon: Smartphone, label: 'UPI QR' }
                ].map(({ key, icon: Icon, label }) => (
                  <button
                    key={key}
                    onClick={() => setPaymentMethod(key)}
                    className={`flex flex-col items-center gap-1 p-3 rounded-xl border-2 font-semibold transition-all ${
                      paymentMethod === key
                        ? 'border-primary-600 bg-primary-50 dark:bg-primary-950/40 text-primary-600 dark:text-primary-400 shadow-sm'
                        : 'border-gray-200 dark:border-gray-700 hover:border-gray-300 text-gray-600 dark:text-gray-400'
                    }`}
                  >
                    <Icon className="w-5 h-5" />
                    <span className="text-xs">{label}</span>
                  </button>
                ))}
              </div>
            </div>

            {/* Live UPI QR Payment Box */}
            {paymentMethod === 'UPI' && (
              <div className="p-4 bg-gradient-to-b from-primary-50/60 to-white dark:from-gray-800 dark:to-gray-800 rounded-xl border border-primary-200 dark:border-primary-900/50 text-center space-y-2">
                <div className="flex items-center justify-between text-xs text-primary-700 dark:text-primary-300 font-semibold">
                  <span className="flex items-center gap-1">
                    <QrCode className="w-4 h-4" /> Scan & Pay via UPI
                  </span>
                  {upiQrCode && (
                    <button
                      onClick={() => setShowLargeUpiModal(true)}
                      className="hover:underline flex items-center gap-1 text-[11px]"
                    >
                      <Maximize2 className="w-3 h-3" /> Enlarge
                    </button>
                  )}
                </div>

                {upiLoading ? (
                  <div className="py-8 text-xs text-gray-400 animate-pulse">Generating UPI payment QR code...</div>
                ) : upiQrCode ? (
                  <div className="space-y-2">
                    <div className="bg-white p-2 rounded-xl inline-block shadow-sm border border-gray-100">
                      <img src={upiQrCode} alt="UPI Payment QR" className="w-36 h-36 mx-auto object-contain" />
                    </div>
                    <p className="text-xs font-bold text-gray-800 dark:text-gray-200">
                      Scan with GPay / PhonePe / Paytm
                    </p>
                    <p className="text-xs text-primary-600 font-extrabold">Rs.{grandTotal.toFixed(2)}</p>
                  </div>
                ) : (
                  <p className="text-xs text-gray-400 py-4">Add items to cart to generate payment QR code.</p>
                )}
              </div>
            )}

            <button
              onClick={handleCheckout}
              disabled={processing || cart.length === 0}
              className="btn-primary w-full flex items-center justify-center gap-2 py-3.5 text-base font-bold shadow-lg shadow-primary-500/20 disabled:opacity-50"
            >
              <Printer className="w-5 h-5" />
              {processing ? 'Generating Bill...' : `Generate Bill (F9)`}
            </button>
          </div>
        </div>
      </div>

      {/* Enlarge UPI QR Modal */}
      {showLargeUpiModal && upiQrCode && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-md z-50 flex items-center justify-center p-4" onClick={() => setShowLargeUpiModal(false)}>
          <div className="bg-white dark:bg-gray-800 rounded-3xl p-8 max-w-sm w-full text-center space-y-4 shadow-2xl border border-gray-100 dark:border-gray-700" onClick={e => e.stopPropagation()}>
            <div className="flex justify-between items-center border-b border-gray-100 dark:border-gray-700 pb-3">
              <h3 className="font-bold text-lg flex items-center gap-2 text-primary-600">
                <Smartphone className="w-5 h-5" /> Customer Payment QR
              </h3>
              <button onClick={() => setShowLargeUpiModal(false)} className="p-1 text-gray-400 hover:text-gray-600">
                <X className="w-5 h-5" />
              </button>
            </div>
            <div className="bg-white p-4 rounded-2xl border-2 border-primary-500/20 shadow-md inline-block">
              <img src={upiQrCode} alt="Enlarged UPI QR" className="w-64 h-64 mx-auto object-contain" />
            </div>
            <div className="space-y-1">
              <p className="text-2xl font-black text-primary-600">Rs.{grandTotal.toFixed(2)}</p>
              <p className="text-xs text-gray-500">Scan using any UPI App (Google Pay, PhonePe, Paytm, BHIM)</p>
            </div>
            <button onClick={() => setShowLargeUpiModal(false)} className="btn-secondary w-full py-2">
              Close
            </button>
          </div>
        </div>
      )}

      {/* Live Camera Scanner Modal */}
      <CameraScannerModal
        isOpen={scannerOpen}
        onClose={() => setScannerOpen(false)}
        onScan={handleBarcodeScan}
        title="POS Camera Barcode / QR Scanner"
      />

      {/* Printable Receipt Modal with Embedded Bill Verification QR */}
      <ReceiptModal
        isOpen={receiptModalOpen}
        onClose={() => { setReceiptModalOpen(false); setCreatedBill(null); }}
        bill={createdBill}
      />
    </div>
  )
}