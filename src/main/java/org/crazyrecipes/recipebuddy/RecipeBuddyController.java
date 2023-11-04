package org.crazyrecipes.recipebuddy;

import java.io.IOException;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * RecipeBuddyController defines and implements the API calls that we support.
 * On startup, it instantiates a DatabaseController to synchronize filesystem operations.
 * It also implements rate limiting to throttle repeated requests.
 */
@SuppressWarnings("unused")
@RestController
public class RecipeBuddyController {
    /**
     * bucket4j bucket for rate limiting
     */
    private final Bucket bucket;

    /**
     * This RecipeBuddyController's Log
     */
    private final Log log;

    /**
     * This RecipeBuddyController's DatabaseController
     */
    private final DatabaseController databaseController;

    /**
     * Instantiates a RecipeBuddyController. Only one instance of RecipeBuddyController
     * will exist. This will also instantiate a DatabaseController to handle the cache
     * and interactions with the filesystem.
     */
    public RecipeBuddyController() throws IOException {
        this.log = new Log("RecipeBuddyController");
        log.print("Starting...");
        this.databaseController = new DatabaseController();
        Bandwidth limit = Bandwidth.classic(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE,
                Refill.greedy(RecipeBuddyMap.MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
        log.print("=== Startup complete. Welcome to RecipeBuddy. ===");
    }

    /* ===== RECIPES ===== */

    /**
     * Returns a list of all recipes.
     * @return A list of all recipes
     */
    @GetMapping("/api/recipes")
    public List<Recipe> readRecipes() {
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
    public Recipe createRecipe(@RequestBody Recipe newRecipe) throws IOException {
        if(bucket.tryConsume(1)) {
            return databaseController.createRecipe(newRecipe);
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
    public Recipe readRecipe(@PathVariable String id) {
        if(bucket.tryConsume(1)) {
            return databaseController.getRecipe(id);
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
    public Recipe updateRecipe(@RequestBody Recipe newRecipe, @PathVariable String id) throws IOException {
        if(bucket.tryConsume(1)) {
            log.print("Updating recipe \"" + id + "\".");
            return databaseController.editRecipe(id, newRecipe);
        }
        throw new RateLimitException();
    }

    /**
     * Deletes a Recipe with a given ID.
     * Will result in an HTTP 404 if the Recipe to delete cannot be found.
     * @param id The ID of the Recipe to delete
     */
    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@PathVariable String id) throws IOException {
        if(bucket.tryConsume(1)) {
            log.print("Deleting recipe \"" + id + "\".");
            databaseController.deleteRecipe(id);
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
    public void cookRecipe(@PathVariable String id) throws IOException {
        if(bucket.tryConsume(1)) {
            log.print("Handling increment times cooked for recipe " + id);
            databaseController.cookRecipe(id);
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
    public List<String> readAllergens() {
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
    public List<String> updateAllergens(@RequestBody List<String> newAllergens) throws IOException {
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
    public List<String> readIngredients() {
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
    public List<String> updateIngredients(@RequestBody List<String> newIngredients) throws IOException {
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
    public List<String> readUtensils() {
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
    public List<String> updateUtensils(@RequestBody List<String> newUtensils) throws IOException {
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
    public List<Recipe> doSearch(@RequestBody Search s_query) {
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
    public byte[] getPhoto(@PathVariable String id) throws IOException {
        if(bucket.tryConsume(1)) {
            return databaseController.readPhoto(id);
        }
        throw new RateLimitException();
    }

    /**
     * Updates a photo by ID. Will create it if it does not already exist.
     * @param item New content of the photo
     * @param id ID of the photo to update
     */
    @PostMapping("api/photo/{id}")
    public void putPhoto(@RequestBody String item, @PathVariable String id) throws IOException {
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
    public List<Recipe> backupRecipes() {
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
    public void restoreRecipes(@RequestBody List<Recipe> newRecipes) throws IOException {
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
