package org.crazyrecipes.recipebuddy;

import org.crazyrecipes.recipebuddy.database.DatabaseController;
import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Vector;

/**
 * Tests the functionality and integrity of database operations.
 */
@SpringBootTest
class DatabaseTests {
    /**
     * Tests reading and writing ingredients
     */
    @Test
    public void testIngredients() {
        Log log = new Log("DatabaseTests");
        log.print("===== Beginning ingredients test. =====");

        log.print("Instantiating a DatabaseController...");
        DatabaseController dbc = new DatabaseController();

        log.print("Resetting database...");
        dbc.reset();

        log.print("Checking ingredient cache...");
        assert dbc.readIngredients().size() == 0;

        log.print("Populating database with test content...");
        Vector<String> test_ingredients = new Vector<>();
        test_ingredients.add("Burger Buns");
        test_ingredients.add("Ground Beef");
        test_ingredients.add("American Cheese");
        dbc.writeIngredients(test_ingredients);

        log.print("Checking ingredients integrity...");
        assert dbc.readIngredients().equals(test_ingredients);

        log.print("Deleting ingredients...");
        test_ingredients = new Vector<>();
        dbc.writeIngredients(test_ingredients);

        log.print("Checking for proper deletion of ingredients...");
        assert dbc.readIngredients().size() == 0;

        log.print("===== Database ingredients test passed. =====");
    }

    /**
     * Tests reading and writing utensils
     */
    @Test
    public void testUtensils() {
        Log log = new Log("DatabaseTests");
        log.print("===== Beginning database utensils test. =====");

        log.print("Instantiating a DatabaseController...");
        DatabaseController dbc = new DatabaseController();

        log.print("Resetting database...");
        dbc.reset();

        log.print("Checking utensil cache...");
        assert dbc.readUtensils().size() == 0;

        log.print("Populating database with test content...");
        Vector<String> test_utensils = new Vector<>();
        test_utensils.add("Frying Pan");
        test_utensils.add("Spatula");
        dbc.writeUtensils(test_utensils);
        log.print("Checking utensils integrity...");
        assert dbc.readUtensils().equals(test_utensils);

        log.print("Deleting utensils...");
        test_utensils = new Vector<>();
        dbc.writeUtensils(test_utensils);

        log.print("Checking for proper deletion of utensils...");
        assert dbc.readUtensils().size() == 0;

        log.print("===== Database utensils test passed. =====");
    }

    /**
     * Tests reading and writing allergens
     */
    @Test
    public void testAllergens() {
        Log log = new Log("DatabaseTests");
        log.print("===== Beginning database allergens test. ====");

        log.print("Instantiating a DatabaseController...");
        DatabaseController dbc = new DatabaseController();

        log.print("Resetting database...");
        dbc.reset();

        log.print("Checking allergen cache...");
        assert dbc.readAllergens().size() == 0;

        log.print("Populating database with test content...");
        Vector<String> test_allergens = new Vector<>();
        test_allergens.add("Gluten");
        test_allergens.add("Dairy");
        dbc.writeAllergens(test_allergens);

        log.print("Checking allergens integrity...");
        assert dbc.readAllergens().equals(test_allergens);

        log.print("Deleting allergens...");
        test_allergens = new Vector<>();
        dbc.writeAllergens(test_allergens);

        log.print("Checking for proper deletion of allergens...");
        assert dbc.readAllergens().size() == 0;

        log.print("===== Database allergens test passed. ====");
    }

