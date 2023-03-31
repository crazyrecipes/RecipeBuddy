package org.crazyrecipes.recipebuddy;

import java.time.Duration;
import java.util.List;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.crazyrecipes.recipebuddy.error.RateLimitException;
import org.crazyrecipes.recipebuddy.recipes.Recipe;
import org.crazyrecipes.recipebuddy.recipes.RecipeRegistry;
import org.crazyrecipes.recipebuddy.allergens.AllergensRegistry;
import org.crazyrecipes.recipebuddy.ingredients.IngredientsRegistry;
import org.crazyrecipes.recipebuddy.utensils.UtensilsRegistry;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeBuddyController {

    private final Bucket bucket;

    private RecipeRegistry recipeRegistry;
    private AllergensRegistry allergensRegistry;
    private IngredientsRegistry ingredientsRegistry;
    private UtensilsRegistry utensilsRegistry;

    RecipeBuddyController() {
        recipeRegistry = new RecipeRegistry();
        allergensRegistry = new AllergensRegistry();
        ingredientsRegistry = new IngredientsRegistry();
        utensilsRegistry = new UtensilsRegistry();
        Bandwidth limit = Bandwidth.classic(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE,
                Refill.greedy(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    };

    /* ===== RECIPES ===== */

    @GetMapping("/api/recipes")
    List<Recipe> readRecipes() {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.getAll();
        }
        throw new RateLimitException();

    }

    @PostMapping("/api/recipes")
    Recipe createRecipe(@RequestBody Recipe newRecipe) {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.createRecipe(newRecipe);
        }
        throw new RateLimitException();

    }

    @GetMapping("/api/recipe/{id}")
    Recipe readRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.getRecipe(id);
        }
        throw new RateLimitException();
    }

    @PutMapping("/api/recipe/{id}")
    Recipe updateRecipe(@RequestBody Recipe newRecipe, @PathVariable String id) {
        if(bucket.tryConsume(1)) {
            return recipeRegistry.editRecipe(id, newRecipe);
        }
        throw new RateLimitException();
    }

    @DeleteMapping("/api/recipe/{id}")
    void deleteRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            recipeRegistry.deleteRecipe(id);
            return;
        }
        throw new RateLimitException();
    }

    /* ===== ALLERGENS ===== */

    @GetMapping("/api/allergens")
    List<String> readAllergens() {
        if(bucket.tryConsume(1)) {
            return allergensRegistry.getAllergens();
        }
        throw new RateLimitException();
    }

    @PostMapping("api/allergens")
    List<String> updateAllergens(@RequestBody List<String> newAllergens) {
        if(bucket.tryConsume(1)) {
            return allergensRegistry.postAllergens(newAllergens);
        }
        throw new RateLimitException();
    }

    /* ===== INGREDIENTS ===== */

    @GetMapping("/api/ingredients")
    List<String> readIngredients() {
        if(bucket.tryConsume(1)) {
            return ingredientsRegistry.getIngredients();
        }
        throw new RateLimitException();
    }

    @PostMapping("/api/ingredients")
    List<String> updateIngredients(@RequestBody List<String> newIngredients) {
        if(bucket.tryConsume(1)) {
            return ingredientsRegistry.postIngredients(newIngredients);
        }
        throw new RateLimitException();
    }

    /* ===== UTENSILS ===== */

    @GetMapping("/api/utensils")
    List<String> readUtensils() {
        if(bucket.tryConsume(1)) {
            return utensilsRegistry.getUtensils();
        }
        throw new RateLimitException();
    }

    @PostMapping("api/utensils")
    List<String> updateUtensils(@RequestBody List<String> newUtensils) {
        if(bucket.tryConsume(1)) {
            return utensilsRegistry.postUtensils(newUtensils);
        }
        throw new RateLimitException();
    }
}
