export interface User {
  id: number
  username: string
  fullName: string
  email: string
  role: 'ADMIN' | 'EMPLOYEE'
  active: boolean
}

export interface Product {
  id: number
  name: string
  category: string
  barcode: string
  qrCode?: string
  imageUrl?: string
  purchasePrice: number
  sellingPrice: number
  gstPercentage: number
  hsnCode?: string
  quantity: number
  minimumQuantity: number
  active: boolean
  lowStock?: boolean
}

export interface BillItem {
  id?: number
  productId: number
  productName: string
  unitPrice: number
  quantity: number
  gstPercentage: number
  cgstAmount: number
  sgstAmount: number
  totalPrice: number
}

export interface Bill {
  id: number
  billNumber: string
  cashierName: string
  items: BillItem[]
  subtotal: number
  discountAmount: number
  cgstAmount: number
  sgstAmount: number
  grandTotal: number
  paymentMethod: 'CASH' | 'CARD' | 'UPI'
  totalItems: number
  createdAt: string
}

export interface DashboardData {
  todaySales: number
  weeklySales: number
  monthlySales: number
  yearlySales: number
  totalRevenue: number
  totalBills: number
  totalProducts: number
  currentStock: number
  lowStockCount: number
}

export interface Settings {
  id?: number
  companyName: string
  companyAddress?: string
  companyPhone?: string
  companyEmail?: string
  gstNumber?: string
  upiId?: string
  invoiceLogoUrl?: string
  invoiceSize: string
  darkMode: boolean
  printerSettings?: string
}

export interface Purchase {
  id: number; supplierName: string; invoiceNumber: string; purchaseDate: string
  productId: number; productName: string; quantity: number; purchasePrice: number
  gstPercentage: number; expiryDate?: string; batchNumber?: string; totalCost: number
}

export interface InventoryAlert {
  type: 'LOW_STOCK' | 'OUT_OF_STOCK' | 'NEAR_EXPIRY'; severity: 'warning' | 'critical'
  productId: number; productName: string; quantity: number; expiryDate?: string; message: string
}

export interface ReportSummary {
  reportType: string; from: string; to: string; sales: number; purchaseCost: number
  gstOnPurchases: number; profit: number; salesCount: number; purchaseCount: number; inventoryValue: number
}
