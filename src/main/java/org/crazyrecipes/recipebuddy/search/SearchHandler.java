package org.crazyrecipes.recipebuddy.search;

import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * SearchHandler provides functionality for searching recipes.
 */
public class SearchHandler {
    Vector<Recipe> recipes;
    Vector<String> ingredients;
    Vector<String> utensils;
    Vector<String> allergens;
    Log log;
    private final String SANITIZER_REGEX = "[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()/_+-]";

    /**
     * Instantiates a SearchHandler
     * @param recipes Recipes to search
     * @param ingredients Ingredients to search
     * @param utensils Utensils to search
     * @param allergens Allergens to search
     */
    public SearchHandler(List<Recipe> recipes, List<String> ingredients,
                         List<String> utensils, List<String> allergens) {
        this.log = new Log("SearchHandler");
        this.recipes = new Vector<>();
        this.ingredients = new Vector<>();
        this.utensils = new Vector<>();
        this.allergens = new Vector<>();
        this.recipes.addAll(recipes);
        this.ingredients.addAll(ingredients);
        this.utensils.addAll(utensils);
        this.allergens.addAll(allergens);
    }

    /**
     * Runs a search and returns the results. The results are first filtered
     *   and then ranked by relevance and quality.
     * @param search Search parameters to use
     * @return Search results
     */
    public List<Recipe> doSearch(Search search) {
        Vector<Result> results = new Vector<>();
        Vector<Recipe> output = new Vector<>();

        log.print("Executing search...");
        log.print("...for query " + search.getQuery());
        log.print("...for ingredients " + search.getIngredients());
        log.print("...for allergens " + search.getAllergens());

        /* Filter and score recipes */
        for(Recipe i : recipes) {
            if(filter(search, i)) {
                results.add(new Result(i, score(search, i)));
            }
        }

        /* Rank filtered recipes */
        Collections.sort(results);
        for(Result i : results) {
            output.add(i.recipe);
        }
        log.print("Ranked " + output.size() + " search results.");

        return output;
    }

    /**
     * Checks if a recipe matches search criteria
     * @param search Search criteria
     * @param recipe Recipe to match
     * @return True if recipe matches
     */
    private boolean filter(Search search, Recipe recipe) {
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
                    allergen_free = false;
                    break;
                }
            }
        }

        /* Filter by ingredients */
        int ingredients_missing = 0;
        boolean has_ingredients = false;
        for(String i : recipe.getIngredients()) {
            boolean has_j = false;
            for(String j : ingredients) {
                if(match_strings(i, j)) {
                    has_j = true;
                    break;
                }
            }
            if(!has_j) { ingredients_missing++; }
        }

        /* Filter by utensils */
        int utensils_missing = 0;
        boolean has_utensils = false;
        for(String i : recipe.getUtensils()) {
            boolean has_j = false;
            for(String j : utensils) {
                if(match_strings(i, j)) {
                    has_j = true;
                    break;
                }
            }
            if(!has_j) { utensils_missing++; }
        }

        /* Handle choice for showing allergens */
        if(search.allergens.equals("SHOW") && !allergen_free) {
            allergen_free = true;
        }

        /* Handle choice for recipes only showing all ingredients */
        if(search.ingredients.equals("ALL") && ingredients_missing == 0) {
            has_ingredients = true;
        }
        /* Handle choice for showing recipes missing a couple ingredients */
        else if(search.ingredients.equals("SOME") && ingredients_missing < 4) {
            has_ingredients = true;
        }
        /* Handle choice for not caring about ingredients */
        else if(search.ingredients.equals("NONE")) {
            has_ingredients = true;
        }

        /* Handle choice for recipes only showing all utensils */
        if(search.utensils.equals("ALL") && utensils_missing == 0) {
            has_utensils = true;
        }
        /* Handle choice for showing recipes missing a couple utensils */
        else if(search.utensils.equals("SOME") && utensils_missing < 4) {
            has_utensils = true;
        }
        /* Handle choice for not caring about utensils */
        else if(search.utensils.equals("NONE")) {
            has_utensils = true;
        }

        /* See if we match */
        return (title_match || tags_match) && allergen_free && has_ingredients && has_utensils;
    }

    /**
     * Assign the given Recipe a quality score based on its relevance
     *   to the search.
     * @param search Search to score recipes with
     * @param recipe Recipe to score
     * @return The recipe's score
     */
    private double score(Search search, Recipe recipe) {
        double output = 0.0;

        /* 1st priority - Recipes matching title */
        if(match_strings(recipe.getName(), search.getQuery())) {
            output += 1000.0;
        }

        /* 2nd priority - Recipes matching tags */
        for(String i : recipe.getTags()) {
            if(match_strings(i, search.getQuery())) {
                output += 50.0;
                break;
            }
        }

        /* 3rd priority - Recipes with high ratings */
        output += recipe.getRating() * 5.0;

        /* Deduct points for missing ingredients */
        for(String i : recipe.getIngredients()) {
            boolean has_j = false;
            for(String j : ingredients) {
                if(match_strings(i, j)) {
                    has_j = true;
                    break;
                }
            }
            if(!has_j) { output -= 10.0; }
        }

        /* Deduct points for missing utensils */
        for(String i : recipe.getUtensils()) {
            boolean has_j = false;
            for(String j : utensils) {
                if(match_strings(i, j)) {
                    has_j = true;
                    break;
                }
            }
            if(!has_j) { output -= 10.0; }
        }

        /* Deduct points for allergens */
        for(String i : recipe.getAllergens()) {
            for(String j : allergens) {
                if(match_strings(i, j)) {
                    output -= 20.0;
                    break;
                }
            }
        }

        return output;
    }

    /**
     * Matches strings
     * @param base Base string
     * @param find String to search base for
     * @return True if strings match
     */
    boolean match_strings(String base, String find) {
        String s_base = base.replaceAll(SANITIZER_REGEX,"");
        String s_find = find.replaceAll(SANITIZER_REGEX,"");
        String pattern = (new StringBuilder()).append("(.*)").append(s_find).append("(.*)").toString();
        return s_base.toLowerCase().matches(pattern.toLowerCase());
    }
}
