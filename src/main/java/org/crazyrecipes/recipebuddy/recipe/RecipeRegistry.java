package org.crazyrecipes.recipebuddy.recipe;

import org.crazyrecipes.recipebuddy.error.NotFoundException;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * RecipeRegistry handles the storage and retrieval of recipes.
 * THIS IS A SIMPLE, INEFFICIENT VERSION FOR TESTING.
 */
public class RecipeRegistry {
    private final String STORE_FILE = "recipes.dat";
    private Vector<Recipe> recipes;

    public RecipeRegistry() {
        recipes = loadRecipesFromFile();
        if(recipes.size() < 1) { // if no recipes could be loaded
            Recipe r = new Recipe();
            r.setName("The Default Burger");
            r.setDesc("The burger that gets generated when a new database is created");
            r.setPhoto("/media/burger.jpg");
            recipes.add(r);
            saveRecipesToFile(recipes);
        }
    }

    /**
     * Loads recipes from a file.
     * @return Recipes loaded from the file
     */
    private Vector<Recipe> loadRecipesFromFile() {
        try {
            FileInputStream f = new FileInputStream(STORE_FILE);
            ObjectInputStream o = new ObjectInputStream(f);
            Vector<Recipe> output = (Vector<Recipe>) o.readObject();
            o.close();
            f.close();
            return output;
        } catch(FileNotFoundException e) {
            System.err.println("RecipeRegistry: FileNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(IOException e) {
            System.err.println("RecipeRegistry: IOException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(ClassNotFoundException e) {
            System.err.println("RecipeRegistry: ClassNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        }

    }

    /**
     * Saves recipes to a file.
     * @param recipes Recipes to save to the file
     */
    private void saveRecipesToFile(Vector<Recipe> recipes) {
        try {
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(recipes);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            System.err.println("RecipeRegistry: FileNotFoundException upon writing " + STORE_FILE);
        } catch(IOException e) {
            System.err.println("RecipeRegistry: IOException upon writing " + STORE_FILE);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets all recipes.
     * @return All recipes as a list
     */
    public List<Recipe> getAll() {
        recipes = loadRecipesFromFile();
        return Collections.list(recipes.elements());
    }

    /**
     * Gets a single recipe by ID
     * @param id Recipe's ID
     * @return The Recipe with the specified ID
     */
    public Recipe getRecipe(String id) throws NotFoundException {
        recipes = loadRecipesFromFile();
        for(Recipe i : recipes) {
            if (i.getID().equals(id)) {
                return i;
            }
        }
        throw new NotFoundException();
    }

    /**
     * Creates a new Recipe with the given instance.
     * @param newRecipe New recipe to add
     * @return The created Recipe
     */
    public synchronized Recipe createRecipe(Recipe newRecipe) {
        recipes = loadRecipesFromFile();
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.duplicate_from(newRecipe);
        recipes.add(recipeToAdd);
        saveRecipesToFile(recipes);
        return recipeToAdd;
    }

    /**
     * Updates a Recipe.
     * @param id The Recipe ID to update
     * @param recipe The Recipe to replace with
     * @return The updated Recipe
     */
    public synchronized Recipe editRecipe(String id, Recipe recipe) throws NotFoundException {
        recipes = loadRecipesFromFile();
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.set(i, recipe);
                saveRecipesToFile(recipes);
                return recipe;
            }
        }
        throw new NotFoundException();
    }

    /**
     * Deletes a Recipe.
     * @param id Recipe ID to delete
     */
    public synchronized void deleteRecipe(String id) {
        recipes = loadRecipesFromFile();
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.remove(i);
                saveRecipesToFile(recipes);
            }
        }
    }
}
