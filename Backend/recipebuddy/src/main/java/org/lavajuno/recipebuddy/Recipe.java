package org.lavajuno.recipebuddy;

public class Recipe {
    private long id;
    private String name;
    private String desc;

    public Recipe(long id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + this.id + ", name='" + this.name + ", desc=" + this.desc + "'}";
    }

}
