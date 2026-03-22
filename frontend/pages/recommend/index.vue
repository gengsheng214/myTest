<template>
  <view class="container">
    <view class="card">
      <text class="section-title">家常菜推荐</text>
      <text class="muted">来自数据库的菜谱列表，支持查看介绍、用料、步骤图、用时信息。</text>
    </view>

    <view v-if="recipes.length === 0" class="card muted">暂无数据</view>

    <view v-for="item in recipes" :key="item.recipeId" class="card recipe-item" @click="goDetail(item.recipeId)">
      <text class="name">{{ item.title }}</text>
      <text class="desc">{{ item.description || '暂无介绍' }}</text>
      <text class="meta">分类：{{ item.categoryName }} · 难度：{{ item.difficulty || '简单' }}</text>
      <text class="meta">准备 {{ item.prepTime || 0 }} 分钟 · 烹饪 {{ item.cookTime || 0 }} 分钟 · 总用时 {{ item.totalTime || 0 }} 分钟</text>
    </view>
  </view>
</template>

<script>
import kitchenApi from '../../api/kitchen.js'

export default {
  data() {
    return {
      recipes: []
    }
  },
  onShow() {
    this.loadRecipes()
  },
  methods: {
    async loadRecipes() {
      const res = await kitchenApi.homeRecipes(30)
      this.recipes = res.data || []
    },
    goDetail(recipeId) {
      uni.navigateTo({ url: `/pages/recipe/detail?recipeId=${recipeId}` })
    }
  }
}
</script>

<style scoped>
.recipe-item {
  margin-top: 16rpx;
}

.name {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #202020;
}

.desc {
  display: block;
  margin-top: 12rpx;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.meta {
  display: block;
  margin-top: 8rpx;
  font-size: 24rpx;
  color: #868686;
}
</style>