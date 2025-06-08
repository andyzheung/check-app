import request from '../utils/request'

export function createRecord(data) {
  return request.post('/records', data)
}

export function listRecords(params) {
  return request.get('/records', { params })
} 