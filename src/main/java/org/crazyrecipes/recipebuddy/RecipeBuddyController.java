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

/**
 * RecipeBuddyController defines and implements the API calls that we support.
 * On startup, it instantiates a DatabaseController to synchronize filesystem operations.
 * It also implements rate limiting to throttle repeated requests.
 */
@RestController
public class RecipeBuddyController {

    private final Bucket bucket;
    private Log log;
    private DatabaseController databaseController;

    /**
     * Instantiates a RecipeBuddyController. Only one instance of RecipeBuddyController
     * will exist. This will also instantiate a DatabaseController to handle the cache
     * and interactions with the filesystem.
     */
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

    /**
     * Returns a list of all recipes.
     * @return A list of all recipes
     */
    @GetMapping("/api/recipes")
    List<Recipe> readRecipes() {
        if(bucket.tryConsume(1)) {
            return databaseController.getRecipes();
        }
        throw new RateLimitException();
    }

    /**
     * Creates a recipe with the specified parameters.
     * Assigns the Recipe an ID and returns the newly created Recipe.
     * @param newRecipe Recipe to create
     * @return the created Recipe with the assigned ID
     */
    @PostMapping("/api/recipes")
    Recipe createRecipe(@RequestBody Recipe newRecipe) {
        if(bucket.tryConsume(1)) {
            Recipe r = databaseController.createRecipe(newRecipe);
            databaseController.writePhoto(RecipeBuddyMap.FALLBACK_THUMBNAIL, r.getID());
            return r;
        }
        throw new RateLimitException();
    }

    /**
     * Gets a single recipe by ID.
     * Will result in an HTTP 404 if the recipe cannot be found.
     * @param id The ID of the recipe to get
     * @return The recipe with the given ID
     */
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

    /**
     * Updates an existing Recipe with the given parameters.
     * Will result in an HTTP 404 if the recipe to update cannot be found.
     * @param newRecipe Recipe containing parameters to update
     * @param id The ID of the Recipe to update
     * @return The updated Recipe
     */
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

    /**
     * Deletes a Recipe with a given ID.
     * Will result in an HTTP 404 if the Recipe to delete cannot be found.
     * @param id The ID of the Recipe to delete
     */
    @DeleteMapping("/api/recipe/{id}")
    void deleteRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            log.print("Handling delete for recipe " + id);
            try {
                databaseController.deleteRecipe(id);
                databaseController.deletePhoto(id);
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find recipe " + id + " in database.");
                throw new NotFoundException();
            }
        } else {
            throw new RateLimitException();
        }
    }

    /**
     * Increments a Recipe's times cooked counter.
     * Will result in an HTTP 404 if the Recipe to update cannot be found.
     * @param id The ID of the recipe to update
     */
    @PostMapping("/api/cook/{id}")
    void cookRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            log.print("Handling increment times cooked for recipe " + id);
            try {
                databaseController.cookRecipe(id);
            } catch(NotFoundException e) {
                log.print(1, "Couldn't find recipe " + id + " in database.");
                throw new NotFoundException();
            }
        } else {
            throw new RateLimitException();
        }
    }

    /* ===== ALLERGENS ===== */

    /**
     * Gets list of allergens
     * @return The list of allergens
     */
    @GetMapping("/api/allergens")
    List<String> readAllergens() {
        if(bucket.tryConsume(1)) {
            return databaseController.readAllergens();
        }
        throw new RateLimitException();
    }

    /**
     * Posts list of allergens.
     * Overwrites the old list.
     * @param newAllergens List of allergens
     * @return The new list of allergens
     */
    @PostMapping("api/allergens")
    List<String> updateAllergens(@RequestBody List<String> newAllergens) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update of allergens.");
            return databaseController.writeAllergens(newAllergens);
        }
        throw new RateLimitException();
    }

    /* ===== INGREDIENTS ===== */

    /**
     * Gets list of ingredients.
     * @return The list of ingredients
     */
    @GetMapping("/api/ingredients")
    List<String> readIngredients() {
        if(bucket.tryConsume(1)) {
            return databaseController.readIngredients();
        }
        throw new RateLimitException();
    }

    /**
     * Posts list of ingredients.
     * @param newIngredients List of ingredients
     * @return The new list of ingredients
     */
    @PostMapping("/api/ingredients")
    List<String> updateIngredients(@RequestBody List<String> newIngredients) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update of ingredients.");
            return databaseController.writeIngredients(newIngredients);
        }
        throw new RateLimitException();
    }

    /* ===== UTENSILS ===== */

    /**
     * Gets list of utensils.
     * @return The list of utensils
     */
    @GetMapping("/api/utensils")
    List<String> readUtensils() {
        if(bucket.tryConsume(1)) {
            return databaseController.readUtensils();
        }
        throw new RateLimitException();
    }

    /**
     * Posts list of utensils.
     * @param newUtensils List of utensils
     * @return The new list of utensils
     */
    @PostMapping("api/utensils")
    List<String> updateUtensils(@RequestBody List<String> newUtensils) {
        if(bucket.tryConsume(1)) {
            log.print("Handling update of utensils.");
            return databaseController.writeUtensils(newUtensils);
        }
        throw new RateLimitException();
    }

    /* ===== SEARCH ===== */

    /**
     * Executes a search and returns the results
     * @param s_query Search parameters to use
     * @return Search results, ranked by relevance and quality
     */
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

    /**
     * Gets a photo by ID.
     * Will result in an HTTP 404 if the photo cannot be found.
     * @param id ID of the photo to get
     * @return The photo with the specified ID
     */
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

    /**
     * Updates a photo by ID. Will create it if it does not already exist.
     * @param item New content of the photo
     * @param id ID of the photo to update
     */
    @PostMapping("api/photo/{id}")
    void putPhoto(@RequestBody String item, @PathVariable String id) {
        if(bucket.tryConsume(1)) {
            databaseController.writePhoto(item, id);
            return;
        }
        throw new RateLimitException();
    }

    /* ===== BACKUP AND RESTORE ===== */

    /**
     * Returns a list of all recipes.
     * Identical to "GET api/recipes".
     * @return List of all recipes
     */
    @GetMapping("api/backup")
    List<Recipe> backupRecipes() {
        if(bucket.tryConsume(1)) {
            return databaseController.getRecipes();
        }
        throw new RateLimitException();
    }

    /**
     * Restores a list of recipes to the database.
     * Will edit recipes if they already exist, and create them if they don't.
     * @param newRecipes List of recipes to restore
     */
    @PostMapping("/api/restore")
    void restoreRecipes(@RequestBody List<Recipe> newRecipes) {
        if(bucket.tryConsume(1)) {
            log.print("Restoring " + newRecipes.size() + " recipes from backup.");
            for(Recipe i : newRecipes) {
                try {
                    databaseController.editRecipe(i.getID(), i);
                } catch(NotFoundException e) {
                    Recipe r = databaseController.createRecipe(i);
                    databaseController.writePhoto(RecipeBuddyMap.FALLBACK_THUMBNAIL, r.getID());
                }
            }
        } else {
            throw new RateLimitException();
        }
    }
}
