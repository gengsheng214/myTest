const BASE_URL = 'http://127.0.0.1:8989/api'

function request(options) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(res)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

export default {
  get(url, data, header) {
    return request({
      url,
      method: 'GET',
      data,
      header
    })
  },
  post(url, data, header) {
    return request({
      url,
      method: 'POST',
      data,
      header
    })
  },
  put(url, data, header) {
    return request({
      url,
      method: 'PUT',
      data,
      header
    })
  },
  delete(url, data, header) {
    return request({
      url,
      method: 'DELETE',
      data,
      header
    })
  }
}
