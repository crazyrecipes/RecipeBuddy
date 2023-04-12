package org.crazyrecipes.recipebuddy.search;

import java.util.Objects;

/**
 * Search holds the parameters of a single user search.
 */
public class Search {
    String query;
    String ingredients;
    String allergens;

    /**
     * Instantiates a Search with the given parameters.
     * @param query Search keyword
     * @param ingredients Ingredients option (ALL, SOME, NONE)
     * @param allergens Allergens option (SHOW, HIDE)
     */
    public Search(String query, String ingredients, String allergens) {
        this.query = query;
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    /**
     * Get this Search's query
     * @return this Search's query
     */
    public String getQuery() { return query; }

    /**
     * Get this Search's ingredients option
     * @return this Search's ingredients option
     */
    public String getIngredients() { return ingredients; }

    /**
     * Get this Search's allergens option
     * @return this Search's allergens option
     */
    public String getAllergens() { return allergens; }

    /**
     * Sets this Search's query
     * @param query this Search's query
     */
    public void setQuery(String query) { this.query = query; }

    /**
     * Sets this Search's ingredients option
     * @param ingredients this Search's ingredients option
     */
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    /**
     * Sets this Search's allergens option
     * @param allergens this Search's allergens option
     */
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
