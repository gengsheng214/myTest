package com.gs.swiftcv.service.api.kitchen;

import com.gs.swiftcv.service.model.FridgeIngredientSaveRequest;
import com.gs.swiftcv.service.model.FridgeIngredientVO;
import com.gs.swiftcv.service.model.KitchenCategoryVO;
import com.gs.swiftcv.service.model.RecipeCardVO;
import com.gs.swiftcv.service.model.RecipeDetailVO;
import com.gs.swiftcv.service.model.RecipeRecommendationVO;

import java.util.List;

/**
 * 厨房业务服务接口。
 */
public interface KitchenService {

    /**
     * 查询用户冰箱食材列表。
     *
     * @param userId 用户ID
     * @return 食材列表
     */
    List<FridgeIngredientVO> listFridgeIngredients(Long userId);

    /**
     * 新增冰箱食材。
     *
     * @param request 新增请求
     * @return 新增结果
     */
    FridgeIngredientVO addFridgeIngredient(FridgeIngredientSaveRequest request);

    /**
     * 更新冰箱食材。
     *
     * @param userIngredientId 用户食材记录ID
     * @param request 更新请求
     * @return 更新结果
     */
    FridgeIngredientVO updateFridgeIngredient(Long userIngredientId, FridgeIngredientSaveRequest request);

    /**
     * 删除冰箱食材。
     *
     * @param userIngredientId 用户食材记录ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteFridgeIngredient(Long userIngredientId, Long userId);

    /**
     * 根据冰箱库存推荐菜谱。
     *
     * @param userId 用户ID
     * @return 推荐结果
     */
    List<RecipeRecommendationVO> recommendByFridge(Long userId);

    /**
     * 查询首页菜谱。
     *
     * @param limit 返回条数
     * @return 菜谱列表
     */
    List<RecipeCardVO> listHomeRecipes(Integer limit);

    /**
     * 查询菜谱分类。
     *
     * @return 分类列表
     */
    List<KitchenCategoryVO> listCategories();

    /**
     * 按分类查询菜谱。
     *
     * @param categoryId 分类ID
     * @param limit 返回条数
     * @return 菜谱列表
     */
    List<RecipeCardVO> listRecipesByCategory(Long categoryId, Integer limit);

    /**
     * 查询菜谱详情。
     *
     * @param recipeId 菜谱ID
     * @return 菜谱详情
     */
    RecipeDetailVO getRecipeDetail(Long recipeId);
}
