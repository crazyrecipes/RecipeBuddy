package org.crazyrecipes.recipebuddy.search;

import java.util.Objects;

public class Search {
    String query;
    String ingredients;
    String allergens;

    public Search(String query, String ingredients, String allergens) {
        this.query = query;
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public String getQuery() { return query; }

    public String getIngredients() { return ingredients; }

    public String getAllergens() { return allergens; }

    public void setQuery(String query) { this.query = query; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public void setAllergens(String allergens) { this.allergens = allergens; }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Search)) { return false; }
        return Objects.equals(this.query, ((Search) other).getQuery()) &&
                Objects.equals(this.ingredients, ((Search) other).getIngredients()) &&
                Objects.equals(this.allergens, ((Search) other).getAllergens());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.query, this.ingredients, this.allergens);
    }

    @Override
    public String toString() {
        return "Search{query='" + this.query + "', ingredients='" + this.ingredients +
                "', allergens='" + this.allergens + "'}";
    }

}
