package com.cheffai.suggestor;

import com.cheffai.model.Recipe;
import com.cheffai.model.Ingredient;
import com.cheffai.llm.LLMClient;

import java.util.List;

/**
 * Sugestor que prioriza rapidez (<=30 minutos). Delegates to LLMClient with specific prompt.
 */
public class SuggestorRapido extends SuggestorBase {

    private final LLMClient llmClient;

    public SuggestorRapido(LLMClient llmClient) {
        super("SugestorRapido");
        this.llmClient = llmClient;
    }

    @Override
    public List<Recipe> suggest(List<Ingredient> pantry, List<String> dietaryConstraints) throws Exception {
        // PromptBuilder builds a targeted prompt; LLMClient handles HTTP & parsing
        return llmClient.requestRecipes(pantry, dietaryConstraints, 30, 3, "quick");
    }
}