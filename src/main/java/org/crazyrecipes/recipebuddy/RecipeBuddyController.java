package org.crazyrecipes.recipebuddy;

import java.time.Duration;
import java.util.List;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.crazyrecipes.recipebuddy.database.DatabaseController;
import org.crazyrecipes.recipebuddy.error.NotFoundException;
import org.crazyrecipes.recipebuddy.error.RateLimitException;
import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.search.Search;
import org.crazyrecipes.recipebuddy.search.SearchHandler;
import org.crazyrecipes.recipebuddy.util.Log;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeBuddyController {

    private final Bucket bucket;
    private Log log;
    private DatabaseController databaseController;

    RecipeBuddyController() {
        this.log = new Log("RecipeBuddyController");
        log.print("Starting up...");
        this.databaseController = new DatabaseController();
        Bandwidth limit = Bandwidth.classic(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE,
                Refill.greedy(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
        log.print("Init completed. Welcome to RecipeBuddy.");
    };

    /* ===== RECIPES ===== */

    @GetMapping("/api/recipes")
    List<Recipe> readRecipes() {
        if(bucket.tryConsume(1)) {
            return databaseController.getRecipes();
        }
        throw new RateLimitException();
    }

    @PostMapping("/api/recipes")
    Recipe createRecipe(@RequestBody Recipe newRecipe) {
        if(bucket.tryConsume(1)) {
            Recipe r = databaseController.createRecipe(newRecipe);
            databaseController.writePhoto(RecipeBuddyMap.FALLBACK_THUMBNAIL, r.getID());
            return r;
        }
        throw new RateLimitException();
    }

    @GetMapping("/api/recipe/{id}")
    Recipe readRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            try {
                return databaseController.getRecipe(id);
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find recipe " + id + " in database.");
                throw new NotFoundException();
            }
        }
        throw new RateLimitException();
    }

    @PutMapping("/api/recipe/{id}")
    Recipe updateRecipe(@RequestBody Recipe newRecipe, @PathVariable String id) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update for recipe " + id);
            try {
                return databaseController.editRecipe(id, newRecipe);
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find recipe " + id + " in database.");
                throw new NotFoundException();
            }
        }
        throw new RateLimitException();
    }

    @DeleteMapping("/api/recipe/{id}")
    void deleteRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            log.print("Handling delete for recipe " + id);
            try {
                databaseController.deleteRecipe(id);
                databaseController.deletePhoto(id);
                return;
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find recipe " + id + " in database.");
                throw new NotFoundException();
            }
        }
        throw new RateLimitException();
    }

    @PostMapping("/api/cook/{id}")
    void cookRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            log.print("Handling increment times cooked for recipe " + id);
            try {
                databaseController.cookRecipe(id);
                return;
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find recipe " + id + " in database.");
                throw new NotFoundException();
            }
        }
        throw new RateLimitException();
    }

    /* ===== ALLERGENS ===== */

    @GetMapping("/api/allergens")
    List<String> readAllergens() {
        if(bucket.tryConsume(1)) {
            return databaseController.readAllergens();
        }
        throw new RateLimitException();
    }

    @PostMapping("api/allergens")
    List<String> updateAllergens(@RequestBody List<String> newAllergens) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update of allergens.");
            return databaseController.writeAllergens(newAllergens);
        }
        throw new RateLimitException();
    }

    /* ===== INGREDIENTS ===== */

    @GetMapping("/api/ingredients")
    List<String> readIngredients() {
        if(bucket.tryConsume(1)) {
            return databaseController.readIngredients();
        }
        throw new RateLimitException();
    }

    @PostMapping("/api/ingredients")
    List<String> updateIngredients(@RequestBody List<String> newIngredients) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update of ingredients.");
            return databaseController.writeIngredients(newIngredients);
        }
        throw new RateLimitException();
    }

    /* ===== UTENSILS ===== */

    @GetMapping("/api/utensils")
    List<String> readUtensils() {
        if(bucket.tryConsume(1)) {
            return databaseController.readUtensils();
        }
        throw new RateLimitException();
    }

    @PostMapping("api/utensils")
    List<String> updateUtensils(@RequestBody List<String> newUtensils) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update of utensils.");
            return databaseController.writeUtensils(newUtensils);
        }
        throw new RateLimitException();
    }

    /* ===== SEARCH ===== */

    @PostMapping("api/search")
    List<Recipe> doSearch(@RequestBody Search s_query) {
        if(bucket.tryConsume(1)) {
            log.print("Handling search.");
            return (new SearchHandler(databaseController.getRecipes(),
                    databaseController.readIngredients(),
                    databaseController.readUtensils(),
                    databaseController.readAllergens())).doSearch(s_query);
        }
        throw new RateLimitException();
    }

    /* ===== PHOTOS ===== */

    @GetMapping(value = "api/photo/{id}")
    byte[] getPhoto(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            try {
                return databaseController.readPhoto(id);
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find photo " + id + " in database.");
                throw new NotFoundException();
            }
        }
        throw new RateLimitException();
    }

    @PostMapping("api/photo/{id}")
    void putPhoto(@RequestBody String item, @PathVariable String id) {
        if(bucket.tryConsume(1)) {
            databaseController.writePhoto(item, id);
            return;
        }
        throw new RateLimitException();
    }
}
