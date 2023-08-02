import axios from 'axios'
import store from '@/store'
import { BASE_URL } from './url'

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true
})

api.interceptors.request.use(
  config => {
    store.commit('startSpinner')
    return config
  },
  error => {
    alert('데이터 요청 실패')
    store.commit('endSpinner')
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
  response => {
    store.commit('endSpinner')
    return response
  },
  error => {
    alert('데이터 응답 실패')
    store.commit('endSpinner')
    console.log(error)
    return Promise.reject(error)
  }
)

export default api
