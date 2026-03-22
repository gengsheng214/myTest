<template>
  <view class="container">
    <view class="hero card">
      <text class="title">我的厨房</text>
      <text class="subtitle">冰箱管理 + 家常菜推荐 + 菜谱分类</text>
    </view>

    <view class="card entry" @click="go('/pages/fridge/index')">
      <text class="entry-title">我的冰箱</text>
      <text class="entry-desc">添加/修改/删除食材，并基于库存智能推荐菜谱</text>
    </view>

    <view class="card entry" @click="go('/pages/recommend/index')">
      <text class="entry-title">家常菜推荐</text>
      <text class="entry-desc">从数据库拉取热门家常菜，查看做法与步骤图</text>
    </view>

    <view class="card entry" @click="go('/pages/category/index')">
      <text class="entry-title">菜谱分类</text>
      <text class="entry-desc">按烘焙、甜品、肉类、蔬果、水产等分类查看</text>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    go(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style scoped>
.hero {
  margin-top: 24rpx;
}

.title {
  display: block;
  font-size: 44rpx;
  font-weight: 700;
  color: #1f1f1f;
}

.subtitle {
  display: block;
  margin-top: 12rpx;
  font-size: 26rpx;
  color: #666;
}

.entry-title {
  display: block;
  font-size: 34rpx;
  font-weight: 600;
  color: #202020;
}

.entry-desc {
  display: block;
  margin-top: 10rpx;
  font-size: 26rpx;
  color: #7a7a7a;
  line-height: 1.6;
}
</style>