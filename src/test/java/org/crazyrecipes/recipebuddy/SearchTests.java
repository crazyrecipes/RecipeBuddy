package org.crazyrecipes.recipebuddy;

import org.crazyrecipes.recipebuddy.search.Search;
import org.crazyrecipes.recipebuddy.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests the Search data structure.
 */
@SpringBootTest
class SearchTests {

	/**
	 * Tests instantiation of Search.
	 */
	@Test
	public void testSearch() {
		Log log = new Log("SearchTests");
		log.print("Beginning Search test.");

		log.print("Creating test Search...");
		Search test_search = new Search("burger", "SOME", "SOME", "BLOCK");

		log.print("Checking integrity...");
		assert test_search.getQuery().equals("burger");
		assert test_search.getIngredients().equals("SOME");
		assert test_search.getUtensils().equals("SOME");
		assert test_search.getAllergens().equals("BLOCK");

		log.print("Search test passed.");
	}
}
