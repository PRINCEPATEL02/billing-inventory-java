import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuthStore } from '../stores/authStore'
import {
  LayoutDashboard, ShoppingCart, Package, FileText, Settings as SettingsIcon, LogOut, Store, Truck, Bell, BarChart3, Brain
} from 'lucide-react'

export default function Navbar() {
  const location = useLocation()
  const navigate = useNavigate()
  const { user, logout } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = [
    { path: '/', label: 'Dashboard', icon: LayoutDashboard },
    { path: '/billing', label: 'POS Billing', icon: ShoppingCart },
    { path: '/products', label: 'Products', icon: Package },
    { path: '/bills', label: 'Bill History', icon: FileText },
    { path: '/purchases', label: 'Purchases', icon: Truck },
    { path: '/inventory-alerts', label: 'Alerts', icon: Bell },
    { path: '/notifications', label: 'Notifications', icon: Bell },
    { path: '/reports', label: 'Reports', icon: BarChart3 },
    { path: '/ai-analytics', label: 'AI Insights', icon: Brain },
    { path: '/settings', label: 'Settings', icon: SettingsIcon },
  ]

  return (
    <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 sticky top-0 z-30">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-blue-600 rounded-xl flex items-center justify-center text-white">
              <Store className="w-6 h-6" />
            </div>
            <span className="font-bold text-lg hidden sm:block">Billing Core</span>
          </div>

          <nav className="flex items-center gap-1 sm:gap-2">
            {navItems.map(({ path, label, icon: Icon }) => {
              const active = location.pathname === path
              return (
                <Link
                  key={path}
                  to={path}
                  className={`flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                    active
                      ? 'bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400'
                      : 'text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span className="hidden md:inline">{label}</span>
                </Link>
              )
            })}
          </nav>

          <div className="flex items-center gap-3">
            <div className="text-right hidden sm:block">
              <p className="text-sm font-medium text-gray-900 dark:text-white">{user?.fullName || user?.username}</p>
              <p className="text-xs text-gray-500 capitalize">{user?.role}</p>
            </div>
            <button
              onClick={handleLogout}
              className="p-2 text-gray-500 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-colors"
              title="Logout"
            >
              <LogOut className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>
    </header>
  )
}
