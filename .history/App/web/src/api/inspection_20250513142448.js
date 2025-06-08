import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000
})

// 巡检点相关接口
export const getInspectionPoint = (code) => {
    return api.get(`/inspection-points/${code}`)
}

// 巡检记录相关接口
export const createRecord = (data) => {
    return api.post('/records', data)
}

export const getRecords = (params) => {
    return api.get('/records', { params })
}

export const getRecordDetail = (id) => {
    return api.get(`/records/${id}`)
}

export const updateRecord = (id, data) => {
    return api.put(`/records/${id}`, data)
}

// 用户相关接口
export const getUserProfile = () => {
    return api.get('/users/profile')
}

export const updateUserProfile = (data) => {
    return api.put('/users/profile', data)
}

export const getUserStatistics = () => {
    return api.get('/users/statistics')
} 