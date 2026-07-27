import { useEffect, useState } from 'react'
import { getProducts, createProduct, updateProduct, deleteProduct, searchProducts, getProductQrCode, generateAllQrCodes } from '../services/productService'
import { Product } from '../types'
import { Search, Plus, Pencil, Trash2, QrCode, Package, AlertTriangle, Camera, Sparkles, RefreshCw } from 'lucide-react'
import toast from 'react-hot-toast'
import QRCodeModal from '../components/QRCodeModal'
import CameraScannerModal from '../components/CameraScannerModal'

const emptyProduct = {
  name: '', category: '', barcode: '', purchasePrice: 0, sellingPrice: 0,
  gstPercentage: 0, hsnCode: '', quantity: 0, minimumQuantity: 10
}

export default function Products() {
  const [products, setProducts] = useState<Product[]>([])
  const [searchQuery, setSearchQuery] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editing, setEditing] = useState<Product | null>(null)
  const [form, setForm] = useState(emptyProduct)
  const [loading, setLoading] = useState(true)

  // QR Modal State
  const [qrModalOpen, setQrModalOpen] = useState(false)
  const [activeQrProduct, setActiveQrProduct] = useState<Product | null>(null)
  const [activeQrUrl, setActiveQrUrl] = useState<string>('')

  // Camera Scanner Modal State
  const [scannerOpen, setScannerOpen] = useState(false)

  useEffect(() => { loadProducts() }, [])

  const loadProducts = async () => {
    try { setProducts(await getProducts()) }
    catch { toast.error('Failed to load products') }
    finally { setLoading(false) }
  }

  const handleSearch = async (queryToSearch = searchQuery) => {
    if (!queryToSearch.trim()) { loadProducts(); return }
    try { setProducts(await searchProducts(queryToSearch)) }
    catch { toast.error('Search failed') }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      if (editing) {
        await updateProduct(editing.id, form)
        toast.success('Product updated successfully')
      } else {
        await createProduct(form)
        toast.success('Product created with QR code')
      }
      setShowForm(false)
      setEditing(null)
      setForm(emptyProduct)
      loadProducts()
    } catch (err: any) {
      toast.error(err.response?.data?.message || 'Operation failed')
    }
  }

  const handleEdit = (product: Product) => {
    setEditing(product)
    setForm({
      name: product.name, category: product.category, barcode: product.barcode,
      purchasePrice: product.purchasePrice, sellingPrice: product.sellingPrice,
      gstPercentage: product.gstPercentage, hsnCode: product.hsnCode || '',
      quantity: product.quantity, minimumQuantity: product.minimumQuantity
    })
    setShowForm(true)
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Delete this product?')) return
    try { await deleteProduct(id); toast.success('Deleted'); loadProducts() }
    catch { toast.error('Delete failed') }
  }

  const handleOpenQrModal = async (product: Product) => {
    setActiveQrProduct(product)
    try {
      let qrUrl = product.qrCode
      const isValid = qrUrl && (qrUrl.startsWith('data:image/') || qrUrl.startsWith('http://') || qrUrl.startsWith('https://'))
      if (!isValid) {
        qrUrl = await getProductQrCode(product.id)
      }
      setActiveQrUrl(qrUrl || '')
      setQrModalOpen(true)
    } catch {
      setActiveQrUrl('')
      setQrModalOpen(true)
    }
  }

  const handleBulkGenerateQr = async () => {
    try {
      const count = await generateAllQrCodes()
      toast.success(`Generated QR Codes for ${count} products!`)
      loadProducts()
    } catch {
      toast.error('Failed to generate missing QR codes')
    }
  }

  const handleCameraScan = (scannedText: string) => {
    setSearchQuery(scannedText)
    handleSearch(scannedText)
  }

  if (loading) return <div className="text-center py-20 text-gray-500">Loading products database...</div>

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">Products & Inventory</h1>
          <p className="text-sm text-gray-500">Manage catalog, barcodes, and automated QR codes.</p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={handleBulkGenerateQr}
            className="btn-secondary flex items-center gap-2 text-sm"
            title="Bulk generate missing QR codes for all products"
          >
            <Sparkles className="w-4 h-4 text-purple-600 dark:text-purple-400" />
            <span>Generate Missing QRs</span>
          </button>
          <button
            onClick={() => { setShowForm(true); setEditing(null); setForm(emptyProduct) }}
            className="btn-primary flex items-center gap-2"
          >
            <Plus className="w-4 h-4" /> Add Product
          </button>
        </div>
      </div>

      <div className="card">
        <div className="flex flex-col sm:flex-row gap-2 mb-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              value={searchQuery}
              onChange={e => setSearchQuery(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && handleSearch()}
              placeholder="Search by name, category, or scan barcode..."
              className="input-field pl-10"
            />
          </div>

          <div className="flex items-center gap-2">
            <button onClick={() => handleSearch()} className="btn-primary flex items-center gap-1">
              Search
            </button>
            <button
              onClick={() => setScannerOpen(true)}
              className="btn-secondary flex items-center gap-1.5"
              title="Scan Barcode / QR with Camera"
            >
              <Camera className="w-4 h-4 text-blue-600 dark:text-blue-400" />
              <span className="hidden sm:inline">Scan Camera</span>
            </button>
            <button onClick={() => { setSearchQuery(''); loadProducts(); }} className="btn-secondary">
              Reset
            </button>
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200 dark:border-gray-700">
                <th className="text-left py-3 px-4 text-sm font-medium text-gray-500">Product</th>
                <th className="text-left py-3 px-4 text-sm font-medium text-gray-500">Category</th>
                <th className="text-left py-3 px-4 text-sm font-medium text-gray-500">Barcode / QR</th>
                <th className="text-right py-3 px-4 text-sm font-medium text-gray-500">Price</th>
                <th className="text-right py-3 px-4 text-sm font-medium text-gray-500">Stock</th>
                <th className="text-center py-3 px-4 text-sm font-medium text-gray-500">Actions</th>
              </tr>
            </thead>
            <tbody>
              {products.map(p => (
                <tr key={p.id} className="border-b border-gray-100 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                  <td className="py-3 px-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 bg-gray-100 dark:bg-gray-700 rounded-lg flex items-center justify-center overflow-hidden border border-gray-200 dark:border-gray-600">
                        {p.imageUrl ? (
                          <img src={p.imageUrl} className="w-full h-full object-cover" alt="" />
                        ) : (
                          <Package className="w-5 h-5 text-gray-400" />
                        )}
                      </div>
                      <div>
                        <p className="font-medium text-gray-900 dark:text-white">{p.name}</p>
                        <p className="text-xs text-gray-500">GST: {p.gstPercentage}% {p.hsnCode ? `| HSN: ${p.hsnCode}` : ''}</p>
                      </div>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-sm text-gray-600 dark:text-gray-300">{p.category}</td>
                  <td className="py-3 px-4 text-sm">
                    <div className="flex items-center gap-2">
                      <span className="font-mono text-xs bg-gray-100 dark:bg-gray-700 px-2 py-0.5 rounded border border-gray-200 dark:border-gray-600">
                        {p.barcode || 'N/A'}
                      </span>
                      <button
                        onClick={() => handleOpenQrModal(p)}
                        className="p-1 hover:bg-primary-50 dark:hover:bg-primary-900/30 text-primary-600 dark:text-primary-400 rounded transition"
                        title="View / Print QR Code & Barcode Label"
                      >
                        <QrCode className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                  <td className="py-3 px-4 text-right text-sm font-semibold text-gray-900 dark:text-white">
                    Rs.{p.sellingPrice.toFixed(2)}
                  </td>
                  <td className="py-3 px-4 text-right">
                    <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-semibold ${
                      p.lowStock ? 'bg-red-100 text-red-700 dark:bg-red-950/60 dark:text-red-300' : 'bg-green-100 text-green-700 dark:bg-green-950/60 dark:text-green-300'
                    }`}>
                      {p.lowStock && <AlertTriangle className="w-3 h-3" />}
                      {p.quantity} units
                    </span>
                  </td>
                  <td className="py-3 px-4 text-center">
                    <div className="flex items-center justify-center gap-1">
                      <button
                        onClick={() => handleOpenQrModal(p)}
                        className="p-1.5 hover:bg-gray-100 dark:hover:bg-gray-700 text-purple-600 dark:text-purple-400 rounded-lg transition"
                        title="Product QR Code & Label"
                      >
                        <QrCode className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => handleEdit(p)}
                        className="p-1.5 hover:bg-gray-100 dark:hover:bg-gray-700 text-blue-600 dark:text-blue-400 rounded-lg transition"
                        title="Edit product"
                      >
                        <Pencil className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => handleDelete(p.id)}
                        className="p-1.5 hover:bg-red-50 dark:hover:bg-red-900/20 text-red-600 dark:text-red-400 rounded-lg transition"
                        title="Delete product"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {products.length === 0 && <p className="text-center py-12 text-gray-500">No products found in inventory</p>}
        </div>
      </div>

      {/* Product Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4" onClick={() => setShowForm(false)}>
          <div className="bg-white dark:bg-gray-800 rounded-2xl max-w-lg w-full max-h-[90vh] overflow-y-auto p-6 shadow-2xl space-y-4" onClick={e => e.stopPropagation()}>
            <h2 className="text-xl font-bold">{editing ? 'Edit Product' : 'Add New Product'}</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Product Name *</label>
                <input type="text" value={form.name} onChange={e => setForm({...form, name: e.target.value})} className="input-field" required />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Category *</label>
                <input type="text" value={form.category} onChange={e => setForm({...form, category: e.target.value})} className="input-field" required />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Barcode / Code</label>
                <input type="text" value={form.barcode} onChange={e => setForm({...form, barcode: e.target.value})} className="input-field" placeholder="Leave empty to auto-generate unique barcode & QR" />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Purchase Price *</label>
                  <input type="number" step="0.01" value={form.purchasePrice} onChange={e => setForm({...form, purchasePrice: Number(e.target.value)})} className="input-field" required />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Selling Price *</label>
                  <input type="number" step="0.01" value={form.sellingPrice} onChange={e => setForm({...form, sellingPrice: Number(e.target.value)})} className="input-field" required />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">GST % *</label>
                  <input type="number" step="0.01" value={form.gstPercentage} onChange={e => setForm({...form, gstPercentage: Number(e.target.value)})} className="input-field" required />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">HSN Code</label>
                  <input type="text" value={form.hsnCode} onChange={e => setForm({...form, hsnCode: e.target.value})} className="input-field" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Quantity *</label>
                  <input type="number" value={form.quantity} onChange={e => setForm({...form, quantity: Number(e.target.value)})} className="input-field" required />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Min Quantity Alert *</label>
                  <input type="number" value={form.minimumQuantity} onChange={e => setForm({...form, minimumQuantity: Number(e.target.value)})} className="input-field" required />
                </div>
              </div>
              <div className="flex gap-2 pt-4">
                <button type="submit" className="btn-primary flex-1 py-2.5">{editing ? 'Update Product' : 'Create Product'}</button>
                <button type="button" onClick={() => setShowForm(false)} className="btn-secondary flex-1 py-2.5">Cancel</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* QR Code Inspection & Printing Modal */}
      {activeQrProduct && (
        <QRCodeModal
          isOpen={qrModalOpen}
          onClose={() => { setQrModalOpen(false); setActiveQrProduct(null); }}
          title="Product Label & QR Code"
          subtitle={`Category: ${activeQrProduct.category}`}
          qrCodeUrl={activeQrUrl}
          barcodeText={activeQrProduct.barcode}
          productDetails={{
            name: activeQrProduct.name,
            price: activeQrProduct.sellingPrice,
            category: activeQrProduct.category,
            gstPercentage: activeQrProduct.gstPercentage,
            hsnCode: activeQrProduct.hsnCode
          }}
        />
      )}

      {/* Live Camera Scanner Modal */}
      <CameraScannerModal
        isOpen={scannerOpen}
        onClose={() => setScannerOpen(false)}
        onScan={handleCameraScan}
        title="Scan Product Barcode / QR Code"
      />
    </div>
  )
}