<template>
  <view class="container">
    <view class="card">
      <text class="section-title">我的冰箱</text>

      <view class="form-row">
        <input v-model="form.ingredientName" class="input" placeholder="食材名，如：鸡胸肉" />
      </view>
      <view class="form-row two-col">
        <input v-model="form.quantity" class="input" type="digit" placeholder="数量" />
        <input v-model="form.unit" class="input" placeholder="单位(g/ml/个)" />
      </view>
      <view class="form-row two-col">
        <input v-model="form.category" class="input" placeholder="分类(肉类/蔬菜等)" />
        <input v-model="form.storageLocation" class="input" placeholder="存储位置(冷藏/冷冻)" />
      </view>
      <view class="form-row">
        <input v-model="form.expiryDate" class="input" placeholder="过期日期 YYYY-MM-DD" />
      </view>
      <view class="form-row">
        <input v-model="form.notes" class="input" placeholder="备注(可选)" />
      </view>

      <view class="btn-row">
        <button type="primary" size="mini" @click="submitIngredient">
          {{ form.userIngredientId ? '更新食材' : '添加食材' }}
        </button>
        <button v-if="form.userIngredientId" size="mini" @click="resetForm">取消编辑</button>
      </view>
    </view>

    <view class="card">
      <text class="section-title">库存列表</text>
      <view v-if="fridgeList.length === 0" class="muted">还没有食材，先添加一些吧。</view>
      <view v-for="item in fridgeList" :key="item.userIngredientId" class="list-item">
        <view>
          <text class="item-title">{{ item.ingredientName }}</text>
          <text class="item-desc">{{ item.quantity || '-' }}{{ item.unit || '' }} · {{ item.storageLocation || '未设置位置' }}</text>
          <text class="item-desc">到期：{{ formatDate(item.expiryDate) }}</text>
        </view>
        <view class="item-actions">
          <text class="link" @click="editItem(item)">编辑</text>
          <text class="link danger" @click="removeItem(item)">删除</text>
        </view>
      </view>
    </view>

    <view class="card">
      <view class="title-row">
        <text class="section-title">智能推荐菜谱</text>
        <text class="link" @click="loadRecommendations">刷新</text>
      </view>
      <view v-if="recommendations.length === 0" class="muted">暂无推荐，请先完善冰箱食材。</view>
      <view v-for="item in recommendations" :key="item.recipeId" class="recipe-item" @click="goDetail(item.recipeId)">
        <text class="item-title">{{ item.title }}（匹配度 {{ toPercent(item.matchRate) }}）</text>
        <text class="item-desc">分类：{{ item.categoryName }} · 总用时：{{ item.totalTime || '-' }} 分钟</text>
        <text class="item-desc">建议：{{ item.aiSuggestion }}</text>
        <text v-if="item.missingIngredients && item.missingIngredients.length" class="item-desc">缺少：{{ item.missingIngredients.join('、') }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import kitchenApi from '../../api/kitchen.js'

export default {
  data() {
    return {
      userId: 1,
      fridgeList: [],
      recommendations: [],
      form: this.newForm()
    }
  },
  onShow() {
    const storeUserId = this.$store.getters.userId
    this.userId = storeUserId || 1
    this.loadFridge()
    this.loadRecommendations()
  },
  methods: {
    newForm() {
      return {
        userIngredientId: null,
        ingredientName: '',
        quantity: '',
        unit: '',
        category: '',
        storageLocation: '',
        expiryDate: '',
        notes: ''
      }
    },
    async loadFridge() {
      const res = await kitchenApi.listFridge(this.userId)
      this.fridgeList = res.data || []
    },
    async loadRecommendations() {
      const res = await kitchenApi.fridgeRecommendations(this.userId)
      this.recommendations = res.data || []
    },
    async submitIngredient() {
      if (!this.form.ingredientName) {
        uni.showToast({ title: '请输入食材名', icon: 'none' })
        return
      }
      const payload = {
        userId: this.userId,
        ingredientName: this.form.ingredientName,
        category: this.form.category,
        quantity: this.form.quantity ? Number(this.form.quantity) : null,
        unit: this.form.unit,
        storageLocation: this.form.storageLocation,
        expiryDate: this.form.expiryDate || null,
        notes: this.form.notes
      }
      const res = this.form.userIngredientId
        ? await kitchenApi.updateFridge(this.form.userIngredientId, payload)
        : await kitchenApi.addFridge(payload)

      if (res.code !== 200) {
        uni.showToast({ title: res.message || '保存失败', icon: 'none' })
        return
      }
      uni.showToast({ title: '保存成功', icon: 'success' })
      this.resetForm()
      this.loadFridge()
      this.loadRecommendations()
    },
    editItem(item) {
      this.form = {
        userIngredientId: item.userIngredientId,
        ingredientName: item.ingredientName || '',
        quantity: item.quantity || '',
        unit: item.unit || '',
        category: item.category || '',
        storageLocation: item.storageLocation || '',
        expiryDate: this.formatDate(item.expiryDate),
        notes: item.notes || ''
      }
    },
    resetForm() {
      this.form = this.newForm()
    },
    removeItem(item) {
      uni.showModal({
        title: '删除确认',
        content: `确认删除 ${item.ingredientName} 吗？`,
        success: async ({ confirm }) => {
          if (!confirm) return
          const res = await kitchenApi.deleteFridge(item.userIngredientId, this.userId)
          if (res.code === 200) {
            uni.showToast({ title: '删除成功', icon: 'success' })
            this.loadFridge()
            this.loadRecommendations()
          } else {
            uni.showToast({ title: res.message || '删除失败', icon: 'none' })
          }
        }
      })
    },
    goDetail(recipeId) {
      uni.navigateTo({ url: `/pages/recipe/detail?recipeId=${recipeId}` })
    },
    toPercent(rate) {
      if (rate === null || rate === undefined) return '0%'
      return `${Math.round(Number(rate) * 100)}%`
    },
    formatDate(value) {
      if (!value) return '-'
      return String(value).slice(0, 10)
    }
  }
}
</script>

<style scoped>
.form-row {
  margin-bottom: 16rpx;
}

.two-col {
  display: flex;
  gap: 12rpx;
}

.input {
  flex: 1;
  height: 72rpx;
  background: #f7f8fa;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 26rpx;
}

.btn-row {
  display: flex;
  gap: 16rpx;
  margin-top: 10rpx;
}

.list-item,
.recipe-item {
  display: flex;
  justify-content: space-between;
  border-bottom: 1rpx solid #f0f0f0;
  padding: 16rpx 0;
}

.item-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #222;
}

.item-desc {
  display: block;
  margin-top: 8rpx;
  font-size: 24rpx;
  color: #777;
}

.item-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
  gap: 10rpx;
}

.link {
  color: #2f7ef7;
  font-size: 24rpx;
}

.danger {
  color: #ef4444;
}

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>