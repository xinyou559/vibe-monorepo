import type { Result } from 'shared-types'

// 本地开发 MVP 使用，需确保真机测试时使用可访问的 IP
const BASE_URL = 'http://localhost:8080/api/v1'

export const request = <T>(url: string, method: 'GET' | 'POST' | 'PUT' | 'DELETE' = 'GET', data?: any) => {
  return new Promise<T>((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
      },
      success: (res: any) => {
        const result = res.data as Result<T>
        if (result.code === 200) {
          resolve(result.data)
        } else {
          uni.showToast({ title: result.message || 'Error', icon: 'none' })
          reject(new Error(result.message))
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络错误', icon: 'none' })
        reject(err)
      }
    })
  })
}