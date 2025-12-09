package com.cheffai.model;

public class Ingredient {
    private String name;
    private String amount; // e.g., "2 cups", "1 piece"
    private double approximateQuantity; // optional numeric quantity for logic

    public Ingredient(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public Ingredient(String name, String amount, double approximateQuantity) {
        this.name = name;
        this.amount = amount;
        this.approximateQuantity = approximateQuantity;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public double getApproximateQuantity() { return approximateQuantity; }
    public void setApproximateQuantity(double approximateQuantity) { this.approximateQuantity = approximateQuantity; }

    @Override
    public String toString() {
        return name + (amount != null && !amount.isEmpty() ? " (" + amount + ")" : "");
    }
}