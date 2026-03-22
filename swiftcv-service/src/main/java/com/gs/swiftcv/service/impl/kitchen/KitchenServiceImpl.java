package com.gs.swiftcv.service.impl.kitchen;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gs.swiftcv.repository.entity.Ingredient;
import com.gs.swiftcv.repository.entity.Recipe;
import com.gs.swiftcv.repository.entity.RecipeCategory;
import com.gs.swiftcv.repository.entity.RecipeIngredient;
import com.gs.swiftcv.repository.entity.RecipeStep;
import com.gs.swiftcv.repository.entity.UserIngredient;
import com.gs.swiftcv.repository.mapper.IngredientMapper;
import com.gs.swiftcv.repository.mapper.RecipeCategoryMapper;
import com.gs.swiftcv.repository.mapper.RecipeIngredientMapper;
import com.gs.swiftcv.repository.mapper.RecipeMapper;
import com.gs.swiftcv.repository.mapper.RecipeStepMapper;
import com.gs.swiftcv.repository.mapper.UserIngredientMapper;
import com.gs.swiftcv.service.api.kitchen.KitchenService;
import com.gs.swiftcv.service.api.kitchen.RecipeAiAdvisor;
import com.gs.swiftcv.service.model.FridgeIngredientSaveRequest;
import com.gs.swiftcv.service.model.FridgeIngredientVO;
import com.gs.swiftcv.service.model.KitchenCategoryVO;
import com.gs.swiftcv.service.model.RecipeCardVO;
import com.gs.swiftcv.service.model.RecipeDetailVO;
import com.gs.swiftcv.service.model.RecipeIngredientVO;
import com.gs.swiftcv.service.model.RecipeRecommendationVO;
import com.gs.swiftcv.service.model.RecipeStepVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 厨房业务服务实现。
 */
@Service
public class KitchenServiceImpl implements KitchenService {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 50;
    private static final int DEFAULT_RECOMMEND_LIMIT = 6;

    @Autowired
    private IngredientMapper ingredientMapper;

    @Autowired
    private UserIngredientMapper userIngredientMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private RecipeCategoryMapper recipeCategoryMapper;

    @Autowired
    private RecipeStepMapper recipeStepMapper;

    @Autowired
    private RecipeIngredientMapper recipeIngredientMapper;

