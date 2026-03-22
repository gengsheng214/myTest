import request from '../http/request.js'

export default {
  login(data) {
    return request.post('/user/login', data)
  },
  register(data) {
    return request.post('/user/register', data)
  },
  getUserInfo(data) {
    return request.get('/user/info', data)
  }
}