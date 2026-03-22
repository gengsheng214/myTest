import request from '../http/request.js'

export default {
  listFridge(userId) {
    return request.get('/kitchen/fridge', { userId })
  },
  addFridge(data) {
    return request.post('/kitchen/fridge', data)
  },
  updateFridge(userIngredientId, data) {
    return request.put(`/kitchen/fridge/${userIngredientId}`, data)
  },
  deleteFridge(userIngredientId, userId) {
    return request.delete(`/kitchen/fridge/${userIngredientId}`, { userId })
  },
  fridgeRecommendations(userId) {
    return request.get('/kitchen/fridge/recommendations', { userId })
  },
  homeRecipes(limit = 20) {
    return request.get('/kitchen/home-recipes', { limit })
  },
  categories() {
    return request.get('/kitchen/categories')
  },
  recipesByCategory(categoryId, limit = 20) {
    return request.get('/kitchen/recipes', { categoryId, limit })
  },
  recipeDetail(recipeId) {
    return request.get(`/kitchen/recipes/${recipeId}`)
  }
}