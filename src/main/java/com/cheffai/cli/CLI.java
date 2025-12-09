package com.cheffai.cli;

import com.cheffai.llm.LLMClient;
import com.cheffai.model.Ingredient;
import com.cheffai.model.Recipe;
import com.cheffai.model.User;
import com.cheffai.suggestor.SuggestorBase;
import com.cheffai.suggestor.SuggestorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console UI.
 */
public class CLI {

    private final Scanner scanner = new Scanner(System.in);
    private final LLMClient llmClient;

    public CLI(LLMClient llmClient) {
        this.llmClient = llmClient;
    }

    public void run() {
        System.out.println("Welcome to ChefAI!");
        System.out.print("Your name: ");
        String name = scanner.nextLine().trim();
        User user = new User(name.isEmpty() ? "Guest" : name);

        System.out.println("Enter ingredients you have (one per line, empty line to finish). Format: name[, amount]");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break;
            String[] parts = line.split(",", 2);
            String iname = parts[0].trim();
            String amount = parts.length > 1 ? parts[1].trim() : "";
            user.addIngredient(new Ingredient(iname, amount));
        }

        System.out.println("Any dietary restrictions? (comma-separated, e.g., vegetarian, no-gluten) or blank:");
        String rest = scanner.nextLine().trim();
        List<String> restrictions = new ArrayList<>();
        if (!rest.isEmpty()) {
            for (String r : rest.split(",")) {
                user.addRestriction(r.trim());
                restrictions.add(r.trim());
            }
        }

        System.out.println("Select suggestor type: [rapido/saudavel] (default rapido): ");
        String stype = scanner.nextLine().trim();
        if (stype.isEmpty()) stype = "rapido";

        SuggestorBase suggestor = SuggestorFactory.create(stype, llmClient);
        System.out.println("Requesting suggestions from suggestor: " + suggestor.getName() + " ...");

        try {
            List<Recipe> recipes = suggestor.suggest(user.getPantry(), user.getDietaryRestrictions());
            if (recipes.isEmpty()) {
                System.out.println("No recipes found.");
            } else {
                System.out.println("\n--- Suggested Recipes ---\n");
                for (Recipe r : recipes) {
                    System.out.println(r.toString());
                    System.out.println("-------------------------\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating suggestions: " + e.getMessage());
            e.printStackTrace();
        }
    }
}