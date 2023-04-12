package org.crazyrecipes.recipebuddy.search;

import org.crazyrecipes.recipebuddy.recipe.Recipe;

/**
 * Result stores a single Recipe and its score as a double.
 */
public class Result {
    public Recipe recipe;
    public double score;

    /**
     * Instantiates a Result with a given Recipe and score.
     * @param recipe The Result's recipe
     * @param score The Result's score
     */
    public Result(Recipe recipe, double score) {
        this.recipe = recipe;
        this.score = score;
    }

}
