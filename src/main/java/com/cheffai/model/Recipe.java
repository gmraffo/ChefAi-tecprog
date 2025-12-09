package com.cheffai.model;

import java.util.List;

public class Recipe {
    private String name;
    private List<RecipeIngredient> ingredients;
    private List<String> steps;
    private int timeMinutes;

    public Recipe(String name, List<RecipeIngredient> ingredients, List<String> steps, int timeMinutes) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.timeMinutes = timeMinutes;
    }

    public String getName() { return name; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public List<String> getSteps() { return steps; }
    public int getTimeMinutes() { return timeMinutes; }

    public static class RecipeIngredient {
        private String name;
        private String amount;
        private boolean hasIngredient;

        public RecipeIngredient(String name, String amount, boolean hasIngredient) {
            this.name = name;
            this.amount = amount;
            this.hasIngredient = hasIngredient;
        }

        public String getName() { return name; }
        public String getAmount() { return amount; }
        public boolean isHasIngredient() { return hasIngredient; }

        @Override
        public String toString() {
            return name + " - " + amount + (hasIngredient ? " (you have)" : "");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe: ").append(name).append("\n");
        sb.append("Time: ").append(timeMinutes).append(" minutes\n");
        sb.append("Ingredients:\n");
        for (RecipeIngredient ri : ingredients) {
            sb.append(" - ").append(ri.toString()).append("\n");
        }
        sb.append("Steps:\n");
        int i = 1;
        for (String s : steps) {
            sb.append(i++).append(". ").append(s).append("\n");
        }
        return sb.toString();
    }
}