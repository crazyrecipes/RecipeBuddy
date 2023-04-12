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

	/* ===== Database Tests ===== */

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
			assert dbc.readUtensils().size() == 2;
		} catch(AssertionError e) {
			log.print(2, "Adding utensils to database failed!");
		}
		try {
			assert dbc.readAllergens().size() == 2;
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

	@Test
	void fillDatabase() {
		Log log = new Log("Tester");
		log.print("Filling database with mock data.");

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

		log.print("Populating database with mock content...");

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

		dbc.writeIngredients(test_ingredients);
		dbc.writeUtensils(test_utensils);
		dbc.writeAllergens(test_allergens);

		Random random = new Random();
		for(int i = 0; i < 100; i++) {
			Recipe test_recipe = new Recipe();
			long n = random.nextLong(0, (int) Math.pow(2, 63) - 1);
			test_recipe.setName("Test Recipe" + n);
			test_recipe.setDesc("This recipe was automatically generated to test the database. This is test recipe " + n);
			test_recipe.setRating(random.nextDouble(0, 5));
			test_recipe.setCooked(random.nextInt(0, 1000));
			test_recipe.setIngredients(test_ingredients);
			test_recipe.setAllergens(test_allergens);
			test_recipe.setUtensils(test_utensils);
			Recipe r = dbc.createRecipe(test_recipe);
			dbc.writePhoto(RecipeBuddyMap.FALLBACK_THUMBNAIL, r.getID());
		}

		log.print("Database fill completed.");
	}

	/* ===== End Database Tests ===== */
}
