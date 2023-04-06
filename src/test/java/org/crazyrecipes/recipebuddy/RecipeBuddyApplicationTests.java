package org.crazyrecipes.recipebuddy;

import org.crazyrecipes.recipebuddy.database.DatabaseController;
import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.Vector;

@SpringBootTest
class RecipeBuddyApplicationTests {

	@Test
	void testExample() {
		System.out.println("This is an example test.");
		try {
			assert 2 + 2 == 4;
		} catch(AssertionError e) {
			System.err.println("Uh-oh!");
		}
	}

	@Test
	void testDatabase() {
		Log log = new Log("Tester");
		log.print("Beginning database test.");

		log.print("Instantiating a DatabaseController...");
		DatabaseController dbc = new DatabaseController();

		log.print("Resetting database...");
		dbc.reset();

		try {
			assert dbc.getRecipes().size() == 0;
		} catch(AssertionError e) {
			log.print(2, "Resetting recipe database failed!");
		}
		try {
			assert dbc.readIngredients().size() == 0;
		} catch(AssertionError e) {
			log.print(2, "Resetting ingredient database failed!");
		}
		try {
			assert dbc.readUtensils().size() == 0;
		} catch(AssertionError e) {
			log.print(2, "Resetting utensil database failed!");
		}
		try {
			assert dbc.readAllergens().size() == 0;
		} catch(AssertionError e) {
			log.print(2, "Resetting allergen database failed!");
		}

		log.print("Showing content...");
		log.print("Recipes:");
		System.out.println(dbc.getRecipes());
		log.print("Ingredients:");
		System.out.println(dbc.readIngredients());
		log.print("Utensils:");
		System.out.println(dbc.readUtensils());
		log.print("Allergens:");
		System.out.println(dbc.readAllergens());

		log.print("Populating database with test content...");
		Recipe test_recipe = new Recipe();
		test_recipe.setName("Test recipe 1");
		test_recipe.setDesc("Test desc");
		Vector<String> test_ingredients = new Vector<>();
		test_ingredients.add("Test ingredient 1");
		test_ingredients.add("Test ingredient 2");
		test_ingredients.add("Test ingredient 3");
		Vector<String> test_utensils = new Vector<>();
		test_utensils.add("Test utensil 1");
		test_utensils.add("Test utensil 2");
		test_utensils.add("Test utensil 3");
		Vector<String> test_allergens = new Vector<>();
		test_allergens.add("Test allergen 1");
		test_allergens.add("Test allergen 2");
		test_allergens.add("Test allergen 3");

		dbc.createRecipe(test_recipe);
		dbc.writeIngredients(test_ingredients);
		dbc.writeUtensils(test_utensils);
		dbc.writeAllergens(test_allergens);

		try {
			assert dbc.getRecipes().size() == 1;
		} catch(AssertionError e) {
			log.print(2, "Adding recipe to database failed!");
		}
		try {
			assert dbc.readIngredients().size() == 3;
		} catch(AssertionError e) {
			log.print(2, "Adding ingredients to database failed!");
		}
		try {
			assert dbc.readUtensils().size() == 3;
		} catch(AssertionError e) {
			log.print(2, "Adding utensils to database failed!");
		}
		try {
			assert dbc.readAllergens().size() == 3;
		} catch(AssertionError e) {
			log.print(2, "Adding allergens to database failed!");
		}

		log.print("Showing content...");
		log.print("Recipes:");
		System.out.println(dbc.getRecipes());
		log.print("Ingredients:");
		System.out.println(dbc.readIngredients());
		log.print("Utensils:");
		System.out.println(dbc.readUtensils());
		log.print("Allergens:");
		System.out.println(dbc.readAllergens());

		log.print("Database test completed.");
	}
}