    /**
     * Tests reading, writing, and deleting Recipes
     */
    @Test
    public void testRecipes() {
        Log log = new Log("DatabaseTests");
        log.print("===== Beginning database Recipes test. =====");

        log.print("Instantiating a DatabaseController...");
        DatabaseController dbc = new DatabaseController();

        log.print("Resetting database...");
        dbc.reset();

        log.print("Checking recipe cache...");
        assert dbc.getRecipes().size() == 0;

        log.print("Database reset succeeded.");

        log.print("Populating database with test content...");
        Vector<String> test_ingredients = new Vector<>();
        test_ingredients.add("Burger Buns");
        test_ingredients.add("Ground Beef");
        test_ingredients.add("American Cheese");
        Vector<String> test_utensils = new Vector<>();
        test_utensils.add("Frying Pan");
        test_utensils.add("Spatula");
        Vector<String> test_allergens = new Vector<>();
        test_allergens.add("Gluten");
        test_allergens.add("Dairy");
        Recipe test_recipe = new Recipe();
        test_recipe.setName("The Test Burger");
        test_recipe.setDesc("This burger is generated when the testDatabase component test is executed.");
        test_recipe.setRating(4.5);
        test_recipe.setCooked(2);
        test_recipe.setIngredients(test_ingredients);
        test_recipe.setAllergens(test_allergens);
        test_recipe.setUtensils(test_utensils);
        dbc.createRecipe(test_recipe);

        log.print("Checking recipe integrity after create...");
        assert dbc.getRecipes().get(0).equals(test_recipe);

        log.print("Editing recipe...");
        test_recipe = dbc.getRecipes().get(0);
        test_recipe.setName("The Test Burrito");
        test_recipe.setDesc("Now a completely new recipe!");
        test_recipe.setCooked(3);
        test_recipe.setRating(3.9);
        dbc.editRecipe(test_recipe.getID(), test_recipe);

        log.print("Checking recipe integrity after edit...");
        assert dbc.getRecipes().get(0).equals(test_recipe);

        log.print("Deleting recipe...");
        dbc.deleteRecipe(test_recipe.getID());

        log.print("Checking for proper deletion of Recipes...");
        assert dbc.getRecipes().size() == 0;

        log.print("===== Database Recipes test passed. =====");
    }

    /**
     * Preloads database.
     */
    @Test
    public void testPreload() {
        Log log = new Log("DatabaseTests");
        log.print("===== Beginning database preload... =====");

        log.print("Instantiating a DatabaseController...");
        DatabaseController dbc = new DatabaseController();

        log.print("Resetting database...");
        dbc.reset();

        log.print("Checking recipe cache...");
        assert dbc.getRecipes().size() == 0;

        log.print("Database reset succeeded.");

        log.print("Populating database with test content...");
        Vector<String> ingredients;
        Vector<String> utensils;
        Vector<String> allergens;
        Vector<String> steps;
        Vector<String> tags;
        Recipe recipe;

        /* ===== FIRST PRELOADED RECIPE ===== */

        /* Reset for new recipe */
        ingredients = new Vector<>();
        utensils = new Vector<>();
        allergens = new Vector<>();
        steps = new Vector<>();
        tags = new Vector<>();
        recipe = new Recipe();
        /* Populate ingredients */
        ingredients.add("Burger Buns");
        ingredients.add("Ground Beef");
        ingredients.add("Swiss Cheese");
        ingredients.add("Tomato");
        ingredients.add("Lettuce");
        ingredients.add("Butter");
        ingredients.add("Bacon");
        ingredients.add("Mayonnaise");
        /* Populate utensils */
        utensils.add("Frying Pan");
        utensils.add("Spatula");
        /* Populate allergens */
        allergens.add("Gluten");
        allergens.add("Dairy");
        /* Populate steps */
        steps.add("Form ground beef into 1/4 lb patty. Season to taste.");
        steps.add("Crisp buns in pan with light butter.");
        steps.add("Sear patty in pan with light butter for 05:00 or until cooked to your preference.");
        steps.add("Crisp up bacon in pan with leftover butter and juice from the patty.");
        steps.add("Add lettuce, tomato, bacon, and mayonnaise.");
        steps.add("Serve.");
        /* Populate tags */
        tags.add("burger");
        tags.add("simple");
        tags.add("preloaded");
        /* Populate parameters */
        recipe.setName("The RecipeBuddy BLT Burger");
        recipe.setDesc("A simple example recipe to show what RecipeBuddy can do.");
        recipe.setRating(4.5);
        recipe.setCooked(0);
        /* Write recipe */
        recipe.setIngredients(ingredients);
        recipe.setAllergens(allergens);
        recipe.setUtensils(utensils);
        recipe.setSteps(steps);
        recipe.setTags(tags);
        dbc.createRecipe(recipe);

        /* ===== END FIRST PRELOADED RECIPE ===== */

        log.print("===== Database preload completed. =====");
    }
}
