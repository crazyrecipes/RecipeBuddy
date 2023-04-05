package org.crazyrecipes.recipebuddy.database;

import org.crazyrecipes.recipebuddy.recipe.*;
import org.crazyrecipes.recipebuddy.ingredient.*;
import org.crazyrecipes.recipebuddy.utensil.*;
import org.crazyrecipes.recipebuddy.allergens.*;

import java.io.*;
import java.util.Collections;
import java.util.Vector;
import java.util.List;

public class Database {

    private Vector<Recipe> recipes;
    private Vector<String> ingredients;
    private Vector<String> utensils;
    private Vector<String> allergens;
    private final String storeFileRecipes="recipes.dat";
    private final String storeFileIngredients="ingredients.dat";
    private final String storeFileUtensils="utensils.dat";
    private final String storeFileAllergens="allergens.dat";

    Database(){
        recipes=loadRecipesFromFile(storeFileRecipes);
        ingredients=loadFromFile(storeFileIngredients);
        utensils=loadFromFile(storeFileUtensils);
        allergens=loadFromFile(storeFileAllergens);
    }

    public synchronized List<Recipe> getRecipes(){
        return Collections.list(recipes.elements());
    }

    public synchronized List<String> getIngredients(){
        return ingredients;
    }

    public synchronized List<String> getUtensils(){
        return utensils;
    }

    public synchronized List<String> getAllergens(){
        return allergens;
    }

    private synchronized Vector<String> loadFromFile(final String STORE_FILE) {
        try {
            FileInputStream f = new FileInputStream(STORE_FILE);
            ObjectInputStream o = new ObjectInputStream(f);
            Vector<String> output = (Vector<String>) o.readObject();
            o.close();
            f.close();
            return output;
        } catch(FileNotFoundException e) {
            System.err.println("Database: FileNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(IOException e) {
            System.err.println("Database: IOException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(ClassNotFoundException e) {
            System.err.println("Database: ClassNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        }
    }

    private synchronized void saveToFile(Vector<String> ingredients, final String STORE_FILE) {
        try {
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(ingredients);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            System.err.println("Database: FileNotFoundException upon writing " + STORE_FILE);
        } catch(IOException e) {
            System.err.println("Database: IOException upon writing " + STORE_FILE);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized Vector<Recipe> loadRecipesFromFile(final String STORE_FILE) {
        try {
            FileInputStream f = new FileInputStream(STORE_FILE);
            ObjectInputStream o = new ObjectInputStream(f);
            Vector<Recipe> output = (Vector<Recipe>) o.readObject();
            o.close();
            f.close();
            return output;
        } catch(FileNotFoundException e) {
            System.err.println("Database: FileNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(IOException e) {
            System.err.println("Database: IOException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(ClassNotFoundException e) {
            System.err.println("Database: ClassNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        }

    }

    private synchronized void saveRecipesToFile(Vector<Recipe> recipes, final String STORE_FILE) {
        try {
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(recipes);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            System.err.println("Database: FileNotFoundException upon writing " + STORE_FILE);
        } catch(IOException e) {
            System.err.println("Database: IOException upon writing " + STORE_FILE);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
