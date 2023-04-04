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

    /* Quick and dirty search - don't look at it too hard */
    public List<Recipe> doSearch(Search search) {
        System.out.println("===== Doing search =====");
        System.out.println("Keyword: " + search.getQuery());
        System.out.println("Ingredients: " + search.getIngredients());
        System.out.println("Allergens: " + search.getAllergens());

        Vector<Recipe> searchResults = new Vector<>();
        for(Recipe i : recipes) {
            if(match(search, i)) {
                searchResults.add(i);
            }
        }
        return searchResults;
    }

    boolean match(Search search, Recipe recipe) {
        /* Filter by title */
        boolean title_match = match_strings(recipe.getName(), search.getQuery());

        /* Filter by tags */
        boolean tags_match = false;
        for(String i : recipe.getTags()) {
            if(match_strings(i, search.getQuery())) {
                tags_match = true;
                break;
            }
        }

        /* Filter by allergens */
        boolean allergen_free = true;
        for(String i : recipe.getAllergens()) {
            for(String j : allergens) {
                if(match_strings(i, j)) {
                    System.out.println("Has allergen " + j);
                    allergen_free = false;
                    break;
                }
            }
        }

        /* Filter by ingredients */
        int ingredients_missing = 0;
        boolean has_ingredients = false;
        for(String i : recipe.getIngredients()) {
            for(String j : ingredients) {
                boolean has_j = false;
                if(match_strings(i, j)) {
                    has_j = true;
                }
                if(!has_j) {
                    System.out.println("Missing ingredient " + j);
                    ingredients_missing++;
                }
            }
        }

        /* Handle choice for showing allergens */
        if(search.allergens.equals("SHOW") && allergen_free == false) {
            allergen_free = true;
        }

        /* Handle choice for recipes only showing all ingredients */
        if(search.ingredients.equals("ALL") && ingredients_missing == 0) {
            has_ingredients = true;
        }
        /* Handle choice for showing recipes missing a couple ingredients */
        else if(search.ingredients.equals("SOME") && ingredients_missing < 3) {
            has_ingredients = true;
        }
        /* Handle choice for not caring about ingredients */
        else if(search.ingredients.equals("NONE")) {
            has_ingredients = true;
        }

        /* See if we match */
        return (title_match || tags_match) && allergen_free && has_ingredients;
    }

    boolean match_strings(String base, String find) {
        /* match any occurrence of base in find */
        String pattern = (new StringBuilder()).append("(.*)").append(find).append("(.*)").toString();
        return base.toLowerCase().matches(pattern.toLowerCase());
    }
}
