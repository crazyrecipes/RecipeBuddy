package org.crazyrecipes.recipebuddy.ingredients;

import java.io.*;
import java.util.List;
import java.util.Vector;

/**
 * IngredientsRegistry handles the storage and retrieval of ingredients.
 * THIS IS A SIMPLE, INEFFICIENT VERSION FOR TESTING.
 */
public class IngredientsRegistry {
    private final String STORE_FILE = "ingredients.dat";

    public IngredientsRegistry() {
        Vector<String> ingredients = loadFromFile();
        if(ingredients.size() < 1) { // if no ingredients could be loaded
            ingredients.add("DEFAULT");
            saveToFile(ingredients);
        }
    }

    /**
     * Loads ingredients from a file.
     * @return Ingredients loaded from the file
     */
    private synchronized Vector<String> loadFromFile() {
        try {
            FileInputStream f = new FileInputStream(STORE_FILE);
            ObjectInputStream o = new ObjectInputStream(f);
            Vector<String> output = (Vector<String>) o.readObject();
            o.close();
            f.close();
            return output;
        } catch(FileNotFoundException e) {
            System.err.println("IngredientsRegistry: FileNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(IOException e) {
            System.err.println("IngredientsRegistry: IOException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(ClassNotFoundException e) {
            System.err.println("IngredientsRegistry: ClassNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        }
    }

    /**
     * Saves ingredients to a file.
     * @param ingredients Ingredients to save to the file
     */
    private synchronized void saveToFile(Vector<String> ingredients) {
        try {
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(ingredients);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            System.err.println("IngredientsRegistry: FileNotFoundException upon writing " + STORE_FILE);
        } catch(IOException e) {
            System.err.println("IngredientsRegistry: IOException upon writing " + STORE_FILE);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets ingredients.
     * @return List of ingredients
     */
    public synchronized List<String> getIngredients() {
        Vector<String> ingredients = loadFromFile();
        return ingredients;
    }

    /**
     * Posts ingredients.
     * @param newIngredients List of ingredients
     */
    public synchronized List<String> postIngredients(List<String> newIngredients) {
        Vector<String> ingredients = new Vector<String>();
        for(String i : newIngredients) {
            ingredients.add(i);
        }
        saveToFile(ingredients);
        return ingredients;
    }
}
