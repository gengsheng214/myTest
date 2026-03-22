package com.gs.swiftcv.web.controller.kitchen;

import com.gs.swiftcv.core.common.Result;
import com.gs.swiftcv.service.api.kitchen.KitchenService;
import com.gs.swiftcv.service.model.FridgeIngredientSaveRequest;
import com.gs.swiftcv.service.model.FridgeIngredientVO;
import com.gs.swiftcv.service.model.KitchenCategoryVO;
import com.gs.swiftcv.service.model.RecipeCardVO;
import com.gs.swiftcv.service.model.RecipeDetailVO;
import com.gs.swiftcv.service.model.RecipeRecommendationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 厨房业务控制器。
 */
@RestController
@RequestMapping("/kitchen")
public class KitchenController {

    @Autowired
    private KitchenService kitchenService;

    /**
     * 查询指定用户的冰箱库存。
     *
     * @param userId 用户ID
     * @return 冰箱食材列表
     */
    @GetMapping("/fridge")
    public Result<List<FridgeIngredientVO>> listFridge(@RequestParam Long userId) {
        return Result.success(kitchenService.listFridgeIngredients(userId));
    }

    /**
     * 新增冰箱食材。
     *
     * @param request 新增请求
     * @return 新增后的食材
     */
    @PostMapping("/fridge")
    public Result<FridgeIngredientVO> addFridge(@RequestBody FridgeIngredientSaveRequest request) {
        try {
            return Result.success(kitchenService.addFridgeIngredient(request));
        } catch (IllegalArgumentException ex) {
            return Result.fail(ex.getMessage());
        }
    }

    /**
     * 更新冰箱食材。
     *
     * @param userIngredientId 用户食材记录ID
     * @param request 更新请求
     * @return 更新后的食材
     */
    @PutMapping("/fridge/{userIngredientId}")
    public Result<FridgeIngredientVO> updateFridge(@PathVariable Long userIngredientId,
                                                   @RequestBody FridgeIngredientSaveRequest request) {
        try {
            return Result.success(kitchenService.updateFridgeIngredient(userIngredientId, request));
        } catch (IllegalArgumentException ex) {
            return Result.fail(ex.getMessage());
        }
    }

    /**
     * 删除冰箱食材。
     *
     * @param userIngredientId 用户食材记录ID
     * @param userId 用户ID（可选）
     * @return 操作结果
     */
    @DeleteMapping("/fridge/{userIngredientId}")
    public Result<Void> deleteFridge(@PathVariable Long userIngredientId,
                                     @RequestParam(required = false) Long userId) {
        boolean deleted = kitchenService.deleteFridgeIngredient(userIngredientId, userId);
        if (deleted) {
            return Result.success();
        }
        return Result.fail("删除失败或记录不存在");
    }

    /**
     * 基于冰箱库存推荐菜谱。
     *
     * @param userId 用户ID
     * @return 推荐菜谱列表
     */
    @GetMapping("/fridge/recommendations")
    public Result<List<RecipeRecommendationVO>> recommendByFridge(@RequestParam Long userId) {
        return Result.success(kitchenService.recommendByFridge(userId));
    }

    /**
     * 查询首页菜谱列表。
     *
     * @param limit 返回条数
     * @return 菜谱卡片列表
     */
    @GetMapping("/home-recipes")
    public Result<List<RecipeCardVO>> listHomeRecipes(@RequestParam(required = false) Integer limit) {
        return Result.success(kitchenService.listHomeRecipes(limit));
    }

    /**
     * 查询菜谱分类列表。
     *
     * @return 分类列表
     */
    @GetMapping("/categories")
    public Result<List<KitchenCategoryVO>> listCategories() {
        return Result.success(kitchenService.listCategories());
    }

    /**
     * 按分类查询菜谱列表。
     *
     * @param categoryId 分类ID
     * @param limit 返回条数
     * @return 菜谱卡片列表
     */
    @GetMapping("/recipes")
    public Result<List<RecipeCardVO>> listRecipesByCategory(@RequestParam(required = false) Long categoryId,
                                                            @RequestParam(required = false) Integer limit) {
        return Result.success(kitchenService.listRecipesByCategory(categoryId, limit));
    }

    /**
     * 查询菜谱详情。
     *
     * @param recipeId 菜谱ID
     * @return 菜谱详情
     */
    @GetMapping("/recipes/{recipeId}")
    public Result<RecipeDetailVO> recipeDetail(@PathVariable Long recipeId) {
        RecipeDetailVO detail = kitchenService.getRecipeDetail(recipeId);
        if (detail == null) {
            return Result.fail("菜谱不存在");
        }
        return Result.success(detail);
    }
}
