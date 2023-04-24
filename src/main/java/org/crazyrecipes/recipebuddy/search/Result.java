package org.crazyrecipes.recipebuddy.search;

import org.crazyrecipes.recipebuddy.recipe.Recipe;

/**
 * Result stores a single Recipe and its score as a double.
 */
public class Result implements Comparable<Result> {
    /**
     * This Result's contained Recipe
     */
    public Recipe recipe;

    /**
     * This Result's contained score
     */
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

    @Override
    public int compareTo(Result other) {
        if(this.score > other.score) {
            return -1;
        } else if(this.score < other.score) {
            return 1;
        } else {
            return 0;
        }
    }

}
