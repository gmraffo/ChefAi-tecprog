package com.cheffai.llm;

import com.cheffai.model.Ingredient;
import com.cheffai.model.Recipe;
import com.cheffai.model.Recipe.RecipeIngredient;
import com.cheffai.util.PromptBuilder;
import com.cheffai.exception.APIException;
import com.google.gson.*;

import okhttp3.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Minimal LLM client. Reads api.url and api.key from config.properties (must exist outside source control).
 * Expects the LLM to return a JSON array of Recipe objects with a fixed schema (see PromptBuilder).
 *
 * NOTE: For unit tests / offline development, you can mock requestRecipes to return canned recipes.
 */
public class LLMClient {

    private final String apiKey;
    private final String apiUrl;
    private final String provider;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    public LLMClient() throws Exception {
        Properties props = new Properties();
        // load config.properties from project root if exists
        try (InputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new APIException("Could not load config.properties. Create it from config.properties.example", e);
        }
        apiKey = props.getProperty("api.key");
        apiUrl = props.getProperty("api.url");
        provider = props.getProperty("api.provider", "openai");
        if (apiKey == null || apiKey.isBlank()) {
            throw new APIException("api.key not set in config.properties");
        }
    }

    /**
     * Requests recipes from the LLM. If your LLM provider or schema differs, adjust PromptBuilder and parsing.
     * @param pantry user's ingredients
     * @param dietaryConstraints list of restrictions
     * @param maxTimeMinutes max minutes per recipe
     * @param howMany number of recipes to request
     * @param flavor strategy hint ("quick", "healthy", etc.)
     * @return list of Recipe domain objects
     */
    public List<Recipe> requestRecipes(List<Ingredient> pantry, List<String> dietaryConstraints, int maxTimeMinutes, int howMany, String flavor) throws Exception {
        String prompt = PromptBuilder.buildPrompt(pantry, dietaryConstraints, maxTimeMinutes, howMany, flavor);

        // For OpenAI chat completions endpoint
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", "gpt-4o-mini"); // example, adjust as needed
        JsonArray messages = new JsonArray();
        JsonObject system = new JsonObject();
        system.addProperty("role", "system");
        system.addProperty("content", "You are a helpful assistant that outputs valid JSON as specified.");
        JsonObject user = new JsonObject();
        user.addProperty("role", "user");
        user.addProperty("content", prompt);
        messages.add(system);
        messages.add(user);
        requestJson.add("messages", messages);
        requestJson.addProperty("max_tokens", 800);
        requestJson.addProperty("temperature", 0.2);

        RequestBody body = RequestBody.create(requestJson.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new APIException("LLM API request failed: " + response.code() + " - " + response.message());
            }
            String responseBody = response.body().string();
            // For OpenAI-like response, extract content text
            JsonObject obj = JsonParser.parseString(responseBody).getAsJsonObject();
            String content = extractContentFromLLMResponse(obj);
            return parseRecipesFromJson(content);
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            throw new APIException("Error calling LLM", e);
        }
    }

    private String extractContentFromLLMResponse(JsonObject obj) {
        // Try to handle OpenAI chat completion structure: choices[0].message.content
        try {
            JsonArray choices = obj.getAsJsonArray("choices");
            if (choices != null && choices.size() > 0) {
                JsonObject first = choices.get(0).getAsJsonObject();
                if (first.has("message")) {
                    return first.getAsJsonObject("message").get("content").getAsString();
                } else if (first.has("text")) {
                    return first.get("text").getAsString();
                }
            }
        } catch (Exception ignored) {}
        // fallback to raw body string
        return obj.toString();
    }

    private List<Recipe> parseRecipesFromJson(String jsonText) throws Exception {
        // If the model returned JSON with code blocks or extra text, try to extract first JSON array found.
        String json = extractJsonArray(jsonText);
        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        List<Recipe> recipes = new ArrayList<>();
        for (JsonElement e : arr) {
            JsonObject r = e.getAsJsonObject();
            String name = r.get("name").getAsString();
            int time = r.has("time_minutes") ? r.get("time_minutes").getAsInt() : 0;
            List<RecipeIngredient> ingredients = new ArrayList<>();
            JsonArray ingr = r.getAsJsonArray("ingredients");
            for (JsonElement ie : ingr) {
                JsonObject io = ie.getAsJsonObject();
                String iname = io.get("name").getAsString();
                String amount = io.has("amount") ? io.get("amount").getAsString() : "";
                boolean has = io.has("hasIngredient") && io.get("hasIngredient").getAsBoolean();
                ingredients.add(new RecipeIngredient(iname, amount, has));
            }
            List<String> steps = new ArrayList<>();
            JsonArray stepsArr = r.getAsJsonArray("steps");
            for (JsonElement s : stepsArr) steps.add(s.getAsString());
            recipes.add(new Recipe(name, ingredients, steps, time));
        }
        return recipes;
    }

    private String extractJsonArray(String text) throws Exception {
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        if (start == -1 || end == -1 || end <= start) {
            throw new APIException("Could not extract JSON array from LLM response");
        }
        return text.substring(start, end + 1);
    }
}