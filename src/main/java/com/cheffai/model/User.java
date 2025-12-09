package com.cheffai.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Ingredient> pantry = new ArrayList<>();
    private List<String> dietaryRestrictions = new ArrayList<>(); // e.g., "vegetarian", "no-lactose"

    public User(String name) {
        this.name = name;
    }

    public void addIngredient(Ingredient ingredient) {
        pantry.add(ingredient);
    }

    public List<Ingredient> getPantry() {
        return pantry;
    }

    public String getName() {
        return name;
    }

    public void addRestriction(String restriction) {
        dietaryRestrictions.add(restriction.toLowerCase());
    }

    public List<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }
}