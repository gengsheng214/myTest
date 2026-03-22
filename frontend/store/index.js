import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    userInfo: uni.getStorageSync('userInfo') || null,
    token: uni.getStorageSync('token') || '',
    userId: uni.getStorageSync('userId') || 1
  },
  mutations: {
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      uni.setStorageSync('userInfo', userInfo)
    },
    SET_TOKEN(state, token) {
      state.token = token
      uni.setStorageSync('token', token)
    },
    SET_USER_ID(state, userId) {
      state.userId = userId
      uni.setStorageSync('userId', userId)
    },
    CLEAR_USER_INFO(state) {
      state.userInfo = null
      uni.removeStorageSync('userInfo')
    },
    CLEAR_TOKEN(state) {
      state.token = ''
      uni.removeStorageSync('token')
    }
  },
  actions: {
    setUserInfo({ commit }, userInfo) {
      commit('SET_USER_INFO', userInfo)
    },
    setToken({ commit }, token) {
      commit('SET_TOKEN', token)
    },
    setUserId({ commit }, userId) {
      commit('SET_USER_ID', userId)
    },
    logout({ commit }) {
      commit('CLEAR_USER_INFO')
      commit('CLEAR_TOKEN')
    }
  },
  getters: {
    userInfo: state => state.userInfo,
    token: state => state.token,
    userId: state => state.userId
  }
})

export default store
