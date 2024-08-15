import axios from 'axios'

const instance = axios.create({
    // baseURL: 'http://8.134.56.250:8081'
    baseURL: 'http://8.134.56.250/api'
    // baseURL: 'http://localhost:8081/api'
})

// 拦截器：在获取数据之前
instance.interceptors.response.use(
    // 请求成功
    response => {
        return response.data
    },
    // 请求失败
    error => {
        return error.response
    }
)

export default instance