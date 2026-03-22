package com.gs.swiftcv.service.api.kitchen;

import java.util.List;

/**
 * 菜谱AI建议服务接口。
 */
public interface RecipeAiAdvisor {

    /**
     * 根据现有食材和缺失食材生成烹饪建议。
     *
     * @param userIngredients 用户现有食材
     * @param recipeTitle 菜谱标题
     * @param missingIngredients 缺失食材
     * @return 建议文案
     */
    String buildSuggestion(List<String> userIngredients, String recipeTitle, List<String> missingIngredients);
}
