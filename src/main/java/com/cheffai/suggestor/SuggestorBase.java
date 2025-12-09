package com.cheffai.suggestor;

import com.cheffai.model.Recipe;
import com.cheffai.model.Ingredient;
import java.util.List;

public abstract class SuggestorBase {
    protected String name;

    public SuggestorBase(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    /**
     * Generate recipe suggestions based on pantry ingredients and constraints.
     * Implementations should call an LLM client or other strategy.
     */
    public abstract List<Recipe> suggest(List<Ingredient> pantry, List<String> dietaryConstraints) throws Exception;
}