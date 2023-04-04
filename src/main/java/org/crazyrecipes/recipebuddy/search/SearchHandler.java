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
            if(match(search, i) > 0) {
                searchResults.add(i);
            }
        }
        return searchResults;
    }

    int match(Search search, Recipe recipe) {
        int title_match = 0;
        int tags_match = 0;


        if(match_strings(recipe.getName(), search.getQuery())) { title_match++; }
        for(String i : recipe.getTags()) {
            if(match_strings(i, search.getQuery())) {
                tags_match++;
                break;
            }
        }


        // TODO implement
        return false;
    }

    boolean match_strings(String base, String find) {
        /* match any occurrence of base in find */
        String pattern = (new StringBuilder()).append("(.*)").append(find).append("(.*)").toString();
        return base.matches(pattern);
    }
}
