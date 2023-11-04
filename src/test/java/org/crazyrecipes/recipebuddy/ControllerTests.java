package org.crazyrecipes.recipebuddy;

import org.crazyrecipes.recipebuddy.database.DatabaseController;
import org.crazyrecipes.recipebuddy.error.NotFoundException;
import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Vector;

/**
 * Tests the functionality and integrity of operations in RecipeBuddyController.
 */
@SpringBootTest
class ControllerTests {
    /**
     * Tests reading and writing ingredients
     */
    @Test
    public void testIngredients() throws IOException {
        Log log = new Log("ControllerTests");
        log.print("Beginning controller ingredients test.");

        log.print("Resetting DB...");
        DatabaseController db = new DatabaseController();
        db.reset();

        log.print("Instantiating a RecipeBuddyController...");
        RecipeBuddyController rbc = new RecipeBuddyController();

        log.print("Checking ingredient cache...");
        assert rbc.readIngredients().isEmpty();

        log.print("Populating with test content...");
        Vector<String> test_ingredients = new Vector<>();
        test_ingredients.add("Burger Buns");
        test_ingredients.add("Ground Beef");
        test_ingredients.add("American Cheese");
        rbc.updateIngredients(test_ingredients);

        log.print("Checking ingredients integrity...");
        assert rbc.readIngredients().equals(test_ingredients);

        log.print("Deleting ingredients...");
        test_ingredients = new Vector<>();
        rbc.updateIngredients(test_ingredients);

        log.print("Checking for proper deletion of ingredients...");
        assert rbc.readIngredients().isEmpty();

        log.print("Controller ingredients test passed.");
    }

    /**
     * Tests reading and writing utensils
     */
    @Test
    public void testUtensils() throws IOException {
        Log log = new Log("ControllerTests");
        log.print("Beginning controller utensils test.");

        log.print("Resetting DB...");
        DatabaseController db = new DatabaseController();
        db.reset();

        log.print("Instantiating a RecipeBuddyController...");
        RecipeBuddyController rbc = new RecipeBuddyController();

        log.print("Checking utensil cache...");
        assert rbc.readUtensils().isEmpty();

        log.print("Populating with test content...");
        Vector<String> test_utensils = new Vector<>();
        test_utensils.add("Frying Pan");
        test_utensils.add("Spatula");
        rbc.updateUtensils(test_utensils);

        log.print("Checking utensils integrity...");
        assert rbc.readUtensils().equals(test_utensils);

        log.print("Deleting utensils...");
        test_utensils = new Vector<>();
        rbc.updateUtensils(test_utensils);

        log.print("Checking for proper deletion of utensils...");
        assert rbc.readUtensils().isEmpty();

        log.print(" Controller utensils test passed. ");
    }

    /**
     * Tests reading and writing allergens
     */
    @Test
    public void testAllergens() throws IOException {
        Log log = new Log("ControllerTests");
        log.print("Beginning controller allergens test.");

        log.print("Resetting DB...");
        DatabaseController db = new DatabaseController();
        db.reset();

        log.print("Instantiating a RecipeBuddyController...");
        RecipeBuddyController rbc = new RecipeBuddyController();

        log.print("Checking allergen cache...");
        assert rbc.readAllergens().isEmpty();

        log.print("Populating with test content...");
        Vector<String> test_allergens = new Vector<>();
        test_allergens.add("Gluten");
        test_allergens.add("Dairy");
        rbc.updateAllergens(test_allergens);

        log.print("Checking allergens integrity...");
        assert rbc.readAllergens().equals(test_allergens);

        log.print("Deleting allergens...");
        test_allergens = new Vector<>();
        rbc.updateAllergens(test_allergens);

        log.print("Checking for proper deletion of allergens...");
        assert rbc.readAllergens().isEmpty();

        log.print("Controller allergens test passed.");
    }

    /**
     * Tests reading, writing, and deleting Recipes
     */
    @Test
    public void testRecipes() throws IOException {
        Log log = new Log("ControllerTests");
        log.print("Beginning controller Recipes test.");

        log.print("Resetting DB...");
        DatabaseController db = new DatabaseController();
        db.reset();

        log.print("Instantiating a RecipeBuddyController...");
        RecipeBuddyController rbc = new RecipeBuddyController();

        log.print("Checking recipe cache...");
        assert rbc.readRecipes().isEmpty();

        log.print("Database reset succeeded.");

        log.print("Populating with test content...");
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
        rbc.createRecipe(test_recipe);

        log.print("Checking recipe integrity after create...");
        assert rbc.readRecipes().get(0).equals(test_recipe);

        log.print("Finding recipe...");
        test_recipe = rbc.readRecipes().get(0);
        test_recipe = rbc.readRecipe(test_recipe.getID());

        log.print("Editing recipe...");
        test_recipe.setName("The Test Burrito");
        test_recipe.setDesc("Now a completely new recipe!");
        test_recipe.setCooked(3);
        test_recipe.setRating(3.9);
        rbc.updateRecipe(test_recipe, test_recipe.getID());

        log.print("Checking recipe integrity after edit...");
        assert rbc.readRecipes().get(0).equals(test_recipe);

        log.print("Deleting recipe...");
        rbc.deleteRecipe(test_recipe.getID());

        log.print("Checking for proper deletion of Recipes...");
        assert rbc.readRecipes().isEmpty();

        log.print("Controller Recipes test passed.");
    }

    /**
     * Tests reading and writing photos
     */
    @Test
    public void testPhotos() throws IOException {
        Log log = new Log("ControllerTests");
        log.print("Beginning controller photos test.");

        log.print("Resetting DB...");
        DatabaseController db = new DatabaseController();
        db.reset();

        log.print("Instantiating a RecipeBuddyController...");
        RecipeBuddyController rbc = new RecipeBuddyController();

        log.print("Populating with test content...");
        Recipe r = rbc.createRecipe(new Recipe());
        String photo_id = r.getID();

        log.print("Checking for presence of photo...");
        rbc.getPhoto(photo_id);

        log.print("Deleting associated recipe...");
        rbc.deleteRecipe(r.getID());

        log.print("Checking for proper deletion of photo...");
        boolean photo_deleted = false;
        try {
            rbc.getPhoto(photo_id);
        } catch(NotFoundException e) {
            photo_deleted = true;
        }
        assert photo_deleted;

        log.print("Controller photos test passed.");
    }
}
