package org.crazyrecipes.recipebuddy.search;

import org.crazyrecipes.recipebuddy.recipe.Recipe;

public class Result {
    public Recipe recipe;
    public double score;
    public Result(Recipe recipe, double score) {
        this.recipe = recipe;
        this.score = score;
    }

}
