import { useEffect, useState } from 'react'
import { getDashboard } from '../services/dashboardService'
import { DashboardData } from '../types'
import {
  DollarSign, Receipt, Package, AlertTriangle,
  TrendingUp, TrendingDown
} from 'lucide-react'
import {
  Chart as ChartJS,
  CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, PointElement, LineElement
} from 'chart.js'
import { Bar, Doughnut, Line } from 'react-chartjs-2'
import toast from 'react-hot-toast'

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, PointElement, LineElement)

const StatCard = ({ title, value, icon: Icon, color }: any) => (
  <div className="card">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm text-gray-500 dark:text-gray-400">{title}</p>
        <p className="text-2xl font-bold mt-1">{value}</p>
      </div>
      <div className={`p-3 rounded-lg ${color}`}>
        <Icon className="w-6 h-6 text-white" />
      </div>
    </div>
  </div>
)

export default function Dashboard() {
  const [data, setData] = useState<DashboardData | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadDashboard()
  }, [])

  const loadDashboard = async () => {
    try {
      const res = await getDashboard()
      setData(res)
    } catch (err) {
      toast.error('Failed to load dashboard')
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div className="text-center py-20">Loading dashboard...</div>
  if (!data) return <div className="text-center py-20">No data available</div>

  const salesChartData = {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    datasets: [{
      label: 'Sales',
      data: [1200, 1900, 1500, 2200, 1800, 2500, 2100],
      backgroundColor: 'rgba(59, 130, 246, 0.5)',
      borderColor: 'rgb(59, 130, 246)',
      borderWidth: 2,
      borderRadius: 4,
    }]
  }

  const topProductsData = {
    labels: ['Rice', 'Wheat', 'Sugar', 'Milk', 'Tea'],
    datasets: [{
      data: [45, 38, 32, 28, 25],
      backgroundColor: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
      borderWidth: 0,
    }]
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <button onClick={loadDashboard} className="btn-secondary text-sm">Refresh</button>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard title="Today\'s Sales" value={`Rs.${data.todaySales?.toLocaleString() || 0}`} icon={DollarSign} color="bg-green-500" />
        <StatCard title="Total Bills" value={data.totalBills || 0} icon={Receipt} color="bg-blue-500" />
        <StatCard title="Total Products" value={data.totalProducts || 0} icon={Package} color="bg-purple-500" />
        <StatCard title="Low Stock" value={data.lowStockCount || 0} icon={AlertTriangle} color="bg-red-500" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="card lg:col-span-2">
          <h3 className="text-lg font-semibold mb-4">Daily Sales</h3>
          <div className="relative h-64"><Bar data={salesChartData} options={{ responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }} /></div>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Top Selling Products</h3>
          <div className="relative h-64"><Doughnut data={topProductsData} options={{ responsive: true, maintainAspectRatio: false, cutout: '65%' }} /></div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Monthly Sales Trend</h3>
          <div className="relative h-52"><Line data={{
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
            datasets: [{
              label: 'Revenue',
              data: [45000, 52000, 48000, 61000, 55000, 67000],
              borderColor: 'rgb(59, 130, 246)',
              backgroundColor: 'rgba(59, 130, 246, 0.1)',
              fill: true,
              tension: 0.4,
            }]
          }} options={{ responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } } }} /></div>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Revenue Overview</h3>
          <div className="space-y-4">
            <div className="flex justify-between items-center p-3 bg-gray-50 dark:bg-gray-700 rounded-lg">
              <span className="text-gray-600 dark:text-gray-300">Weekly Sales</span>
              <span className="font-semibold">Rs.{data.weeklySales?.toLocaleString() || 0}</span>
            </div>
            <div className="flex justify-between items-center p-3 bg-gray-50 dark:bg-gray-700 rounded-lg">
              <span className="text-gray-600 dark:text-gray-300">Monthly Sales</span>
              <span className="font-semibold">Rs.{data.monthlySales?.toLocaleString() || 0}</span>
            </div>
            <div className="flex justify-between items-center p-3 bg-gray-50 dark:bg-gray-700 rounded-lg">
              <span className="text-gray-600 dark:text-gray-300">Yearly Sales</span>
              <span className="font-semibold">Rs.{data.yearlySales?.toLocaleString() || 0}</span>
            </div>
            <div className="flex justify-between items-center p-3 bg-primary-50 dark:bg-primary-900/20 rounded-lg">
              <span className="text-primary-700 dark:text-primary-300 font-medium">Total Revenue</span>
              <span className="font-bold text-primary-700 dark:text-primary-300">Rs.{data.totalRevenue?.toLocaleString() || 0}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
