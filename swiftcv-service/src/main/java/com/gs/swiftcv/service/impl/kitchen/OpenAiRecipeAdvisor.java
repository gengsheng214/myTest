package com.gs.swiftcv.service.impl.kitchen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.swiftcv.service.api.kitchen.RecipeAiAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 OpenAI 接口生成菜谱补料建议。
 */
@Service
public class OpenAiRecipeAdvisor implements RecipeAiAdvisor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kitchen.ai.enabled:false}")
    private boolean enabled;

    @Value("${kitchen.ai.api-key:}")
    private String apiKey;

    @Value("${kitchen.ai.endpoint:https://api.openai.com/v1/chat/completions}")
    private String endpoint;

    @Value("${kitchen.ai.model:gpt-4o-mini}")
    private String model;

    @Value("${kitchen.ai.timeout-ms:8000}")
    private Integer timeoutMs;

    /**
     * 根据食材匹配结果生成建议文案。
     *
     * @param userIngredients 用户现有食材
     * @param recipeTitle 菜谱标题
     * @param missingIngredients 缺失食材
     * @return 建议文案
     */
    @Override
    public String buildSuggestion(List<String> userIngredients, String recipeTitle, List<String> missingIngredients) {
        if (!enabled || !StringUtils.hasText(apiKey)) {
            return buildFallbackSuggestion(missingIngredients);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(timeoutMs);
            connection.setReadTimeout(timeoutMs);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model);
            payload.put("temperature", 0.3);
            payload.put("max_tokens", 180);
            payload.put("messages", buildMessages(userIngredients, recipeTitle, missingIngredients));

            byte[] body = objectMapper.writeValueAsBytes(payload);
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body);
            }

            int responseCode = connection.getResponseCode();
            InputStream stream = responseCode >= 200 && responseCode < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            if (stream == null) {
                return buildFallbackSuggestion(missingIngredients);
            }

            String response;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                response = builder.toString();
            }

            JsonNode contentNode = objectMapper.readTree(response)
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content");
            String content = contentNode.asText();
            if (!StringUtils.hasText(content)) {
                return buildFallbackSuggestion(missingIngredients);
            }
            return content.trim();
        } catch (Exception ex) {
            return buildFallbackSuggestion(missingIngredients);
        }
    }

    /**
     * 构建对话消息体。
     *
     * @param userIngredients 用户现有食材
     * @param recipeTitle 菜谱标题
     * @param missingIngredients 缺失食材
     * @return 请求消息
     */
    private Object buildMessages(List<String> userIngredients, String recipeTitle, List<String> missingIngredients) {
        Map<String, String> system = new HashMap<>();
        system.put("role", "system");
        system.put("content", "你是中餐家常菜助手，回答要简洁、可执行，且包含具体调味克数。");

        Map<String, String> user = new HashMap<>();
        user.put("role", "user");
        user.put("content", String.format(
                "现有食材：%s；目标菜品：%s；缺少食材：%s。请输出1句建议，包含至少2种调味料和大致克数，例如酱油10g、白糖5g。",
                String.join("、", userIngredients),
                recipeTitle,
                missingIngredients.isEmpty() ? "无" : String.join("、", missingIngredients)
        ));

        return java.util.Arrays.asList(system, user);
    }

    /**
     * 生成兜底建议。
     *
     * @param missingIngredients 缺失食材
     * @return 兜底文案
     */
    private String buildFallbackSuggestion(List<String> missingIngredients) {
        if (missingIngredients == null || missingIngredients.isEmpty()) {
            return "食材齐全，建议先腌制主料，再用酱油10g、白糖5g、盐2g调味。";
        }
        return "建议先补充：" + String.join("、", missingIngredients)
                + "；现有食材可先预处理，调味可参考酱油10g、白糖5g。";
    }
}
