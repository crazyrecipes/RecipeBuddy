package org.crazyrecipes.recipebuddy.recipe;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

/**
 * Recipe represents a recipe with a name and description.
 */
public class Recipe implements Serializable {

    private String id;
    private String name;
    private String desc;
    private String photo;
    private double rating;
    private int cooked;
    private Vector<String> ingredients;
    private Vector<String> utensils;
    private Vector<String> allergens;
    private Vector<String> steps;
    private Vector<String> tags;


    /**
     * Constructs a Recipe.
     */
    public Recipe() {
        this.id = "" + new Random().nextLong();
        this.name = "";
        this.desc = "";
        this.photo = "";
        this.rating = 0.0;
        this.cooked = 0;
        this.ingredients = new Vector<String>();
        this.utensils = new Vector<String>();
        this.allergens = new Vector<String>();
        this.steps = new Vector<String>();
        this.tags = new Vector<String>();
    }

    /**
     * Returns this Recipe's ID.
     * @return This Recipe's ID
     */
    public String getID() {
        return this.id;
    }

    /**
     * Returns this Recipe's name.
     * @return this Recipe's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns this Recipe's description.
     * @return this Recipe's description
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * Returns this Recipe's photo URL.
     * @return this Recipe's photo URL
     */
    public String getPhoto() { return this.photo; }

    /**
     * Returns this Recipe's rating.
     * @return this Recipe's rating
     */
    public double getRating() { return this.rating; }

    /**
     * Returns this Recipe's cooked counter.
     * @return this Recipe's cooked counter
     */
    public int getCooked() { return this.cooked; }

    /**
     * Returns this Recipe's ingredients.
     * @return this Recipe's ingredients
     */
    public List<String> getIngredients() { return this.ingredients; }

    /**
     * Returns this Recipe's utensils.
     * @return this Recipe's utensils
     */
    public List<String> getUtensils() { return this.utensils; }

    /**
     * Returns this Recipe's allergens.
     * @return this Recipe's allergens
     */
    public List<String> getAllergens() { return this.allergens; }

    /**
     * Returns this Recipe's steps.
     * @return this Recipe's steps
     */
    public List<String> getSteps() { return this.steps; }

    /**
     * Returns this Recipe's tags.
     * @return this Recipe's tags
     */
    public List<String> getTags() { return this.tags; }

    /**
     * Sets this Recipe's ID.
     * @param id this Recipe's ID
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Sets this Recipe's name.
     * @param name this Recipe's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets this Recipe's description.
     * @param desc this Recipe's description
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Sets this Recipe's photo URL.
     * @param photo this Recipe's photo URL
     */
    public void setPhoto(String photo) { this.photo = photo; }

    /**
     * Sets this Recipe's rating.
     * @param rating this Recipe's rating
     */
    public void setRating(double rating) { this.rating = rating; }

    /**
     * Sets this Recipe's cooked counter.
     * @param cooked this Recipe's cooked counter
     */
    public void setCooked(int cooked) { this.cooked = cooked; }

    /**
     * Sets this Recipe's ingredients.
     * @param ingredients this Recipe's ingredients
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients.clear();
        for(String i : ingredients) {
            this.ingredients.add(i);
        }
    }

    /**
     * Sets this Recipe's utensils.
     * @param utensils this Recipe's utensils
     */
    public void setUtensils(List<String> utensils) {
        this.utensils.clear();
        for(String i : utensils) {
            this.utensils.add(i);
        }
    }

    /**
     * Sets this Recipe's allergens.
     * @param allergens this Recipe's allergens
     */
    public void setAllergens(List<String> allergens) {
        this.allergens.clear();
        for(String i : allergens) {
            this.allergens.add(i);
        }
    }

    /**
     * Sets this Recipe's steps.
     * @param steps this Recipe's steps
     */
    public void setSteps(List<String> steps) {
        this.steps.clear();
        for(String i : steps) {
            this.steps.add(i);
        }
    }

    /**
     * Sets this Recipe's tags.
     * @param tags this Recipe's tags
     */
    public void setTags(List<String> tags) {
        this.tags.clear();
        for(String i : tags) {
            this.tags.add(i);
        }
    }

    /**
     * Copies another Recipe's parameters into this Recipe.
     * (Excludes ID)
     * @param other Recipe to duplicate into this one
     */
    public void duplicate_from(Recipe other) {
        this.setName(other.getName());
        this.setDesc(other.getDesc());
        this.setPhoto(other.getPhoto());
        this.setRating(other.getRating());
        this.setCooked(other.getCooked());
        this.setIngredients(other.getIngredients());
        this.setUtensils(other.getUtensils());
        this.setAllergens(other.getAllergens());
        this.setSteps(other.getSteps());
        this.setTags(other.getTags());
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Recipe)) {
            return false;
        }
        return Objects.equals(this.id, ((Recipe) other).getID()) &&
                Objects.equals(this.name, ((Recipe) other).getName()) &&
                Objects.equals(this.desc, ((Recipe) other).getDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.desc);
    }

    @Override
    public String toString() {
        return "User{" + "id='" + this.id + "', name='" + this.name + "', desc='" +
                this.desc + "'}";
    }
}
