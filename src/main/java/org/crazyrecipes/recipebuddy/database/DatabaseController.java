package org.crazyrecipes.recipebuddy.database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;
import java.util.Vector;
import java.util.List;

import org.crazyrecipes.recipebuddy.RecipeBuddyMap;
import org.crazyrecipes.recipebuddy.error.NotFoundException;
import org.crazyrecipes.recipebuddy.recipe.*;
import org.crazyrecipes.recipebuddy.util.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * DatabaseController provides functionality for creating, reading, updating,
 *   and deleting recipes, ingredients, utensils, allergens, and photos.
 */
public class DatabaseController {
    /**
     * Path where recipes are stored
     */
    private static final String RECIPES_STORE_FILE = "data/recipes.dat";

    /**
     * Path where ingredients are stored
     */
    private static final String INGREDIENTS_STORE_FILE = "data/ingredients.dat";

    /**
     * Path where utensils are stored
     */
    private static final String UTENSILS_STORE_FILE = "data/utensils.dat";

    /**
     * Path where allergens are stored
     */
    private static final String ALLERGENS_STORE_FILE = "data/allergens.dat";

    /**
     * Currently loaded recipes
     */
    private final Vector<Recipe> recipes;

    /**
     * Currently loaded ingredients
     */
    private final Vector<String> ingredients;

    /**
     * Currently loaded utensils
     */
    private final Vector<String> utensils;

    /**
     * Currently loaded allergens
     */
    private final Vector<String> allergens;

    /**
     * This DatabaseController's Log
     */
    private final Log log;

    /**
     * Instantiates a DatabaseController.
     */
    public DatabaseController() throws IOException {
        this.log = new Log("DatabaseController");
        try {
            Files.createDirectories(Paths.get("data/photos/"));
        } catch(IOException e) {
            log.print(2, "Failed to ensure presence of data directories.");
        }
        log.print("Loading data from disk...");
        this.recipes = loadRecipesFromFile();
        log.print("Loaded " + this.recipes.size() + " recipes.");
        this.ingredients = loadStringsFromFile(INGREDIENTS_STORE_FILE);
        log.print("Loaded " + this.ingredients.size() + " ingredients.");
        this.utensils = loadStringsFromFile(UTENSILS_STORE_FILE);
        log.print("Loaded " + this.utensils.size() + " utensils.");
        this.allergens = loadStringsFromFile(ALLERGENS_STORE_FILE);
        log.print("Loaded " + this.allergens.size() + " allergens.");
        cleanupPhotos();
        log.print("Init completed.");
    }

    /**
     * Gets all recipes.
     * @return All recipes as a list
     */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Gets a single recipe by ID
     * @param id Recipe's ID
     * @return The Recipe with the specified ID
     */
    public Recipe getRecipe(String id) throws HttpStatusCodeException {
        for(Recipe i : recipes) {
            if (i.getID().equals(id)) {
                return i;
            }
        }
        log.print(1, "Recipe \"" + id + "\" not found.");
        throw new NotFoundException();
    }

    /**
     * Creates a new Recipe with the given instance.
     * @param newRecipe New recipe to add
     * @return The created Recipe
     */
    public synchronized Recipe createRecipe(Recipe newRecipe) throws IOException {
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.duplicate_from(newRecipe);
        recipes.add(recipeToAdd);
        writePhoto(RecipeBuddyMap.FALLBACK_THUMBNAIL, recipeToAdd.getID());
        saveRecipesToFile(recipes);
        return recipeToAdd;
    }

