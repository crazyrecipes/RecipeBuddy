package org.crazyrecipes.recipebuddy.search;

import org.crazyrecipes.recipebuddy.recipe.Recipe;

import java.util.List;
import java.util.Vector;

public class SearchHandler {
    Vector<Recipe> recipes;
    Vector<String> ingredients;
    Vector<String> utensils;
    Vector<String> allergens;

    public SearchHandler(List<Recipe> recipes, List<String> ingredients,
                         List<String> utensils, List<String> allergens) {
        this.recipes = new Vector<>();
        this.ingredients = new Vector<>();
        this.utensils = new Vector<>();
        this.allergens = new Vector<>();
        for(Recipe i : recipes) { this.recipes.add(i); }
        for(String i : ingredients) { this.ingredients.add(i); }
        for(String i : utensils) { this.utensils.add(i); }
        for(String i : allergens) { this.allergens.add(i); }
    }

    public List<Recipe> doSearch(Search search) {
        Vector<Recipe> searchResults = new Vector<>();
        for(Recipe i : recipes) {
            if(match(search, i)) {
                searchResults.add(i);
            }
        }
        return searchResults;
    }

    boolean match(Search search, Recipe recipe) {
        // TODO implement
        return false;
    }
}
