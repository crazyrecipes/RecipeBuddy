package org.crazyrecipes.recipebuddy.recipe;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Recipe stores a single recipe. Each recipe has an ID, a name, a description,
 *   a rating, a times cooked counter, a list of ingredients,
 *   a list of utensils, a list of allergens, a list of steps, and a list of tags.
 */
@SuppressWarnings("unused")
public class Recipe {
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
    private final Vector<String> ingredients;

    /**
     * The Recipe's utensils
     */
    private final Vector<String> utensils;

    /**
     * The Recipe's allergens
     */
    private final Vector<String> allergens;

    /**
     * The steps to make the Recipe
     */
    private final Vector<String> steps;

    /**
     * The Recipe's tags
     */
    private final Vector<String> tags;

    /**
     * Regex used to remove invalid characters from input
     */
    private final String SANITIZER_REGEX = "[^a-zA-Z0-9¿-ÿ° !.,?:;'#$%^*()/_+-]";

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
        this.ingredients = new Vector<>();
        this.utensils = new Vector<>();
        this.allergens = new Vector<>();
        this.steps = new Vector<>();
        this.tags = new Vector<>();
    }

    /**
     * Constructs a Recipe from a list of lines.
     * @param lines List of lines to construct Recipe with
     * @throws IOException if parsing the input lines fails
     */
    public Recipe(List<String> lines) throws IOException {
        int header_index;
        try {
            this.id = lines.get(0).split("id=", 2)[1];
            this.name = lines.get(1).split("name=", 2)[1];
            this.desc = lines.get(2).split("desc=", 2)[1];
            this.rating = Double.parseDouble(lines.get(3).split("rating=", 2)[1]);
            this.cooked = Integer.parseInt(lines.get(4).split("cooked=", 2)[1]);
            header_index = 5;

            this.ingredients = parseList(lines, header_index, "ingredients:");
            header_index += this.ingredients.size() + 1;

            this.utensils = parseList(lines, header_index, "utensils:");
            header_index += this.utensils.size() + 1;

            this.allergens = parseList(lines, header_index, "allergens:");
            header_index += this.allergens.size() + 1;

            this.steps = parseList(lines, header_index, "steps:");
            header_index += this.steps.size() + 1;

            this.tags = parseList(lines, header_index, "tags:");
        } catch(IndexOutOfBoundsException e) {
            throw new IOException("Failed to read recipe (Bad syntax). Lines: " + Arrays.toString(lines.toArray()));
        } catch(NumberFormatException e) {
            throw new IOException("Failed to read recipe (Bad value). Lines: " + Arrays.toString(lines.toArray()));
        }
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
            String j = i.replaceAll(SANITIZER_REGEX, "");
            if(j.length() > 1) { this.ingredients.add(j); }
        }
    }

    /**
     * Sets this Recipe's utensils.
     * @param utensils this Recipe's utensils
     */
    public void setUtensils(List<String> utensils) {
        this.utensils.clear();
        for(String i : utensils) {
            String j = i.replaceAll(SANITIZER_REGEX, "");
            if(j.length() > 1) { this.utensils.add(j); }
        }
    }

    /**
     * Sets this Recipe's allergens.
     * @param allergens this Recipe's allergens
     */
    public void setAllergens(List<String> allergens) {
        this.allergens.clear();
        for(String i : allergens) {
            String j = i.replaceAll(SANITIZER_REGEX, "");
            if(j.length() > 1) { this.allergens.add(j); }
        }
    }

    /**
     * Sets this Recipe's steps.
     * @param steps this Recipe's steps
     */
    public void setSteps(List<String> steps) {
        this.steps.clear();
        for(String i : steps) {
            String j = i.replaceAll(SANITIZER_REGEX, "");
            if(j.length() > 1) { this.steps.add(j); }
        }
    }

    /**
     * Sets this Recipe's tags.
     * @param tags this Recipe's tags
     */
    public void setTags(List<String> tags) {
        this.tags.clear();
        for(String i : tags) {
            String j = i.replaceAll(SANITIZER_REGEX, "");
            if(j.length() > 1) { this.tags.add(j); }
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
     * Parses a list-type parameter for deserialization.
     * @param lines List of lines to parse
     * @param header_index Index of the list header
     * @param header_prefix Prefix of the list header
     * @return List elements
     */
    private Vector<String> parseList(List<String> lines, int header_index, String header_prefix) {
        int n_items = Integer.parseInt(lines.get(header_index).split(header_prefix, 2)[1]);
        Vector<String> items = new Vector<>();
        for(int i = header_index + 1; i <= header_index + n_items; i++) {
            items.add(lines.get(i));
        }
        return items;
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
        StringBuilder sb = new StringBuilder();

        sb.append("id=").append(this.id).append("\n");
        sb.append("name=").append(this.name).append("\n");
        sb.append("desc=").append(this.desc).append("\n");
        sb.append("rating=").append(this.rating).append("\n");
        sb.append("cooked=").append(this.cooked).append("\n");

        sb.append("ingredients:").append(this.ingredients.size()).append("\n");
        for(String i : this.ingredients) { sb.append(i).append("\n"); }

        sb.append("utensils:").append(this.utensils.size()).append("\n");
        for(String i : this.utensils) { sb.append(i).append("\n"); }

        sb.append("allergens:").append(this.allergens.size()).append("\n");
        for(String i : this.allergens) { sb.append(i).append("\n"); }

        sb.append("steps:").append(this.steps.size()).append("\n");
        for(String i : this.steps) { sb.append(i).append("\n"); }

        sb.append("tags:").append(this.tags.size()).append("\n");
        for(String i : this.tags) { sb.append(i).append("\n"); }

        sb.append("\f\n");
        return sb.toString();
    }
}
