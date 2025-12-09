package com.cheffai.util;

import com.cheffai.model.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility that builds a deterministic prompt to request structured JSON output from the LLM.
 * The prompt asks for exactly a JSON array with objects containing: name, ingredients (name, amount, hasIngredient), steps[], time_minutes
 */
public class PromptBuilder {

    public static String buildPrompt(List<Ingredient> pantry, List<String> dietaryConstraints, int maxTimeMinutes, int howMany, String flavor) {
        String pantryList = pantry.stream()
                .map(i -> i.getName() + (i.getAmount() != null && !i.getAmount().isBlank() ? " (" + i.getAmount() + ")" : ""))
                .collect(Collectors.joining(", "));
        StringBuilder sb = new StringBuilder();
        sb.append("You are a recipe suggestion engine. Respond ONLY with a valid JSON array (no markdown, no extra commentary). ");
        sb.append("Each element must be an object with the following fields:\n");
        sb.append(" - name: string\n");
        sb.append(" - ingredients: array of objects { name: string, amount: string, hasIngredient: boolean }\n");
        sb.append(" - steps: array of strings (step-by-step)\n");
        sb.append(" - time_minutes: integer\n\n");
        sb.append("Constraints:\n");
        sb.append(" - Return exactly ").append(howMany).append(" recipes.\n");
        sb.append(" - Each recipe must be realistically executable in a home kitchen and must have total time <= ").append(maxTimeMinutes).append(" minutes.\n");
        sb.append(" - Prefer using the user's pantry ingredients where appropriate. Mark hasIngredient true for ingredients the user already has, false otherwise.\n");
        if (dietaryConstraints != null && !dietaryConstraints.isEmpty()) {
            sb.append(" - Dietary constraints: ").append(String.join(", ", dietaryConstraints)).append(".\n");
        }
        sb.append(" - Style hint: ").append(flavor).append(".\n\n");
        sb.append("User pantry: ").append(pantryList).append("\n\n");
        sb.append("Return JSON like:\n");
        sb.append("[ { \"name\": \"...\", \"ingredients\": [{\"name\":\"\",\"amount\":\"\",\"hasIngredient\":true}], \"steps\":[\"step1\",\"step2\"], \"time_minutes\": 20 }, ... ]\n");
        sb.append("Make ingredient names concise. When possible, use pantry items as-is as hasIngredient=true.\n");
        return sb.toString();
    }
}