    /**
     * Updates a specific Recipe.
     * @param id The Recipe ID to update
     * @param recipe The Recipe to replace with
     * @return The updated Recipe
     */
    public synchronized Recipe editRecipe(String id, Recipe recipe) throws NotFoundException, IOException {
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.get(i).duplicate_from(recipe);
                saveRecipesToFile(recipes);
                return recipe;
            }
        }
        log.print(1, "Recipe \"" + id + "\" not found.");
        throw new NotFoundException();
    }

    /**
     * Increments a Recipe's times cooked counter.
     * @param id The Recipe ID to update
     */
    public synchronized void cookRecipe(String id) throws NotFoundException, IOException {
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.get(i).cook();
                saveRecipesToFile(recipes);
                return;
            }
        }
        log.print(1, "Recipe \"" + id + "\" not found.");
        throw new NotFoundException();
    }

    /**
     * Deletes a Recipe.
     * @param id Recipe ID to delete
     */
    public synchronized void deleteRecipe(String id) throws NotFoundException, IOException {
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.remove(i);
                saveRecipesToFile(recipes);
                deletePhoto(id);
                return;
            }
        }
        log.print(1, "Recipe \"" + id + "\" not found.");
        throw new NotFoundException();
    }

    /**
     * Reads ingredients list.
     * @return All ingredients as a list
     */
    public synchronized List<String> readIngredients(){
        return ingredients;
    }

    /**
     * Writes ingredients list.
     * @param newIngredients Ingredients list to write
     * @return The posted ingredients.
     */
    public synchronized List<String> writeIngredients(List<String> newIngredients) throws IOException {
        ingredients.clear();
        for(String i : newIngredients) {
            String j = i.replaceAll("[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()]","");
            if(j.length() > 1) {
                ingredients.add(j);
            }
        }
        saveStringsToFile(ingredients, INGREDIENTS_STORE_FILE);
        return ingredients;
    }

    /**
     * Reads utensils list.
     * @return All utensils as a list
     */
    public synchronized List<String> readUtensils() { return utensils; }

    /**
     * Writes utensils list.
     * @param newUtensils Utensils list to write
     * @return The posted utensils
     */
    public synchronized List<String> writeUtensils(List<String> newUtensils) throws IOException {
        utensils.clear();
        for(String i : newUtensils) {
            String j = i.replaceAll("[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()]","");
            if(j.length() > 1) {
                utensils.add(j);
            }
        }
        saveStringsToFile(utensils, UTENSILS_STORE_FILE);
        return utensils;
    }

    /**
     * Reads allergens list.
     * @return All allergens as a list
     */
    public synchronized List<String> readAllergens() { return allergens; }

    /**
     * Writes allergens list.
     * @param newAllergens Allergens list to write
     * @return The posted allergens
     */
    public synchronized List<String> writeAllergens(List<String> newAllergens) throws IOException {
        allergens.clear();
        for(String i : newAllergens) {
            String j = i.replaceAll("[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()]","");
            if(j.length() > 1) {
                allergens.add(j);
            }
        }
        saveStringsToFile(allergens, ALLERGENS_STORE_FILE);
        return allergens;
    }

    /**
     * Reads a single photo with a given ID
     * @param id The ID of the photo to read
     * @return The photo as bytes
     */
    public synchronized byte[] readPhoto(String id) throws IOException {
        try {
            return loadBytesFromFile("data/photos/"+id);
        } catch(FileNotFoundException e) {
            log.print(1, "Photo \"" + id + "\" not found.");
            throw new NotFoundException();
        }
        
    }

    /**
     * Writes a single photo with a given ID
     * @param item The photo as bytes
     * @param id The ID of the photo to write
     */
    public synchronized void writePhoto(String item, String id) throws IOException {
        byte[] image = Base64.getDecoder().decode(item.split(",")[1]);
        saveBytesToFile(image, "data/photos/"+id);
    }

    /**
     * Deletes a single photo with a given ID
     * @param id The ID of the photo to delete
     */
    public synchronized void deletePhoto(String id) {
        deleteFile("data/photos/"+id);
    }

    /**
     * Resets the database. Clears all items in the cache and on the disk.
     */
    public synchronized void reset() throws IOException {
        log.print(1, "RESETTING DATABASE!");
        recipes.clear();
        ingredients.clear();
        utensils.clear();
        allergens.clear();
        saveRecipesToFile(recipes);
        saveStringsToFile(ingredients, INGREDIENTS_STORE_FILE);
        saveStringsToFile(utensils, UTENSILS_STORE_FILE);
        saveStringsToFile(allergens, ALLERGENS_STORE_FILE);
        cleanupPhotos();
        log.print("Database reset complete.");
    }

    /**
     * Loads a vector of strings from a file.
     * @param STORE_FILE Filename to load strings from
     * @return The loaded strings
     */
    private synchronized Vector<String> loadStringsFromFile(String STORE_FILE) {
        try {
            log.print("Loading \"" + STORE_FILE + "\".");
            Scanner file = new Scanner(new FileInputStream(STORE_FILE));
            Vector<String> lines = new Vector<>();
            while(file.hasNextLine()) { lines.add(file.nextLine()); }
            return lines;
        } catch(FileNotFoundException e) {
            log.print(1, "File \"" + STORE_FILE + "\" not found.");
            return new Vector<>();
        }
    }

    /**
     * Saves a vector of strings to a file.
     * @param items Vector of strings to save
     * @param STORE_FILE Filename to save to
     */
    private synchronized void saveStringsToFile(Vector<String> items, String STORE_FILE) throws IOException {
        log.print("Saving \"" + STORE_FILE + "\".");
        PrintWriter file = new PrintWriter(new FileOutputStream(STORE_FILE));
        for(String i : items) { file.println(i); }
        file.flush();
        file.close();
    }

    /**
     * Loads a vector of recipes from a file.
     * @return Loaded recipes
     */
    private synchronized Vector<Recipe> loadRecipesFromFile() throws IOException {
        try {
            log.print("Loading \"" + RECIPES_STORE_FILE + "\".");
            Scanner file = new Scanner(new FileInputStream(RECIPES_STORE_FILE));
            Vector<Recipe> loaded_recipes = new Vector<>();
            Vector<String> line_buf = new Vector<>();
            String current_line;
            while(file.hasNextLine()) {
                current_line = file.nextLine();
                if(current_line.equals("\f")) {
                    loaded_recipes.add(new Recipe(line_buf));
                    line_buf.clear();
                } else {
                    line_buf.add(current_line);
                }
            }
            file.close();
            return loaded_recipes;
        } catch(FileNotFoundException e) {
            log.print(1, "File \"" + RECIPES_STORE_FILE + "\" not found.");
            return new Vector<>();
        }
    }

    /**
     * Saves a vector of strings to a file.
     * @param items Vector of strings to save
     */
    private synchronized void saveRecipesToFile(Vector<Recipe> items) throws IOException {
        log.print("Saving \"" + RECIPES_STORE_FILE + "\".");
        PrintWriter file = new PrintWriter(new FileOutputStream(RECIPES_STORE_FILE));
        for(Recipe i : items) { file.print(i); }
        file.flush();
        file.close();
    }

    /**
     * Loads bytes from a file.
     * @param STORE_FILE Filename to load bytes from
     * @return The loaded bytes
     */
    private synchronized byte[] loadBytesFromFile(String STORE_FILE) throws IOException {
        File f = new File(STORE_FILE);
        FileInputStream fis = new FileInputStream(f);
        byte[] fb = new byte[(int) f.length()];
        int ignored = fis.read(fb);
        fis.close();
        return fb;
    }

    /**
     * Saves bytes to a file.
     * @param item Bytes to save
     * @param STORE_FILE Filename to save to
     */
    private synchronized void saveBytesToFile(byte[] item, String STORE_FILE) throws IOException {
        log.print("Saving \"" + STORE_FILE + "\".");
        FileOutputStream f = new FileOutputStream(STORE_FILE);
        f.write(item);
        f.close();
    }

    /**
     * Deletes a file.
     * @param STORE_FILE File to delete
     */
    private synchronized void deleteFile(String STORE_FILE) {
        File f = new File(STORE_FILE);
        if(f.delete()) { return; }
        log.print(2, "Could not delete \"" + STORE_FILE + "\".");
    }

    /**
     * Cleans up unused photos from disk.
     */
    private synchronized void cleanupPhotos() {
        log.print(0, "Scanning photo database...");
        Vector<String> recipe_ids = new Vector<>();
        for(Recipe i : recipes) {
            recipe_ids.add(i.getID());
        }
        File[] files = (new File("data/photos/")).listFiles();
        if(files == null || files.length == 0) { return; }
        int n_photos = 0;
        for(File i : files) { if(i.isFile()) { n_photos++; } }
        if(n_photos <= recipe_ids.size()) { return; }
        log.print(0, "Cleaning up unused photos.");
        for(File i : files) {
            if(i.isFile()) {
                boolean hasParentRecipe = false;
                for(String j : recipe_ids) {
                    if(i.getName().equals(j)) {
                        hasParentRecipe = true;
                        recipe_ids.remove(j);
                        break;
                    }
                }
                if(!hasParentRecipe) {
                    log.print(0, "Deleting unused photo \"" + i.getName() + "\".");
                    if(!i.delete()) {
                        log.print(1, "Failed to delete unused photo " + i.getName() + "\".");
                    }
                }
            }
        }
    }
}
