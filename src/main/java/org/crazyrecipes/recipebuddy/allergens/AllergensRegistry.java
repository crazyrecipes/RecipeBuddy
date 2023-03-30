package org.crazyrecipes.recipebuddy.allergens;

import java.io.*;
import java.util.List;
import java.util.Vector;

/**
 * AllergensRegistry handles the storage and retrieval of allergens.
 * THIS IS A SIMPLE, INEFFICIENT VERSION FOR TESTING.
 */
public class AllergensRegistry {
    private final String STORE_FILE = "allergens.dat";

    public AllergensRegistry() {
        Vector<String> allergens = loadFromFile();
        if(allergens.size() < 1) { // if no allergens could be loaded
            allergens.add("DEFAULT");
            saveToFile(allergens);
        }
    }

    /**
     * Loads allergens from a file.
     * @return allergens loaded from the file
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
            System.err.println("AllergensRegistry: FileNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(IOException e) {
            System.err.println("AllergensRegistry: IOException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(ClassNotFoundException e) {
            System.err.println("AllergensRegistry: ClassNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        }

    }

    /**
     * Saves allergens to a file.
     * @param allergens Allergens to save to the file
     */
    private synchronized void saveToFile(Vector<String> allergens) {
        try {
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(allergens);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            System.err.println("AllergensRegistry: FileNotFoundException upon writing " + STORE_FILE);
        } catch(IOException e) {
            System.err.println("AllergensRegistry: IOException upon writing " + STORE_FILE);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets allergens.
     * @return List of allergens
     */
    public synchronized List<String> getAllergens() {
        Vector<String> allergens = loadFromFile();
        return allergens;
    }

    /**
     * Posts allergens.
     * @param newAllergens List of allergens
     */
    public synchronized List<String> postAllergens(List<String> newAllergens) {
        Vector<String> allergens = new Vector<String>();
        for(String i : newAllergens) {
            allergens.add(i);
        }
        saveToFile(allergens);
        return allergens;
    }
}
