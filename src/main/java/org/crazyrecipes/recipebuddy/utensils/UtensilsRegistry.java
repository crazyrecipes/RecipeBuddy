package org.crazyrecipes.recipebuddy.utensils;

import java.io.*;
import java.util.List;
import java.util.Vector;

/**
 * UtensilsRegistry handles the storage and retrieval of utensils.
 * THIS IS A SIMPLE, INEFFICIENT VERSION FOR TESTING.
 */
public class UtensilsRegistry {
    private final String STORE_FILE = "utensils.dat";

    public UtensilsRegistry() {
        Vector<String> utensils = loadFromFile();
        if(utensils.size() < 1) { // if no utensils could be loaded
            utensils.add("DEFAULT");
            saveToFile(utensils);
        }
    }

    /**
     * Loads utensils from a file.
     * @return utensils loaded from the file
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
            System.err.println("UtensilsRegistry: FileNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(IOException e) {
            System.err.println("UtensilsRegistry: IOException upon reading " + STORE_FILE);
            return new Vector<>();
        } catch(ClassNotFoundException e) {
            System.err.println("UtensilsRegistry: ClassNotFoundException upon reading " + STORE_FILE);
            return new Vector<>();
        }
    }

    /**
     * Saves utensils to a file.
     * @param utensils Utensils to save to the file
     */
    private synchronized void saveToFile(Vector<String> utensils) {
        try {
            FileOutputStream f = new FileOutputStream(STORE_FILE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(utensils);
            o.close();
            f.close();
        } catch(FileNotFoundException e) {
            System.err.println("UtensilsRegistry: FileNotFoundException upon writing " + STORE_FILE);
        } catch(IOException e) {
            System.err.println("UtensilsRegistry: IOException upon writing " + STORE_FILE);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets utensils.
     * @return List of utensils
     */
    public synchronized List<String> getUtensils() {
        Vector<String> utensils = loadFromFile();
        return utensils;
    }

    /**
     * Posts utensils.
     * @param newUtensils List of utensils
     */
    public synchronized List<String> postUtensils(List<String> newUtensils) {
        Vector<String> utensils = new Vector<String>();
        for(String i : newUtensils) {
            utensils.add(i);
        }
        saveToFile(utensils);
        return utensils;
    }
}
