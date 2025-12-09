package com.cheffai.suggestor;

import com.cheffai.model.Recipe;
import com.cheffai.model.Ingredient;
import com.cheffai.llm.LLMClient;

import java.util.List;

/**
 * Sugestor que prioriza opções saudáveis.
 */
public class SuggestorSaudavel extends SuggestorBase {

    private final LLMClient llmClient;

    public SuggestorSaudavel(LLMClient llmClient) {
        super("SugestorSaudavel");
        this.llmClient = llmClient;
    }

    @Override
    public List<Recipe> suggest(List<Ingredient> pantry, List<String> dietaryConstraints) throws Exception {
        return llmClient.requestRecipes(pantry, dietaryConstraints, 30, 3, "healthy");
    }
}