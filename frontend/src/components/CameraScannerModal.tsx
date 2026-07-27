import { useEffect, useRef, useState } from 'react'
import { Html5Qrcode, Html5QrcodeSupportedFormats } from 'html5-qrcode'
import { X, Camera, RefreshCw, AlertCircle, CheckCircle2 } from 'lucide-react'
import toast from 'react-hot-toast'

interface CameraScannerModalProps {
  isOpen: boolean
  onClose: () => void
  onScan: (decodedText: string) => void
  title?: string
}

export default function CameraScannerModal({
  isOpen,
  onClose,
  onScan,
  title = 'Scan Product Barcode / QR Code'
}: CameraScannerModalProps) {
  const [cameras, setCameras] = useState<{ id: string; label: string }[]>([])
  const [selectedCamera, setSelectedCamera] = useState<string>('')
  const [error, setError] = useState<string>('')
  const [isScanning, setIsScanning] = useState<boolean>(false)
  const scannerRef = useRef<Html5Qrcode | null>(null)
  const hasScannedRef = useRef<boolean>(false)

  useEffect(() => {
    if (!isOpen) {
      stopScanner()
      return
    }

    hasScannedRef.current = false

    Html5Qrcode.getCameras()
      .then(devices => {
        if (devices && devices.length > 0) {
          setCameras(devices)
          // Prefer environment / back camera
          const backCam = devices.find(d => 
            d.label.toLowerCase().includes('back') || 
            d.label.toLowerCase().includes('environment') ||
            d.label.toLowerCase().includes('rear')
          )
          const targetCam = backCam ? backCam.id : devices[0].id
          setSelectedCamera(targetCam)
          startScanner(targetCam)
        } else {
          setError('No camera found on your device.')
        }
      })
      .catch(err => {
        setError('Camera permission denied or unavailable: ' + (err.message || err))
      })

    return () => {
      stopScanner()
    }
  }, [isOpen])

  const startScanner = async (cameraId: string) => {
    try {
      if (scannerRef.current) {
        await stopScanner()
      }

      setError('')
      const scanner = new Html5Qrcode('qr-reader')
      scannerRef.current = scanner

      const config = {
        fps: 15,
        qrbox: { width: 280, height: 180 }, // Optimal rectangular shape for 1D barcodes & 2D QR
        aspectRatio: 1.333,
        formatsToSupport: [
          Html5QrcodeSupportedFormats.QR_CODE,
          Html5QrcodeSupportedFormats.EAN_13,
          Html5QrcodeSupportedFormats.EAN_8,
          Html5QrcodeSupportedFormats.CODE_128,
          Html5QrcodeSupportedFormats.CODE_39,
          Html5QrcodeSupportedFormats.UPC_A,
          Html5QrcodeSupportedFormats.UPC_E,
          Html5QrcodeSupportedFormats.ITF,
          Html5QrcodeSupportedFormats.DATA_MATRIX
        ],
        experimentalFeatures: {
          useBarCodeDetectorIfSupported: true
        }
      }

      await scanner.start(
        cameraId,
        config,
        (decodedText) => {
          if (!decodedText || hasScannedRef.current) return
          hasScannedRef.current = true

          // Audio beep feedback
          try {
            const ctx = new (window.AudioContext || (window as any).webkitAudioContext)()
            const osc = ctx.createOscillator()
            osc.type = 'sine'
            osc.frequency.setValueAtTime(880, ctx.currentTime)
            osc.connect(ctx.destination)
            osc.start()
            osc.stop(ctx.currentTime + 0.12)
          } catch {}

          toast.success(`Scanned: ${decodedText}`)
          onScan(decodedText)

          setTimeout(() => {
            stopScanner()
            onClose()
          }, 100)
        },
        () => {
          // ignore scan error frames
        }
      )
      setIsScanning(true)
    } catch (err: any) {
      console.error('Failed to start scanner', err)
      setError(err.message || 'Unable to access camera feed.')
      setIsScanning(false)
    }
  }

  const stopScanner = async () => {
    if (scannerRef.current) {
      try {
        if (scannerRef.current.isScanning) {
          await scannerRef.current.stop()
        }
        scannerRef.current.clear()
      } catch (err) {
        // ignore stop errors
      } finally {
        scannerRef.current = null
        setIsScanning(false)
      }
    }
  }

  const handleCameraChange = (camId: string) => {
    setSelectedCamera(camId)
    startScanner(camId)
  }

  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4" onClick={onClose}>
      <div
        className="bg-white dark:bg-gray-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4 border border-gray-100 dark:border-gray-700 animate-in fade-in zoom-in duration-200"
        onClick={e => e.stopPropagation()}
      >
        <div className="flex items-center justify-between border-b border-gray-100 dark:border-gray-700 pb-3">
          <div className="flex items-center gap-2 text-primary-600 dark:text-primary-400 font-bold text-lg">
            <Camera className="w-5 h-5" />
            <span>{title}</span>
          </div>
          <button
            onClick={onClose}
            className="p-1 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {error ? (
          <div className="p-4 bg-red-50 dark:bg-red-950/40 border border-red-200 dark:border-red-800 rounded-xl text-red-700 dark:text-red-300 flex items-start gap-3">
            <AlertCircle className="w-5 h-5 shrink-0 mt-0.5" />
            <div className="text-sm">
              <p className="font-semibold">Camera Access Error</p>
              <p className="mt-1">{error}</p>
            </div>
          </div>
        ) : (
          <>
            {cameras.length > 1 && (
              <div className="flex items-center gap-2 text-sm">
                <label className="text-gray-600 dark:text-gray-300 font-medium">Camera:</label>
                <select
                  value={selectedCamera}
                  onChange={e => handleCameraChange(e.target.value)}
                  className="input-field py-1 text-sm flex-1"
                >
                  {cameras.map(c => (
                    <option key={c.id} value={c.id}>
                      {c.label || `Camera ${c.id.substring(0, 8)}...`}
                    </option>
                  ))}
                </select>
              </div>
            )}

            <div className="relative overflow-hidden rounded-2xl bg-black border-2 border-primary-500/40 shadow-inner flex items-center justify-center min-h-[300px]">
              <div id="qr-reader" className="w-full h-full"></div>
              {!isScanning && !error && (
                <div className="absolute inset-0 flex flex-col items-center justify-center text-white bg-black/70 space-y-2">
                  <RefreshCw className="w-8 h-8 animate-spin text-primary-400" />
                  <span className="text-sm font-medium">Starting camera scanner...</span>
                </div>
              )}
            </div>

            <div className="text-center space-y-1">
              <p className="text-xs font-semibold text-gray-700 dark:text-gray-300">
                Hold product barcode or QR code steady inside the box.
              </p>
              <p className="text-[11px] text-gray-400">
                Supports EAN-13, CODE-128, CODE-39, UPC, & QR Codes
              </p>
            </div>
          </>
        )}

        <div className="pt-2">
          <button onClick={onClose} className="btn-secondary w-full py-2.5">
            Close Scanner
          </button>
        </div>
      </div>
    </div>
  )
}
