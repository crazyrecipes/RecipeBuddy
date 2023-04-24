package org.crazyrecipes.recipebuddy.search;

import java.util.Objects;

/**
 * Search holds the parameters of a single user search.
 */
public class Search {
    private String query;
    private String ingredients;
    private String utensils;
    private String allergens;

    /**
     * Instantiates a Search with the given parameters.
     * @param query Search keyword
     * @param ingredients Ingredients option (ALL, SOME, NONE)
     * @param utensils Utensils option (ALL, SOME, NONE)
     * @param allergens Allergens option (SHOW, HIDE)
     */
    public Search(String query, String ingredients, String utensils, String allergens) {
        this.query = query;
        this.ingredients = ingredients;
        this.utensils = utensils;
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
     * Get this Search's utensils option
     * @return this Search's utensils option
     */
    public String getUtensils() { return utensils; }

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
     * Sets this Search's utensils option
     * @param utensils this Search's utensils option
     */
    public void setUtensils(String utensils) { this.utensils = utensils; }

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
                Objects.equals(this.utensils, ((Search) other).getUtensils()) &&
                Objects.equals(this.allergens, ((Search) other).getAllergens());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.query, this.ingredients, this.utensils, this.allergens);
    }

    @Override
    public String toString() {
        return String.format(
                """
                "query":"%s",
                "ingredients":"%s",
                "utensils":"%s",
                "allergens":"%s"
                }
                """,
                this.query,
                this.ingredients,
                this.utensils,
                this.allergens
        );
    }

}
