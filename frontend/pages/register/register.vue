<template>
  <view class="container">
    <view class="header">
      <text class="title">注册</text>
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
      <view class="form-item">
        <text class="label">确认密码</text>
        <input class="input" v-model="confirmPassword" type="password" placeholder="请再次输入密码" />
      </view>
      <view class="form-item">
        <text class="label">邮箱</text>
        <input class="input" v-model="email" placeholder="请输入邮箱" />
      </view>
      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input" v-model="phone" placeholder="请输入手机号" />
      </view>
      <view class="form-item">
        <text class="label">真实姓名</text>
        <input class="input" v-model="realName" placeholder="请输入真实姓名" />
      </view>
      <button class="btn" @click="handleRegister">注册</button>
      <view class="link">
        <text @click="goToLogin">已有账号？去登录</text>
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
      password: '',
      confirmPassword: '',
      email: '',
      phone: '',
      realName: ''
    }
  },
  methods: {
    async handleRegister() {
      if (!this.username || !this.password || !this.email || !this.phone || !this.realName) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      
      if (this.password !== this.confirmPassword) {
        uni.showToast({
          title: '两次密码不一致',
          icon: 'none'
        })
        return
      }
      
      try {
        const res = await userApi.register({
          username: this.username,
          password: this.password,
          email: this.email,
          phone: this.phone,
          realName: this.realName
        })
        
        if (res.code === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          })
          setTimeout(() => {
            uni.navigateTo({
              url: '/pages/login/login'
            })
          }, 1500)
        } else {
          uni.showToast({
            title: res.message || '注册失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.showToast({
          title: '注册失败',
          icon: 'none'
        })
      }
    },
    goToLogin() {
      uni.navigateTo({
        url: '/pages/login/login'
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
  gap: 30rpx;
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