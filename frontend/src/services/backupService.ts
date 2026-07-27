import api from './api'
export const downloadBackup = async () => { const r=await api.get('/backups',{responseType:'blob'});const url=URL.createObjectURL(r.data);const a=document.createElement('a');a.href=url;a.download=`inventory-backup-${new Date().toISOString().slice(0,10)}.json`;a.click();URL.revokeObjectURL(url) }
export const restoreBackup = async (file: File) => { const contents=JSON.parse(await file.text()); return api.post('/backups/restore',contents).then(r=>r.data.data) }
