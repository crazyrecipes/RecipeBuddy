package org.crazyrecipes.recipebuddy.recipe;

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
        if(recipes.size() < 1) { // if no users could be loaded
            recipes.add(new Recipe("New Recipe", "Default Description"));
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
    public Recipe getRecipe(String id) {
        recipes = loadRecipesFromFile();
        for(Recipe i : recipes) {
            if(i.getID().equals(id)) {
                return i;
            }
        }
        return new Recipe("Not Found","Not Found");
    }

    /**
     * Creates a Recipe with the specified name
     * @param name Name of the Recipe to be created
     * @return The Recipe with the specified name
     */
    public synchronized Recipe createRecipe(String name) {
        recipes = loadRecipesFromFile();
        Recipe recipeToAdd = new Recipe(name);
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
    public synchronized Recipe editRecipe(String id, Recipe recipe) {
        recipes = loadRecipesFromFile();
        for(int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getID().equals(id)) {
                recipes.set(i, recipe);
                saveRecipesToFile(recipes);
                return recipe;
            }
        }
        return new Recipe("Not Found", "Not Found");
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
