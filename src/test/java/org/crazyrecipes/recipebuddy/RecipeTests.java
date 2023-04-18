package org.crazyrecipes.recipebuddy;

import org.crazyrecipes.recipebuddy.database.DatabaseController;
import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Vector;

/**
 * Tests the Recipe data structure.
 */
@SpringBootTest
class RecipeTests {

	/**
	 * Tests instantiation and comparison of Recipes.
	 */
	@Test
	public void testRecipe() {
		Log log = new Log("RecipeTests");
		log.print("===== Beginning Recipe test. =====");

		log.print("Creating first test recipe...");
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
		Recipe test_recipe1 = new Recipe();
		test_recipe1.setName("The Test Burger");
		test_recipe1.setDesc("This burger is generated when the testDatabase component test is executed.");
		test_recipe1.setRating(4.5);
		test_recipe1.setCooked(2);
		test_recipe1.setIngredients(test_ingredients);
		test_recipe1.setAllergens(test_allergens);
		test_recipe1.setUtensils(test_utensils);

		log.print("Creating second test recipe...");
		test_ingredients = new Vector<>();
		test_ingredients.add("Pulled Pork");
		test_ingredients.add("Flour Tortillas");
		test_utensils = new Vector<>();
		test_utensils.add("Skillet");
		test_utensils.add("Big Spatula");
		test_allergens = new Vector<>();
		test_allergens.add("Shellfish");
		test_allergens.add("Soy");
		Recipe test_recipe2 = new Recipe();
		test_recipe2.setName("The Test Burrito");
		test_recipe2.setDesc("Now a completely new recipe!");
		test_recipe2.setCooked(7);
		test_recipe2.setRating(4.9);
		test_recipe2.setIngredients(test_ingredients);
		test_recipe2.setUtensils(test_utensils);
		test_recipe2.setAllergens(test_allergens);

		log.print("Checking comparison...");
		assert test_recipe1.equals(test_recipe1);
		assert !test_recipe1.equals(test_recipe2);

		log.print("===== Recipe test passed. =====");
	}
}
