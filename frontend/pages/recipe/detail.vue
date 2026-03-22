<template>
  <view class="container">
    <view v-if="loading" class="card muted">加载中...</view>

    <view v-else-if="!detail" class="card muted">菜谱不存在或已被删除</view>

    <template v-else>
      <view class="card">
        <text class="title">{{ detail.title }}</text>
        <text class="meta">分类：{{ detail.categoryName || '未分类' }} · 难度：{{ detail.difficulty || '简单' }}</text>
        <text class="meta">
          准备 {{ detail.prepTime || 0 }} 分钟 · 烹饪 {{ detail.cookTime || 0 }} 分钟 · 总用时 {{ detail.totalTime || 0 }} 分钟
        </text>
        <text class="meta">份量：{{ detail.servings || '-' }} 人 · 热量：{{ detail.calories || '-' }} kcal</text>
        <text class="desc">{{ detail.description || '暂无介绍' }}</text>
      </view>

      <view class="card">
        <text class="section-title">食材清单</text>
        <view v-if="!detail.ingredients || detail.ingredients.length === 0" class="muted">暂无食材信息</view>
        <view v-for="(item, index) in detail.ingredients" :key="index" class="ingredient-row">
          <text class="ingredient-name">{{ item.ingredientName || '未命名食材' }}</text>
          <text class="ingredient-qty">{{ item.quantity || '适量' }}</text>
        </view>
      </view>

      <view class="card">
        <text class="section-title">烹饪步骤</text>
        <view v-if="!detail.steps || detail.steps.length === 0" class="muted">暂无步骤信息</view>
        <view v-for="(step, index) in detail.steps" :key="index" class="step-item">
          <text class="step-title">步骤 {{ step.stepNumber || index + 1 }}</text>
          <text class="step-desc">{{ step.description || '暂无描述' }}</text>
          <text v-if="step.tips" class="step-tip">小贴士：{{ step.tips }}</text>
        </view>
      </view>

      <view v-if="detail.tips" class="card tips-card">
        <text class="section-title">整体贴士</text>
        <text class="desc">{{ detail.tips }}</text>
      </view>
    </template>
  </view>
</template>

<script>
import kitchenApi from '../../api/kitchen.js'

export default {
  data() {
    return {
      recipeId: null,
      loading: false,
      detail: null
    }
  },
  onLoad(query) {
    this.recipeId = query && query.recipeId ? Number(query.recipeId) : null
    this.loadDetail()
  },
  methods: {
    async loadDetail() {
      if (!this.recipeId) {
        uni.showToast({ title: '参数错误', icon: 'none' })
        return
      }
      this.loading = true
      try {
        const res = await kitchenApi.recipeDetail(this.recipeId)
        if (res.code !== 200) {
          uni.showToast({ title: res.message || '加载失败', icon: 'none' })
          this.detail = null
          return
        }
        this.detail = res.data || null
      } catch (e) {
        this.detail = null
        uni.showToast({ title: '网络异常', icon: 'none' })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.title {
  display: block;
  font-size: 38rpx;
  font-weight: 700;
  color: #1e1e1e;
}

.meta {
  display: block;
  margin-top: 10rpx;
  font-size: 24rpx;
  color: #767676;
  line-height: 1.5;
}

.desc {
  display: block;
  margin-top: 14rpx;
  font-size: 27rpx;
  color: #3d3d3d;
  line-height: 1.65;
}

.ingredient-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.ingredient-name {
  font-size: 27rpx;
  color: #222;
}

.ingredient-qty {
  font-size: 25rpx;
  color: #666;
}

.step-item {
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.step-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #1f1f1f;
}

.step-desc {
  display: block;
  margin-top: 10rpx;
  font-size: 26rpx;
  color: #505050;
  line-height: 1.65;
}

.step-tip {
  display: block;
  margin-top: 8rpx;
  font-size: 24rpx;
  color: #888;
}

.tips-card {
  margin-bottom: 28rpx;
}
</style>
