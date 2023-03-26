package org.crazyrecipes.recipebuddy.recipe;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Recipe represents a recipe with a name and description.
 */
public class Recipe implements Serializable {

    private String id;
    private String name;
    private String desc;

    /**
     * Constructs a Recipe.
     */
    public Recipe() {
        this.id = "" + new Random().nextLong();
        this.name = "";
        this.desc = "";
    }

    /**
     * Constructs a Recipe with the given name.
     * @param name The Recipe's name
     */
    public Recipe(String name) {
        this.id = "" + new Random().nextLong();
        this.name = name;
        this.desc = "";
    }

    /**
     * Constructs a Recipe with the given name and description
     * @param name The Recipe's name
     * @param desc The Recipe's description
     */
    public Recipe(String name, String desc) {
        this.id = "" + new Random().nextLong();
        this.name = name;
        this.desc = desc;
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
     * @param desc this User's description
     */
    public void setDesc(String desc) {
        this.desc = desc;
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
