import api from './api'

export const generateCustomQrCode = (text: string, width: number = 250, height: number = 250) => {
  return api.get(`/qrcode/generate?text=${encodeURIComponent(text)}&width=${width}&height=${height}`)
    .then(r => r.data.data)
}

export const getCustomQrCodeImageUrl = (text: string, width: number = 250, height: number = 250) => {
  const baseURL = api.defaults.baseURL || '/api'
  return `${baseURL}/qrcode/image?text=${encodeURIComponent(text)}&width=${width}&height=${height}`
}
