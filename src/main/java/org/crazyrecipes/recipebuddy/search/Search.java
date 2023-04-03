package org.crazyrecipes.recipebuddy.search;

import java.util.Objects;

public class Search {
    String title;
    String tags;
    String ingredients;
    String allergens;

    public Search(String title, String tags, String ingredients, String allergens) {
        this.title = title;
        this.tags = tags;
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public String getTitle() { return title; }

    public String getTags() { return tags; }

    public String getIngredients() { return ingredients; }

    public String getAllergens() { return allergens; }

    public void setTitle(String title) { this.title = title; }

    public void setTags(String tags) { this.tags = tags; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public void setAllergens(String allergens) { this.allergens = allergens; }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Search)) { return false; }
        return Objects.equals(this.title, ((Search) other).getTitle()) &&
                Objects.equals(this.tags, ((Search) other).getTags()) &&
                Objects.equals(this.ingredients, ((Search) other).getIngredients()) &&
                Objects.equals(this.allergens, ((Search) other).getAllergens());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.tags, this.ingredients,
                this.allergens);
    }

    @Override
    public String toString() {
        return "Search{title='" + this.title + "', tags = '" + this.tags +
                "', ingredients='" + this.ingredients + "', allergens='" +
                this.allergens + "'}";
    }

}
