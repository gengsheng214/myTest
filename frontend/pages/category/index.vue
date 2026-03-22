<template>
  <view class="container">
    <view class="card">
      <text class="section-title">菜谱分类</text>
      <scroll-view scroll-x class="category-scroll" show-scrollbar="false">
        <view class="category-row">
          <view
            v-for="cat in categories"
            :key="cat.categoryId"
            class="chip"
            :class="{ active: selectedCategoryId === cat.categoryId }"
            @click="selectCategory(cat.categoryId)"
          >
            {{ cat.name }}
          </view>
        </view>
      </scroll-view>
    </view>

    <view v-if="recipes.length === 0" class="card muted">该分类暂无菜谱</view>

    <view v-for="item in recipes" :key="item.recipeId" class="card recipe-item" @click="goDetail(item.recipeId)">
      <text class="name">{{ item.title }}</text>
      <text class="desc">{{ item.description || '暂无介绍' }}</text>
      <text class="meta">总用时：{{ item.totalTime || 0 }} 分钟</text>
    </view>
  </view>
</template>

<script>
import kitchenApi from '../../api/kitchen.js'

export default {
  data() {
    return {
      categories: [],
      selectedCategoryId: null,
      recipes: []
    }
  },
  onShow() {
    this.loadCategories()
  },
  methods: {
    async loadCategories() {
      const res = await kitchenApi.categories()
      this.categories = res.data || []
      if (this.categories.length && !this.selectedCategoryId) {
        this.selectedCategoryId = this.categories[0].categoryId
      }
      this.loadRecipes()
    },
    async loadRecipes() {
      const res = await kitchenApi.recipesByCategory(this.selectedCategoryId, 30)
      this.recipes = res.data || []
    },
    selectCategory(categoryId) {
      this.selectedCategoryId = categoryId
      this.loadRecipes()
    },
    goDetail(recipeId) {
      uni.navigateTo({ url: `/pages/recipe/detail?recipeId=${recipeId}` })
    }
  }
}
</script>

<style scoped>
.category-scroll {
  white-space: nowrap;
}

.category-row {
  display: flex;
  gap: 16rpx;
}

.chip {
  padding: 10rpx 20rpx;
  border-radius: 999rpx;
  background: #f2f4f7;
  color: #666;
  font-size: 24rpx;
}

.chip.active {
  background: #1f7aff;
  color: #fff;
}

.recipe-item {
  margin-top: 16rpx;
}

.name {
  display: block;
  font-size: 30rpx;
  font-weight: 600;
  color: #222;
}

.desc {
  display: block;
  margin-top: 8rpx;
  font-size: 25rpx;
  color: #666;
}

.meta {
  display: block;
  margin-top: 8rpx;
  font-size: 24rpx;
  color: #888;
}
</style>