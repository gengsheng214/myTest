<template>
  <view class="container">
    <view class="header">
      <text class="title">登录</text>
    </view>
    <view class="form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input" v-model="username" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <text class="label">密码</text>
        <input class="input" v-model="password" type="password" placeholder="请输入密码" />
      </view>
      <button class="btn" @click="handleLogin">登录</button>
      <view class="link">
        <text @click="goToRegister">还没有账号？去注册</text>
      </view>
    </view>
  </view>
</template>

<script>
import userApi from '../../api/user.js'

export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async handleLogin() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }
      
      try {
        const res = await userApi.login({
          username: this.username,
          password: this.password
        })
        
        if (res.code === 200) {
          this.$store.dispatch('setUserInfo', res.data)
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          setTimeout(() => {
            uni.reLaunch({
              url: '/pages/index/index'
            })
          }, 1500)
        } else {
          uni.showToast({
            title: res.message || '登录失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.showToast({
          title: '登录失败',
          icon: 'none'
        })
      }
    },
    goToRegister() {
      uni.navigateTo({
        url: '/pages/register/register'
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 60rpx 40rpx;
}

.header {
  margin-bottom: 80rpx;
  text-align: center;
}

.title {
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 40rpx;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.label {
  font-size: 28rpx;
  color: #333;
}

.input {
  height: 88rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.btn {
  height: 88rpx;
  background: #007AFF;
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
  margin-top: 20rpx;
}

.link {
  text-align: center;
  margin-top: 20rpx;
}

.link text {
  color: #007AFF;
  font-size: 28rpx;
}
</style>
