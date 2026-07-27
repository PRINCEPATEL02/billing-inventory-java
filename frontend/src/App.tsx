import { Routes, Route, Navigate } from 'react-router-dom'
import { useEffect } from 'react'
import { Toaster } from 'react-hot-toast'
import { useAuthStore } from './stores/authStore'
import Navbar from './components/Navbar'
import Dashboard from './pages/Dashboard'
import Billing from './pages/Billing'
import Products from './pages/Products'
import BillHistory from './pages/BillHistory'
import SettingsPage from './pages/Settings'
import Login from './pages/Login'
import Purchases from './pages/Purchases'
import InventoryAlerts from './pages/InventoryAlerts'
import Reports from './pages/Reports'
import Notifications from './pages/Notifications'
import AiAnalytics from './pages/AiAnalytics'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const token = useAuthStore(state => state.token)
  if (!token) return <Navigate to="/login" replace />
  return <>{children}</>
}

export default function App() {
  const token = useAuthStore(state => state.token)

  useEffect(() => {
    const saved = localStorage.getItem('billing-dark-mode') === 'true'
    document.documentElement.classList.toggle('dark', saved)
  }, [])

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
      <Toaster position="top-right" />
      {token && <Navbar />}
      <main className={token ? 'max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8' : ''}>
        <Routes>
          <Route path="/login" element={token ? <Navigate to="/" replace /> : <Login />} />
          <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/billing" element={<ProtectedRoute><Billing /></ProtectedRoute>} />
          <Route path="/products" element={<ProtectedRoute><Products /></ProtectedRoute>} />
          <Route path="/bills" element={<ProtectedRoute><BillHistory /></ProtectedRoute>} />
          <Route path="/purchases" element={<ProtectedRoute><Purchases /></ProtectedRoute>} />
          <Route path="/inventory-alerts" element={<ProtectedRoute><InventoryAlerts /></ProtectedRoute>} />
          <Route path="/notifications" element={<ProtectedRoute><Notifications /></ProtectedRoute>} />
          <Route path="/ai-analytics" element={<ProtectedRoute><AiAnalytics /></ProtectedRoute>} />
          <Route path="/reports" element={<ProtectedRoute><Reports /></ProtectedRoute>} />
          <Route path="/settings" element={<ProtectedRoute><SettingsPage /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  )
}
