package org.crazyrecipes.recipebuddy.database;

import org.crazyrecipes.recipebuddy.RecipeBuddyMap;
import org.crazyrecipes.recipebuddy.error.NotFoundException;
import org.crazyrecipes.recipebuddy.error.ResourceUpdateException;
import org.crazyrecipes.recipebuddy.recipe.*;
import org.crazyrecipes.recipebuddy.util.Log;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Vector;
import java.util.List;

/**
 * DatabaseController provides functionality for creating, reading, updating,
 *   and deleting recipes, ingredients, utensils, allergens, and photos.
 */
public class DatabaseController {
    private final String RECIPES_STORE_FILE = "data/recipes.dat";
    private final String INGREDIENTS_STORE_FILE = "data/ingredients.dat";
    private final String UTENSILS_STORE_FILE = "data/utensils.dat";
    private final String ALLERGENS_STORE_FILE = "data/allergens.dat";

    private Vector<Recipe> recipes;
    private Vector<String> ingredients;
    private Vector<String> utensils;
    private Vector<String> allergens;
    private Log log;

    /**
     * Instantiates a DatabaseController.
     */
    public DatabaseController() {
        this.log = new Log("DatabaseController");
        try {
            Files.createDirectories(Paths.get("data/photos/"));
        } catch(IOException e) {
            log.print(2, "Failed to ensure presence of database directories.");
        }
        log.print("Creating cached database...");
        this.recipes = loadRecipesFromFile(RECIPES_STORE_FILE);
        this.ingredients = loadStringsFromFile(INGREDIENTS_STORE_FILE);
        this.utensils = loadStringsFromFile(UTENSILS_STORE_FILE);
        this.allergens = loadStringsFromFile(ALLERGENS_STORE_FILE);
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
    public Recipe getRecipe(String id) throws NotFoundException {
        for(Recipe i : recipes) {
            if (i.getID().equals(id)) {
                return i;
            }
        }
        log.print(1, "Recipe " + id + " not found in cache on read.");
        throw new NotFoundException();
    }

    /**
     * Creates a new Recipe with the given instance.
     * @param newRecipe New recipe to add
     * @return The created Recipe
     */
    public synchronized Recipe createRecipe(Recipe newRecipe) {
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.duplicate_from(newRecipe);
        recipes.add(recipeToAdd);
        writePhoto(RecipeBuddyMap.FALLBACK_THUMBNAIL, recipeToAdd.getID());
        saveRecipesToFile(recipes, RECIPES_STORE_FILE);
        return recipeToAdd;
    }

    /**
     * Updates a specific Recipe.
     * @param id The Recipe ID to update
     * @param recipe The Recipe to replace with
     * @return The updated Recipe
     */
    public synchronized Recipe editRecipe(String id, Recipe recipe) throws NotFoundException {
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.get(i).duplicate_from(recipe);
                saveRecipesToFile(recipes, RECIPES_STORE_FILE);
                return recipe;
            }
        }
        log.print(1, "Recipe " + id + " not found in cache on edit.");
        throw new NotFoundException();
    }

    /**
     * Increments a Recipe's times cooked counter.
     * @param id The Recipe ID to update
     */
    public synchronized void cookRecipe(String id) throws NotFoundException {
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.get(i).cook();
                saveRecipesToFile(recipes, RECIPES_STORE_FILE);
                return;
            }
        }
        log.print(1, "Recipe " + id + " not found in cache on edit.");
        throw new NotFoundException();
    }

    /**
     * Deletes a Recipe.
     * @param id Recipe ID to delete
     */
    public synchronized void deleteRecipe(String id) throws NotFoundException {
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.remove(i);
                saveRecipesToFile(recipes, RECIPES_STORE_FILE);
                deletePhoto(id);
                return;
            }
        }
        log.print(1, "Recipe " + id + " not found in cache on delete.");
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
     * @param newIngredients Ingredients list to write.
     * @return The posted ingredients.
     */
    public synchronized List<String> writeIngredients(List<String> newIngredients) {
        ingredients = new Vector<>();
        for(String i : newIngredients) {
            ingredients.add(i.replaceAll("[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()]",""));
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
     * @param newUtensils Utensils list to post
     * @return The posted utensils
     */
    public synchronized List<String> writeUtensils(List<String> newUtensils) {
        utensils = new Vector<>();
        for(String i : newUtensils) {
            utensils.add(i.replaceAll("[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()]",""));
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
     * @param newAllergens Allergens list to post
     * @return The posted allergens
     */
    public synchronized List<String> writeAllergens(List<String> newAllergens) {
        allergens = new Vector<>();
        for(String i : newAllergens) {
            allergens.add(i.replaceAll("[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()]",""));
        }
        saveStringsToFile(allergens, ALLERGENS_STORE_FILE);
        return allergens;
    }

    /**
     * Reads a single photo with a given ID
     * @param id The ID of the photo to read
     * @return The photo as bytes
     */
    public synchronized byte[] readPhoto(String id) {
        return loadBytesFromFile("data/photos/"+id);
    }

    /**
     * Writes a single photo with a given ID
     * @param item The photo as bytes
     * @param id The ID of the photo to write
     */
    public synchronized void writePhoto(String item, String id) {
        try {
            byte[] image = Base64.getDecoder().decode(item.split(",")[1]);
            saveBytesToFile(image, "data/photos/"+id);
        } catch(RuntimeException e) {
            log.print(2, "Failed to write image.");
            throw new ResourceUpdateException();
        }
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
    public synchronized void reset() {
        log.print(1, "RESETTING DATABASE!");
        recipes = new Vector<>();
        ingredients = new Vector<>();
        utensils = new Vector<>();
        allergens = new Vector<>();
        saveRecipesToFile(recipes, RECIPES_STORE_FILE);
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
            log.print("Creating cache from " + STORE_FILE + ".");
            FileInputStream f = new FileInputStream(STORE_FILE);
            ObjectInputStream o = new ObjectInputStream(f);
            Vector<String> output = (Vector<String>) o.readObject();
            o.close();
            f.close();
            return output;
        } catch(FileNotFoundException e) {
            log.print(1, "Couldn't find " + STORE_FILE + ". Will attempt to create it on write.");
            return new Vector<>();
        } catch(IOException e) {
            log.print(2, "Error reading " + STORE_FILE + ".");
            throw new RuntimeException("Failed to read " + STORE_FILE + ".");
            //return new Vector<>();
        } catch(ClassNotFoundException e) {
            log.print(2, "Class mismatch reading from " + STORE_FILE + ".");
            return new Vector<>();
        }
    }

    /**
     * Saves a vector of strings to a file.
     * @param items Vector of strings to save
     * @param STORE_FILE Filename to save to
     */
    private synchronized void saveStringsToFile(Vector<String> items, String STORE_FILE) {
        try {
            log.print("Syncing writes to " + STORE_FILE + ".");
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(items);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            log.print(2, "Couldn't find " + STORE_FILE + " on write.");
            throw new ResourceUpdateException();
        } catch(IOException e) {
            log.print(2, "I/O error writing " + STORE_FILE + ".");
            throw new ResourceUpdateException();
        }
    }

    /**
     * Loads a vector of recipes from a file.
     * @param STORE_FILE Filename to load recipes from
     * @return Loaded recipes
     */
    private synchronized Vector<Recipe> loadRecipesFromFile(String STORE_FILE) {
        try {
            log.print("Creating cache from " + STORE_FILE + ".");
            FileInputStream f = new FileInputStream(STORE_FILE);
            ObjectInputStream o = new ObjectInputStream(f);
            Vector<Recipe> output = (Vector<Recipe>) o.readObject();
            o.close();
            f.close();
            return output;
        } catch(FileNotFoundException e) {
            log.print(1, "Couldn't find " + STORE_FILE + ". Will attempt to create it on write.");
            return new Vector<>();
        } catch(IOException e) {
            log.print(2, "I/O error reading " + STORE_FILE + ".");
            throw new RuntimeException("Failed to read " + STORE_FILE + ".");
            //return new Vector<>();
        } catch(ClassNotFoundException e) {
            log.print(2, "Class mismatch reading from " + STORE_FILE + ".");
            return new Vector<>();
        }
    }

    /**
     * Saves a vector of strings to a file.
     * @param items Vector of strings to save
     * @param STORE_FILE Filename to save to
     */
    private synchronized void saveRecipesToFile(Vector<Recipe> items, String STORE_FILE) {
        try {
            log.print("Syncing writes to " + STORE_FILE + ".");
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(items);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            log.print(2, "Couldn't find " + STORE_FILE + " on write.");
            throw new ResourceUpdateException();
        } catch(IOException e) {
            log.print(2, "I/O error writing " + STORE_FILE + ".");
            throw new ResourceUpdateException();
        }
    }

    /**
     * Loads bytes from a file.
     * @param STORE_FILE Filename to load bytes from
     * @return The loaded bytes
     */
    private synchronized byte[] loadBytesFromFile(String STORE_FILE) {
        try {
            File f = new File(STORE_FILE);
            FileInputStream fis = new FileInputStream(f);
            byte[] fb = new byte[(int) f.length()];
            fis.read(fb);
            fis.close();
            return fb;
        } catch(FileNotFoundException e) {
            log.print(1, "Couldn't find " + STORE_FILE + ". Will attempt to create it on write.");
            return new byte[0];
        } catch(IOException e) {
            log.print(2, "Error reading " + STORE_FILE + ".");
            throw new RuntimeException("Failed to read " + STORE_FILE + ".");
            //return new byte[0];
        }
    }

    /**
     * Saves bytes to a file.
     * @param item Bytes to save
     * @param STORE_FILE Filename to save to
     */
    private synchronized void saveBytesToFile(byte[] item, String STORE_FILE) {
        try {
            log.print("Writing " + STORE_FILE + ".");
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            f.write(item);
            f.close();
        } catch(FileNotFoundException e) {
            log.print(2, "Couldn't find " + STORE_FILE + " on write.");
            throw new ResourceUpdateException();
        } catch(IOException e) {
            log.print(2, "I/O error writing " + STORE_FILE + ".");
            throw new ResourceUpdateException();
        }
    }

    /**
     * Deletes a file.
     * @param STORE_FILE File to delete
     */
    private synchronized void deleteFile(String STORE_FILE) {
        File f = new File(STORE_FILE);
        if(f.delete()) { return; }
        log.print(2, "I/O error deleting " + STORE_FILE + ".");
    }

    /**
     * Cleans up unused photos from disk.
     */
    private synchronized void cleanupPhotos() {
        log.print(0, "Cleaning up photo database...");
        Vector<String> recipe_ids = new Vector<>();
        for(Recipe i : recipes) {
            recipe_ids.add(i.getID());
        }
        File[] files = (new File("data/photos/")).listFiles();
        if(files == null || files.length == 0) { return; }
        int n_photos = 0;
        for(File i : files) { if(i.isFile()) { n_photos++; } }
        if(n_photos <= recipe_ids.size()) { return; }
        log.print(1, "Found unused photos.");
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
                    log.print(0, "Deleting unused photo " + i.getName());
                    if(!i.delete()) {
                        log.print(1, "Failed to delete unused photo " + i.getName());
                    }
                }
            }
        }
    }
}
