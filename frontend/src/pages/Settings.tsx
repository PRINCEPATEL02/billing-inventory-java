import { useEffect, useState } from 'react'
import { getSettings, updateSettings } from '../services/settingsService'
import { Settings } from '../types'
import { Building2, Save } from 'lucide-react'
import toast from 'react-hot-toast'
import { downloadBackup, restoreBackup } from '../services/backupService'

export default function SettingsPage() {
  const [settings, setSettings] = useState<Settings | null>(null)
  const [form, setForm] = useState({
    companyName: '', companyAddress: '', companyPhone: '', companyEmail: '',
    gstNumber: '', upiId: '', invoiceLogoUrl: '', invoiceSize: 'A4', darkMode: false, printerSettings: ''
  })
  const [loading, setLoading] = useState(true)
  const [automaticBackup, setAutomaticBackup] = useState(() => localStorage.getItem('billing-automatic-backup') === 'true')
  const [taxRate, setTaxRate] = useState(() => localStorage.getItem('billing-default-tax-rate') || '18')

  useEffect(() => {
    loadSettings()
  }, [])

  const loadSettings = async () => {
    try {
      const res = await getSettings()
      setSettings(res)
      setForm({
        companyName: res.companyName || '',
        companyAddress: res.companyAddress || '',
        companyPhone: res.companyPhone || '',
        companyEmail: res.companyEmail || '',
        gstNumber: res.gstNumber || '',
        upiId: res.upiId || '',
        invoiceLogoUrl: res.invoiceLogoUrl || '',
        invoiceSize: res.invoiceSize || 'A4',
        darkMode: res.darkMode || false,
        printerSettings: res.printerSettings || ''
      })
    } catch {
      toast.error('Failed to load settings')
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await updateSettings(form)
      localStorage.setItem('billing-dark-mode', String(form.darkMode))
      localStorage.setItem('billing-automatic-backup', String(automaticBackup))
      localStorage.setItem('billing-default-tax-rate', taxRate)
      document.documentElement.classList.toggle('dark', form.darkMode)
      toast.success('Settings saved')
      loadSettings()
    } catch (err: any) {
      toast.error(err.response?.data?.message || 'Failed to save')
    }
  }
  const handleRestore = async (file?: File) => {
    if (!file) return
    if (!confirm('Restore product and company settings from this backup? Current matching products will be replaced.')) return
    try { const count = await restoreBackup(file); toast.success(`${count} products restored`); loadSettings() }
    catch { toast.error('This backup file could not be restored') }
  }

  if (loading) return <div className="text-center py-20">Loading...</div>

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-2">
        <Building2 className="w-6 h-6" />
        <h1 className="text-2xl font-bold">Settings</h1>
      </div>

      <div className="card max-w-2xl">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Company Name *</label>
            <input type="text" value={form.companyName} onChange={e => setForm({...form, companyName: e.target.value})} className="input-field" required />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Default tax rate (%)</label>
            <input type="number" min="0" max="100" step="0.01" value={taxRate} onChange={e => setTaxRate(e.target.value)} className="input-field" />
            <p className="text-xs text-gray-500 mt-1">Used as the starting GST rate for new billing and purchase entries.</p>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Address</label>
            <textarea value={form.companyAddress} onChange={e => setForm({...form, companyAddress: e.target.value})} className="input-field" rows={3} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Phone</label>
              <input type="text" value={form.companyPhone} onChange={e => setForm({...form, companyPhone: e.target.value})} className="input-field" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Email</label>
              <input type="email" value={form.companyEmail} onChange={e => setForm({...form, companyEmail: e.target.value})} className="input-field" />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">GST Number</label>
              <input type="text" value={form.gstNumber} onChange={e => setForm({...form, gstNumber: e.target.value})} className="input-field" placeholder="22AAAAA0000A1Z5" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Shop UPI ID (for QR payment)</label>
              <input type="text" value={form.upiId} onChange={e => setForm({...form, upiId: e.target.value})} className="input-field" placeholder="merchant@upi or 9876543210@upi" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Invoice Logo URL</label>
            <input type="url" value={form.invoiceLogoUrl} onChange={e => setForm({...form, invoiceLogoUrl: e.target.value})} className="input-field" placeholder="https://..." />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Printer Settings</label>
            <input type="text" value={form.printerSettings} onChange={e => setForm({...form, printerSettings: e.target.value})} className="input-field" placeholder="Printer name or configuration" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Invoice Size</label>
            <select value={form.invoiceSize} onChange={e => setForm({...form, invoiceSize: e.target.value})} className="input-field">
              <option value="A4">A4</option>
              <option value="THERMAL">Thermal (80mm)</option>
            </select>
          </div>
          <div className="flex items-center gap-2">
            <input type="checkbox" id="darkMode" checked={form.darkMode} onChange={e => setForm({...form, darkMode: e.target.checked})} className="w-4 h-4" />
            <label htmlFor="darkMode" className="text-sm font-medium">Dark Mode</label>
          </div>
          <button type="submit" className="btn-primary flex items-center gap-2">
            <Save className="w-4 h-4" /> Save Settings
          </button>
        </form>
      </div>
      <div className="card max-w-2xl space-y-3">
        <div><h2 className="font-semibold">Backup & restore</h2><p className="text-sm text-gray-500">Download an inventory and company-settings backup, or restore a backup you previously exported.</p></div>
        <label className="flex items-center gap-2 text-sm"><input type="checkbox" checked={automaticBackup} onChange={e => setAutomaticBackup(e.target.checked)} className="w-4 h-4" /> Enable automatic backup reminder</label>
        <div className="flex flex-wrap gap-3"><button onClick={async () => { try { await downloadBackup(); toast.success('Backup downloaded') } catch { toast.error('Backup failed') } }} className="btn-secondary">Manual backup</button><label className="btn-secondary cursor-pointer">Restore backup<input type="file" accept="application/json" className="hidden" onChange={e => handleRestore(e.target.files?.[0])} /></label></div>
      </div>
    </div>
  )
}
