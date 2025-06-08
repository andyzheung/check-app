import request from '../utils/request'

export function createRecord(data) {
  return request.post('/v1/records', data)
}

export function listRecords(params) {
  return request.get('/v1/records', { params })
} 