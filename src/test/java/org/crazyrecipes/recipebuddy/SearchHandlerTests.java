package org.crazyrecipes.recipebuddy;

import org.crazyrecipes.recipebuddy.recipe.Recipe;
import org.crazyrecipes.recipebuddy.search.Search;
import org.crazyrecipes.recipebuddy.search.SearchHandler;
import org.crazyrecipes.recipebuddy.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Vector;

/**
 * Tests the SearchHandler class
 */
@SpringBootTest
public class SearchHandlerTests {
    /**
     * Tests the functionality of searching, filtering, and ranking in SearchHandler
     */
    @Test
    public void testSearchHandler() {
        Log log = new Log("SearchHandlerTests");
        log.print("Starting test of SearchHandler...");

        /*
            Generate recipes to test:
            Names: 10, 20, ... to 90
            Ingredients, allergens, utensils: 11-19, 21-29, ... to 91-99
            Tags: "abc"
         */
        log.print("Generating test recipes...");
        Vector<Recipe> recipes = new Vector<>();
        for(int i = 10; i < 100; i += 10) {
            Recipe r = new Recipe();
            Vector<String> r_ingredients = new Vector<>();
            Vector<String> r_utensils = new Vector<>();
            Vector<String> r_allergens = new Vector<>();
            Vector<String> r_tags = new Vector<>();
            r_tags.add("abc");
            r.setName(String.valueOf(i));
            for(int j = i; j < i + 10; j++) {
                r_ingredients.add(String.valueOf(j));
                r_utensils.add(String.valueOf(j));
                r_allergens.add(String.valueOf(j));
            }
            r.setRating(i / 10.0);
            r.setCooked(i / 10);
            r.setIngredients(r_ingredients);
            r.setUtensils(r_utensils);
            r.setAllergens(r_allergens);
            r.setTags(r_tags);
            recipes.add(r);
        }

        Vector<String> ingredients;
        Vector<String> utensils;
        Vector<String> allergens;
        Vector<Recipe> results;
        Search search;

        /* ===== Test queries ===== */

        log.print("Testing searching by name...");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            for(int j = i; j < i + 10; j++) {
                ingredients.add(String.valueOf(j));
                utensils.add(String.valueOf(j));
                allergens.add(String.valueOf(j));
            }
            search = new Search(String.valueOf(i), "NONE", "NONE", "SHOW");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.size() == 1;
            assert results.get(0).equals(recipes.get((i / 10) - 1));
        }

        log.print("Testing searching by tags...");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            search = new Search("abc", "NONE", "NONE", "SHOW");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.size() == 9;
        }

        /* ===== Test filters ===== */

        log.print("Testing showing only recipes with all ingredients+utensils (we do have everything)...");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            for(int j = i; j < i + 10; j++) {
                ingredients.add(String.valueOf(j));
                utensils.add(String.valueOf(j));
                allergens.add(String.valueOf(j));
            }
            search = new Search(String.valueOf(i), "ALL", "ALL", "SHOW");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.size() == 1;
            assert results.get(0).equals(recipes.get((i / 10) - 1));
        }

        log.print("Testing showing only recipes with all ingredients+utensils (we do NOT have everything)...");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            for(int j = i; j < i + 9; j++) {
                ingredients.add(String.valueOf(j));
                utensils.add(String.valueOf(j));
                allergens.add(String.valueOf(j));
            }
            search = new Search(String.valueOf(i), "ALL", "ALL", "SHOW");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.isEmpty();
        }

        log.print("Testing showing only recipes with most ingredients+utensils (we do have most)...");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            for(int j = i; j < i + 8; j++) {
                ingredients.add(String.valueOf(j));
                utensils.add(String.valueOf(j));
                allergens.add(String.valueOf(j));
            }
            search = new Search(String.valueOf(i), "SOME", "SOME", "SHOW");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.size() == 1;
            assert results.get(0).equals(recipes.get((i / 10) - 1));
        }

        log.print("Testing showing only recipes with most ingredients+utensils (we do NOT have most)...");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            for(int j = i; j < i + 4; j++) {
                ingredients.add(String.valueOf(j));
                utensils.add(String.valueOf(j));
                allergens.add(String.valueOf(j));
            }
            search = new Search(String.valueOf(i), "SOME", "SOME", "SHOW");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.isEmpty();
        }

        log.print("Testing showing only recipes without allergens (we do NOT have allergens)");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();

            search = new Search(String.valueOf(i), "NONE", "NONE", "BLOCK");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.size() == 1;
            assert results.get(0).equals(recipes.get((i / 10) - 1));
        }

        log.print("Testing showing only recipes without allergens (we do have allergens)");
        for(int i = 10; i < 100; i += 10) {
            ingredients = new Vector<>();
            utensils = new Vector<>();
            allergens = new Vector<>();
            for(int j = i; j < i + 10; j++) {
                ingredients.add(String.valueOf(j));
                utensils.add(String.valueOf(j));
                allergens.add(String.valueOf(j));
            }
            search = new Search(String.valueOf(i), "NONE", "NONE", "BLOCK");
            results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
            assert results.isEmpty();
        }

        /* ===== Test ranking ===== */

        log.print("Testing ranking of search results...");
        ingredients = new Vector<>();
        utensils = new Vector<>();
        allergens = new Vector<>();
        search = new Search("", "NONE", "NONE", "SHOW");
        results = new Vector<>((new SearchHandler(recipes, ingredients, utensils, allergens)).doSearch(search));
        assert results.size() == 9;
        for(int i = 0; i < 9; i++) {
            assert results.get(i).equals(recipes.get(8-i));
        }

        log.print("Test of SearchHandler passed.");
    }
}
