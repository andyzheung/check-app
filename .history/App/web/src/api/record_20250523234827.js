import request from '../utils/request'

export function createRecord(data) {
  return request.post('/api/v1/records', data)
}

export function listRecords(params) {
  return request.get('/api/v1/records', { params })
} 