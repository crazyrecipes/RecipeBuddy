package org.lavajuno.recipebuddy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
public class RecipeBuddyController {
    private final List<Recipe> recipes = new ArrayList<>();
    public RecipeBuddyController() {
        recipes.add(new Recipe(0,"test0", "test0desc(c)"));
        recipes.add(new Recipe(1,"test1", "test1desc(r)"));
        recipes.add(new Recipe(2,"test2", "test2desc(u)"));
        recipes.add(new Recipe(3,"test3", "test3desc(d)"));
    }

    @GetMapping("/recipes")
    List<Recipe> all() {
        return recipes;
    }

    @PostMapping("/recipes")
    Recipe newRecipe(@RequestBody Recipe newRecipe) {
        return recipes.get(0);
    }

    @GetMapping("/recipes/{id}")
    Recipe one(@PathVariable Long id) {
        return recipes.get(1);
    }

    @PutMapping("/recipes/{id}")
    Recipe replaceRecipe(@RequestBody Recipe newRecipe, @PathVariable Long id) {
        return recipes.get(2);
    }

    @DeleteMapping("/recipes/{id}")
    void deleteRecipe(@PathVariable Long id) {
        return;
    }
}
