package org.crazyrecipes.recipebuddy.search;

import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.util.Log;

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
     * Executes a search and returns the results.
     * @param search Search parameters to use
     * @return Search results
     */
    public List<Recipe> doSearch(Search search) {
        log.print("Doing search...");
        log.print("...for query " + search.getQuery());
        log.print("...for ingredients " + search.getIngredients());
        log.print("...for allergens " + search.getAllergens());

        Vector<Recipe> searchResults = new Vector<>();
        for(Recipe i : recipes) {
            if(match(search, i)) {
                searchResults.add(i);
            }
        }
        log.print("Got " + searchResults.size() + " results.");
        return rank(searchResults);
    }

    /**
     * Checks if a recipe matches search criteria
     * @param search Search criteria
     * @param recipe Recipe to match
     * @return True if recipe matches
     */
    private boolean match(Search search, Recipe recipe) {
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
                }
                break;
            }
            if(!has_j) {
                ingredients_missing++;
            }
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

    /**
     * Ranks search results by quality
     * @param results Results to rank
     * @return Ranked results
     */
    private Vector<Recipe> rank(Vector<Recipe> results) {
        Vector<Result> ranked_results = new Vector<>();
        Vector<Recipe> output = new Vector<>();
        boolean firstElement = true;
        for(Recipe i : results) {
            double score = 0;
            score += i.getRating() * 20;
            score += i.getCooked();
            if(firstElement) {
                ranked_results.add(new Result(i, score));
                firstElement = false;
            } else {
                for(int j = 0; j < ranked_results.size(); j++) {
                    if(score >= ranked_results.get(j).score) {
                        ranked_results.insertElementAt(new Result(i, score), j);
                        break;
                    }
                }
            }
        }
        for(Result i : ranked_results) {
            output.add(i.recipe);
        }
        return output;
    }

    /**
     * Matches strings
     * @param base Base string
     * @param find String to search for
     * @return True if strings match
     */
    boolean match_strings(String base, String find) {
        /* match any occurrence of base in find */
        String pattern = (new StringBuilder()).append("(.*)").append(find).append("(.*)").toString();
        return base.toLowerCase().matches(pattern.toLowerCase());
    }
}
