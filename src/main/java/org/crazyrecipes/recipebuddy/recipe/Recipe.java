package org.crazyrecipes.recipebuddy.recipe;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

/**
 * Recipe stores a single recipe. Each recipe has an ID, a name, a description,
 *   a rating, a times cooked counter, a list of ingredients,
 *   a list of utensils, a list of allergens, a list of steps, and a list of tags.
 */
public class Recipe implements Serializable {
    @Serial
    private static final long serialVersionUID = 1802001L;
    /**
     * The Recipe's unique ID
     */
    private String id;

    /**
     * The Recipe's name
     */
    private String name;

    /**
     * The Recipe's description
     */
    private String desc;

    /**
     * The Recipe's rating
     */
    private double rating;

    /**
     * How many times the Recipe has been cooked
     */
    private int cooked;

    /**
     * The Recipe's ingredients
     */
    private Vector<String> ingredients;

    /**
     * The Recipe's utensils
     */
    private Vector<String> utensils;

    /**
     * The Recipe's allergens
     */
    private Vector<String> allergens;

    /**
     * The steps to make the Recipe
     */
    private Vector<String> steps;

    /**
     * The Recipe's tags
     */
    private Vector<String> tags;

    /**
     * Regex used to remove invalid characters from input
     */
    private final String SANITIZER_REGEX = "[^a-zA-Z0-9¿-ÿ !.,?:;'#$%^*()/_+-]";

    /**
     * Constructs a Recipe.
     */
    public Recipe() {
        /* Generate ID */
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 4; i++) {
            long n = random.nextLong(0, (int) Math.pow(2, 63) - 1);
            sb.append(n);
        }
        this.id = sb.toString();
        this.name = "";
        this.desc = "";
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
     * Returns this Recipe's rating.
     * @return this Recipe's rating
     */
    public double getRating() { return this.rating; }

    /**
     * Returns this Recipe's times cooked counter.
     * @return this Recipe's times cooked counter
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
        this.id = id.replaceAll(SANITIZER_REGEX,"");
    }

    /**
     * Sets this Recipe's name.
     * @param name this Recipe's name
     */
    public void setName(String name) {
        this.name = name.replaceAll(SANITIZER_REGEX,"");
    }

    /**
     * Sets this Recipe's description.
     * @param desc this Recipe's description
     */
    public void setDesc(String desc) {
        this.desc = desc.replaceAll(SANITIZER_REGEX,"");
    }

    /**
     * Sets this Recipe's rating.
     * @param rating this Recipe's rating
     */
    public void setRating(double rating) { this.rating = rating; }

    /**
     * Sets this Recipe's times cooked counter.
     * @param cooked this Recipe's times cooked counter
     */
    public void setCooked(int cooked) { this.cooked = cooked; }

    /**
     * Sets this Recipe's ingredients.
     * @param ingredients this Recipe's ingredients
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients.clear();
        for(String i : ingredients) {
            this.ingredients.add(i.replaceAll(SANITIZER_REGEX,""));
        }
    }

    /**
     * Sets this Recipe's utensils.
     * @param utensils this Recipe's utensils
     */
    public void setUtensils(List<String> utensils) {
        this.utensils.clear();
        for(String i : utensils) {
            this.utensils.add(i.replaceAll(SANITIZER_REGEX,""));
        }
    }

    /**
     * Sets this Recipe's allergens.
     * @param allergens this Recipe's allergens
     */
    public void setAllergens(List<String> allergens) {
        this.allergens.clear();
        for(String i : allergens) {
            this.allergens.add(i.replaceAll(SANITIZER_REGEX,""));
        }
    }

    /**
     * Sets this Recipe's steps.
     * @param steps this Recipe's steps
     */
    public void setSteps(List<String> steps) {
        this.steps.clear();
        for(String i : steps) {
            this.steps.add(i.replaceAll(SANITIZER_REGEX,""));
        }
    }

    /**
     * Sets this Recipe's tags.
     * @param tags this Recipe's tags
     */
    public void setTags(List<String> tags) {
        this.tags.clear();
        for(String i : tags) {
            this.tags.add(i.replaceAll(SANITIZER_REGEX,""));
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
        this.setRating(other.getRating());
        this.setCooked(other.getCooked());
        this.setIngredients(other.getIngredients());
        this.setUtensils(other.getUtensils());
        this.setAllergens(other.getAllergens());
        this.setSteps(other.getSteps());
        this.setTags(other.getTags());
    }

    /**
     * Increment this recipe's times cooked counter.
     */
    public void cook() {
        this.cooked++;
    }

    /**
     * Convert a vector of strings to JSON
     * @param s The vector to convert
     * @return The vector converted to JSON
     */
    private String vec_strings_to_json(Vector<String> s) {
        if(s.size() == 0) { return "[]"; }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String i : s) {
            sb.append("\"");
            sb.append(i);
            sb.append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Recipe)) {
            return false;
        }
        return Objects.equals(this.name, ((Recipe) other).getName()) &&
                Objects.equals(this.desc, ((Recipe) other).getDesc()) &&
                Objects.equals(this.rating, ((Recipe) other).getRating()) &&
                Objects.equals(this.cooked, ((Recipe) other).getCooked()) &&
                Objects.equals(this.ingredients, ((Recipe) other).getIngredients()) &&
                Objects.equals(this.utensils, ((Recipe) other).getUtensils()) &&
                Objects.equals(this.steps, ((Recipe) other).getSteps()) &&
                Objects.equals(this.allergens, ((Recipe) other).getAllergens());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.desc, this.rating, this.cooked, this.ingredients, this.allergens, this.utensils, this.steps);
    }

    @Override
    public String toString() {
        return String.format(
                """
                {
                "id":"%s",
                "name":"%s",
                "desc":"%s",
                "rating":"%f",
                "cooked":"%d",
                "ingredients":%s,
                "utensils":%s,
                "steps":%s,
                "allergens":%s
                }
                """,
                this.id,
                this.name,
                this.desc,
                this.rating,
                this.cooked,
                vec_strings_to_json(this.ingredients),
                vec_strings_to_json(this.utensils),
                vec_strings_to_json(this.steps),
                vec_strings_to_json(this.allergens)
        );
    }
}