    @Autowired
    private RecipeAiAdvisor recipeAiAdvisor;
    /**
     * 查询用户冰箱食材列表。
     *
     * @param userId 用户ID
     * @return 冰箱食材列表
     */
    @Override
    public List<FridgeIngredientVO> listFridgeIngredients(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<UserIngredient> userIngredients = userIngredientMapper.selectList(
                Wrappers.<UserIngredient>lambdaQuery()
                        .eq(UserIngredient::getUserId, userId)
                        .orderByAsc(UserIngredient::getExpiryDate)
                        .orderByDesc(UserIngredient::getCreatedAt)
        );
        if (userIngredients.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Ingredient> ingredientMap = loadIngredientsByIds(
                userIngredients.stream().map(UserIngredient::getIngredientId).collect(Collectors.toSet())
        );
        return userIngredients.stream()
                .map(item -> toFridgeIngredientVo(item, ingredientMap.get(item.getIngredientId())))
                .collect(Collectors.toList());
    }
    /**
     * 新增冰箱食材。
     *
     * @param request 新增请求
     * @return 新增结果
     */
    @Override
    public FridgeIngredientVO addFridgeIngredient(FridgeIngredientSaveRequest request) {
        if (request == null || request.getUserId() == null) {
            throw new IllegalArgumentException("userId不能为空");
        }

        Ingredient ingredient = resolveIngredient(request.getIngredientId(), request.getIngredientName(), request.getCategory(), request.getUnit());

        UserIngredient entity = new UserIngredient();
        entity.setUserId(request.getUserId());
        entity.setIngredientId(ingredient.getIngredientId());
        entity.setQuantity(request.getQuantity());
        entity.setUnit(StringUtils.hasText(request.getUnit()) ? request.getUnit() : ingredient.getUnit());
        entity.setPurchaseDate(request.getPurchaseDate());
        entity.setExpiryDate(request.getExpiryDate());
        entity.setStorageLocation(request.getStorageLocation());
        entity.setNotes(request.getNotes());

        userIngredientMapper.insert(entity);
        return toFridgeIngredientVo(entity, ingredient);
    }
    /**
     * 更新冰箱食材。
     *
     * @param userIngredientId 用户食材记录ID
     * @param request 更新请求
     * @return 更新结果
     */
    @Override
    public FridgeIngredientVO updateFridgeIngredient(Long userIngredientId, FridgeIngredientSaveRequest request) {
        if (userIngredientId == null || request == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        UserIngredient entity = userIngredientMapper.selectById(userIngredientId);
        if (entity == null) {
            throw new IllegalArgumentException("库存记录不存在");
        }
        if (request.getUserId() != null && !Objects.equals(request.getUserId(), entity.getUserId())) {
            throw new IllegalArgumentException("用户信息不匹配");
        }

        Ingredient ingredient = ingredientMapper.selectById(entity.getIngredientId());
        if (request.getIngredientId() != null || StringUtils.hasText(request.getIngredientName())) {
            ingredient = resolveIngredient(request.getIngredientId(), request.getIngredientName(), request.getCategory(), request.getUnit());
            entity.setIngredientId(ingredient.getIngredientId());
        }

        if (request.getQuantity() != null) {
            entity.setQuantity(request.getQuantity());
        }
        if (StringUtils.hasText(request.getUnit())) {
            entity.setUnit(request.getUnit());
        } else if (!StringUtils.hasText(entity.getUnit()) && ingredient != null) {
            entity.setUnit(ingredient.getUnit());
        }
        if (request.getPurchaseDate() != null) {
            entity.setPurchaseDate(request.getPurchaseDate());
        }
        if (request.getExpiryDate() != null) {
            entity.setExpiryDate(request.getExpiryDate());
        }
        if (StringUtils.hasText(request.getStorageLocation())) {
            entity.setStorageLocation(request.getStorageLocation());
        }
        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }

        userIngredientMapper.updateById(entity);
        return toFridgeIngredientVo(entity, ingredient);
    }
    /**
     * 删除冰箱食材。
     *
     * @param userIngredientId 用户食材记录ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFridgeIngredient(Long userIngredientId, Long userId) {
        if (userIngredientId == null) {
            return false;
        }
        if (userId == null) {
            return userIngredientMapper.deleteById(userIngredientId) > 0;
        }
        UserIngredient entity = userIngredientMapper.selectOne(
                Wrappers.<UserIngredient>lambdaQuery()
                        .eq(UserIngredient::getUserIngredientId, userIngredientId)
                        .eq(UserIngredient::getUserId, userId)
        );
        if (entity == null) {
            return false;
        }
        return userIngredientMapper.deleteById(userIngredientId) > 0;
    }
    /**
     * 基于冰箱库存推荐菜谱。
     *
     * @param userId 用户ID
     * @return 推荐结果
     */
    @Override
    public List<RecipeRecommendationVO> recommendByFridge(Long userId) {
        List<FridgeIngredientVO> fridgeIngredients = listFridgeIngredients(userId);
        if (fridgeIngredients.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> availableIngredientNames = fridgeIngredients.stream()
                .map(FridgeIngredientVO::getIngredientName)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        Set<String> availableLowerSet = availableIngredientNames.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<RecipeIngredient> allRecipeIngredients = recipeIngredientMapper.selectList(
                Wrappers.<RecipeIngredient>lambdaQuery().isNotNull(RecipeIngredient::getRecipeId)
        );
        if (allRecipeIngredients.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, List<RecipeIngredient>> recipeIngredientMap = allRecipeIngredients.stream()
                .collect(Collectors.groupingBy(RecipeIngredient::getRecipeId));
        List<Long> recipeIds = new ArrayList<>(recipeIngredientMap.keySet());
        List<Recipe> recipes = recipeMapper.selectBatchIds(recipeIds);
        if (recipes.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, RecipeCategory> categoryMap = loadCategoryMap(
                recipes.stream().map(Recipe::getCategoryId).filter(Objects::nonNull).collect(Collectors.toSet())
        );
        Map<Long, List<RecipeStepVO>> stepMap = loadRecipeStepVoMap(recipeIds);

        List<RecipeRecommendationVO> recommendations = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<RecipeIngredient> recipeIngredients = recipeIngredientMap.get(recipe.getRecipeId());
            if (recipeIngredients == null || recipeIngredients.isEmpty()) {
                continue;
            }

            List<String> requiredNamesLower = recipeIngredients.stream()
                    .map(item -> resolveIngredientName(item, null))
                    .filter(StringUtils::hasText)
                    .map(String::toLowerCase)
                    .distinct()
                    .collect(Collectors.toList());
            if (requiredNamesLower.isEmpty()) {
                continue;
            }

            int matchedCount = 0;
            LinkedHashSet<String> missing = new LinkedHashSet<>();
            for (RecipeIngredient ingredient : recipeIngredients) {
                String ingredientName = resolveIngredientName(ingredient, null);
                if (!StringUtils.hasText(ingredientName)) {
                    continue;
                }
                if (availableLowerSet.contains(ingredientName.toLowerCase())) {
                    matchedCount++;
                } else {
                    missing.add(ingredientName);
                }
            }
            if (matchedCount == 0) {
                continue;
            }

            RecipeRecommendationVO vo = new RecipeRecommendationVO();
            vo.setRecipeId(recipe.getRecipeId());
            vo.setTitle(recipe.getTitle());
            vo.setCoverImage(recipe.getCoverImage());
            vo.setTotalTime(recipe.getTotalTime());

            RecipeCategory category = categoryMap.get(recipe.getCategoryId());
            vo.setCategoryName(category == null ? "未分类" : category.getName());

            BigDecimal matchRate = BigDecimal.valueOf((double) matchedCount / requiredNamesLower.size())
                    .setScale(2, RoundingMode.HALF_UP);
            vo.setMatchRate(matchRate);
            vo.setRecommendationType(missing.isEmpty() ? "完全匹配" : "需补充");
            vo.setMissingIngredients(new ArrayList<>(missing));
            vo.setIngredients(convertRecipeIngredients(recipeIngredients));
            vo.setSteps(stepMap.getOrDefault(recipe.getRecipeId(), Collections.emptyList()));
            vo.setAiSuggestion(recipeAiAdvisor.buildSuggestion(availableIngredientNames, recipe.getTitle(), vo.getMissingIngredients()));
            recommendations.add(vo);
        }

        recommendations.sort(Comparator
                .comparing(RecipeRecommendationVO::getMatchRate, Comparator.nullsLast(BigDecimal::compareTo)).reversed()
                .thenComparing(item -> item.getMissingIngredients() == null ? Integer.MAX_VALUE : item.getMissingIngredients().size())
                .thenComparing(RecipeRecommendationVO::getTotalTime, Comparator.nullsLast(Integer::compareTo))
        );

        if (recommendations.size() > DEFAULT_RECOMMEND_LIMIT) {
            return recommendations.subList(0, DEFAULT_RECOMMEND_LIMIT);
        }
        return recommendations;
    }
    /**
     * 查询首页菜谱。
     *
     * @param limit 返回条数
     * @return 菜谱列表
     */
    @Override
    public List<RecipeCardVO> listHomeRecipes(Integer limit) {
        int safeLimit = normalizeLimit(limit);
        List<Recipe> recipes = recipeMapper.selectList(
                Wrappers.<Recipe>lambdaQuery()
                        .orderByDesc(Recipe::getFavoritesCount)
                        .orderByDesc(Recipe::getAverageRating)
                        .orderByDesc(Recipe::getCreatedAt)
                        .last("LIMIT " + safeLimit)
        );
        if (recipes.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, RecipeCategory> categoryMap = loadCategoryMap(
                recipes.stream().map(Recipe::getCategoryId).filter(Objects::nonNull).collect(Collectors.toSet())
        );
        return recipes.stream().map(recipe -> toRecipeCardVo(recipe, categoryMap.get(recipe.getCategoryId()))).collect(Collectors.toList());
    }
    /**
     * 查询菜谱分类。
     *
     * @return 分类列表
     */
    @Override
    public List<KitchenCategoryVO> listCategories() {
        List<RecipeCategory> categories = recipeCategoryMapper.selectList(
                Wrappers.<RecipeCategory>lambdaQuery()
                        .orderByAsc(RecipeCategory::getSortOrder)
                        .orderByAsc(RecipeCategory::getCategoryId)
        );
        return categories.stream().map(item -> {
            KitchenCategoryVO vo = new KitchenCategoryVO();
            vo.setCategoryId(item.getCategoryId());
            vo.setName(item.getName());
            vo.setDescription(item.getDescription());
            vo.setIconUrl(item.getIconUrl());
            return vo;
        }).collect(Collectors.toList());
    }
    /**
     * 按分类查询菜谱列表。
     *
     * @param categoryId 分类ID
     * @param limit 返回条数
     * @return 菜谱列表
     */
    @Override
    public List<RecipeCardVO> listRecipesByCategory(Long categoryId, Integer limit) {
        int safeLimit = normalizeLimit(limit);
        List<Recipe> recipes = recipeMapper.selectList(
                Wrappers.<Recipe>lambdaQuery()
                        .eq(categoryId != null, Recipe::getCategoryId, categoryId)
                        .orderByDesc(Recipe::getFavoritesCount)
                        .orderByDesc(Recipe::getAverageRating)
                        .orderByDesc(Recipe::getCreatedAt)
                        .last("LIMIT " + safeLimit)
        );
        if (recipes.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, RecipeCategory> categoryMap = loadCategoryMap(
                recipes.stream().map(Recipe::getCategoryId).filter(Objects::nonNull).collect(Collectors.toSet())
        );
        return recipes.stream().map(recipe -> toRecipeCardVo(recipe, categoryMap.get(recipe.getCategoryId()))).collect(Collectors.toList());
    }
    /**
     * 查询菜谱详情。
     *
     * @param recipeId 菜谱ID
     * @return 菜谱详情
     */
    @Override
    public RecipeDetailVO getRecipeDetail(Long recipeId) {
        if (recipeId == null) {
            return null;
        }
        Recipe recipe = recipeMapper.selectById(recipeId);
        if (recipe == null) {
            return null;
        }

        RecipeCategory category = recipe.getCategoryId() == null ? null : recipeCategoryMapper.selectById(recipe.getCategoryId());
        List<RecipeIngredient> recipeIngredients = recipeIngredientMapper.selectList(
                Wrappers.<RecipeIngredient>lambdaQuery().eq(RecipeIngredient::getRecipeId, recipeId)
        );
        List<RecipeStep> recipeSteps = recipeStepMapper.selectList(
                Wrappers.<RecipeStep>lambdaQuery()
                        .eq(RecipeStep::getRecipeId, recipeId)
                        .orderByAsc(RecipeStep::getStepNumber)
        );

        RecipeDetailVO detailVO = new RecipeDetailVO();
        detailVO.setRecipeId(recipe.getRecipeId());
        detailVO.setTitle(recipe.getTitle());
        detailVO.setDescription(recipe.getDescription());
        detailVO.setCategoryId(recipe.getCategoryId());
        detailVO.setCategoryName(category == null ? "未分类" : category.getName());
        detailVO.setDifficulty(recipe.getDifficulty());
        detailVO.setPrepTime(recipe.getPrepTime());
        detailVO.setCookTime(recipe.getCookTime());
        detailVO.setTotalTime(recipe.getTotalTime());
        detailVO.setServings(recipe.getServings());
        detailVO.setCalories(recipe.getCalories());
        detailVO.setCoverImage(recipe.getCoverImage());
        detailVO.setTips(recipe.getTips());
        detailVO.setIngredients(convertRecipeIngredients(recipeIngredients));
        detailVO.setSteps(convertRecipeStepVo(recipeSteps));
        return detailVO;
    }

    /**
     * 规范化分页条数。
     *
     * @param limit 请求条数
     * @return 合法条数
     */
    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }

    /**
     * 解析食材信息，不存在时按名称自动创建。
     *
     * @param ingredientId 食材ID
     * @param ingredientName 食材名称
     * @param category 食材分类
     * @param unit 食材单位
     * @return 食材实体
     */
    private Ingredient resolveIngredient(Long ingredientId, String ingredientName, String category, String unit) {
        if (ingredientId != null) {
            Ingredient ingredient = ingredientMapper.selectById(ingredientId);
            if (ingredient == null) {
                throw new IllegalArgumentException("食材不存在: " + ingredientId);
            }
            return ingredient;
        }

        if (!StringUtils.hasText(ingredientName)) {
            throw new IllegalArgumentException("ingredientId和ingredientName不能同时为空");
        }

        Ingredient existing = ingredientMapper.selectOne(
                Wrappers.<Ingredient>lambdaQuery().eq(Ingredient::getName, ingredientName).last("LIMIT 1")
        );
        if (existing != null) {
            return existing;
        }

        Ingredient created = new Ingredient();
        created.setName(ingredientName);
        created.setCategory(category);
        created.setUnit(unit);
        created.setCommon(Boolean.TRUE);
        ingredientMapper.insert(created);
        return created;
    }

    /**
     * 组装冰箱食材视图对象。
     *
     * @param userIngredient 用户食材实体
     * @param ingredient 食材实体
     * @return 视图对象
     */
    private FridgeIngredientVO toFridgeIngredientVo(UserIngredient userIngredient, Ingredient ingredient) {
        FridgeIngredientVO vo = new FridgeIngredientVO();
        vo.setUserIngredientId(userIngredient.getUserIngredientId());
        vo.setIngredientId(userIngredient.getIngredientId());
        vo.setIngredientName(ingredient == null ? null : ingredient.getName());
        vo.setCategory(ingredient == null ? null : ingredient.getCategory());
        vo.setQuantity(userIngredient.getQuantity());
        vo.setUnit(userIngredient.getUnit());
        vo.setPurchaseDate(userIngredient.getPurchaseDate());
        vo.setExpiryDate(userIngredient.getExpiryDate());
        vo.setStorageLocation(userIngredient.getStorageLocation());
        vo.setNotes(userIngredient.getNotes());
        vo.setIconUrl(ingredient == null ? null : ingredient.getIconUrl());
        return vo;
    }

    /**
     * 组装菜谱卡片视图对象。
     *
     * @param recipe 菜谱实体
     * @param category 分类实体
     * @return 视图对象
     */
    private RecipeCardVO toRecipeCardVo(Recipe recipe, RecipeCategory category) {
        RecipeCardVO vo = new RecipeCardVO();
        vo.setRecipeId(recipe.getRecipeId());
        vo.setTitle(recipe.getTitle());
        vo.setDescription(recipe.getDescription());
        vo.setCategoryId(recipe.getCategoryId());
        vo.setCategoryName(category == null ? "未分类" : category.getName());
        vo.setDifficulty(recipe.getDifficulty());
        vo.setPrepTime(recipe.getPrepTime());
        vo.setCookTime(recipe.getCookTime());
        vo.setTotalTime(recipe.getTotalTime());
        vo.setCoverImage(recipe.getCoverImage());
        return vo;
    }

    /**
     * 批量加载食材并转为映射表。
     *
     * @param ingredientIds 食材ID集合
     * @return 食材映射
     */
    private Map<Long, Ingredient> loadIngredientsByIds(Collection<Long> ingredientIds) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return ingredientMapper.selectBatchIds(ingredientIds).stream()
                .collect(Collectors.toMap(Ingredient::getIngredientId, item -> item, (a, b) -> a));
    }

    /**
     * 批量加载分类并转为映射表。
     *
     * @param categoryIds 分类ID集合
     * @return 分类映射
     */
    private Map<Long, RecipeCategory> loadCategoryMap(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return recipeCategoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(RecipeCategory::getCategoryId, item -> item, (a, b) -> a));
    }

    /**
     * 批量加载菜谱步骤并按菜谱ID分组。
     *
     * @param recipeIds 菜谱ID集合
     * @return 步骤映射
     */
    private Map<Long, List<RecipeStepVO>> loadRecipeStepVoMap(Collection<Long> recipeIds) {
        if (recipeIds == null || recipeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<RecipeStep> steps = recipeStepMapper.selectList(
                Wrappers.<RecipeStep>lambdaQuery()
                        .in(RecipeStep::getRecipeId, recipeIds)
                        .orderByAsc(RecipeStep::getStepNumber)
        );
        Map<Long, List<RecipeStepVO>> result = new HashMap<>();
        for (RecipeStep step : steps) {
            result.computeIfAbsent(step.getRecipeId(), key -> new ArrayList<>()).add(toRecipeStepVo(step));
        }
        return result;
    }

    /**
     * 将步骤实体列表转换为视图列表。
     *
     * @param steps 步骤实体列表
     * @return 视图列表
     */
    private List<RecipeStepVO> convertRecipeStepVo(List<RecipeStep> steps) {
        if (steps == null || steps.isEmpty()) {
            return Collections.emptyList();
        }
        return steps.stream().map(this::toRecipeStepVo).collect(Collectors.toList());
    }

    /**
     * 将步骤实体转换为视图对象。
     *
     * @param step 步骤实体
     * @return 视图对象
     */
    private RecipeStepVO toRecipeStepVo(RecipeStep step) {
        RecipeStepVO vo = new RecipeStepVO();
        vo.setStepNumber(step.getStepNumber());
        vo.setDescription(step.getDescription());
        vo.setImageUrl(step.getImageUrl());
        vo.setTips(step.getTips());
        return vo;
    }

    /**
     * 将菜谱用料实体列表转换为视图列表。
     *
     * @param recipeIngredients 用料实体列表
     * @return 视图列表
     */
    private List<RecipeIngredientVO> convertRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        if (recipeIngredients == null || recipeIngredients.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> ingredientIds = recipeIngredients.stream()
                .map(RecipeIngredient::getIngredientId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Ingredient> ingredientMap = loadIngredientsByIds(ingredientIds);
        return recipeIngredients.stream().map(item -> {
            RecipeIngredientVO vo = new RecipeIngredientVO();
            vo.setIngredientName(resolveIngredientName(item, ingredientMap));
            vo.setQuantity(formatQuantity(item.getQuantity(), item.getUnit()));
            vo.setNotes(item.getNotes());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 解析用料名称。
     *
     * @param item 用料实体
     * @param ingredientMap 食材映射（可为空）
     * @return 食材名称
     */
    private String resolveIngredientName(RecipeIngredient item, Map<Long, Ingredient> ingredientMap) {
        if (StringUtils.hasText(item.getIngredientName())) {
            return item.getIngredientName();
        }
        if (item.getIngredientId() != null && ingredientMap != null) {
            Ingredient ingredient = ingredientMap.get(item.getIngredientId());
            if (ingredient != null) {
                return ingredient.getName();
            }
        }
        if (item.getIngredientId() != null && ingredientMap == null) {
            Ingredient ingredient = ingredientMapper.selectById(item.getIngredientId());
            if (ingredient != null) {
                return ingredient.getName();
            }
        }
        return "未知食材";
    }

    /**
     * 格式化数量与单位显示文案。
     *
     * @param quantity 数量
     * @param unit 单位
     * @return 格式化文案
     */
    private String formatQuantity(BigDecimal quantity, String unit) {
        if (quantity == null) {
            return StringUtils.hasText(unit) ? unit : "适量";
        }
        String value = quantity.stripTrailingZeros().toPlainString();
        if (StringUtils.hasText(unit)) {
            return value + unit;
        }
        return value;
    }
}



