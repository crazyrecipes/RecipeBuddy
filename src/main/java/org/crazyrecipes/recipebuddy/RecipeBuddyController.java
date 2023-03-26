package org.crazyrecipes.recipebuddy;

import java.time.Duration;
import java.util.List;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.crazyrecipes.recipebuddy.error.RateLimitException;
import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.recipe.RecipeRegistry;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeBuddyController {


    private final Bucket bucket;

    private RecipeRegistry recipeRegistry;

    RecipeBuddyController() {
        recipeRegistry = new RecipeRegistry();
        Bandwidth limit = Bandwidth.classic(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE,
                Refill.greedy(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    };

    @GetMapping("/api/v1/recipes")
    List<Recipe> readRecipes() {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.getAll();
        }
        throw new RateLimitException();

    }

    @PostMapping("/api/v1/recipes")
    Recipe createRecipe(@RequestBody String name) {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.createRecipe(name);
        }
        throw new RateLimitException();

    }

    @GetMapping("/api/v1/recipes/{id}")
    Recipe readRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.getRecipe(id);
        }
        throw new RateLimitException();
    }

    @PutMapping("/api/v1/recipes/{id}")
    Recipe updateRecipe(@RequestBody Recipe newRecipe, @PathVariable String id) {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.editRecipe(id, newRecipe);
        }
        throw new RateLimitException();
    }

    @DeleteMapping("/api/v1/recipes/{id}")
    void deleteRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            recipeRegistry.deleteRecipe(id);
        }
        throw new RateLimitException();
    }
}
