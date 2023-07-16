package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import recipes.models.Recipe;
import recipes.services.RecipeServices;

import java.util.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeServices recipeServices;

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> addRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        return recipeServices.addRecipe(recipe, authentication);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRecipe(@PathVariable Long id) {
        return recipeServices.getRecipe(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchRecipes(@RequestParam(required = false) String category,
                                                                   @RequestParam(required = false) String name) {
        return recipeServices.searchRecipes(category, name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id, Authentication authentication) {
        return recipeServices.deleteRecipe(id, authentication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable Long id, @RequestBody Recipe updatedRecipe,
                                             Authentication authentication) {
        return recipeServices.updateRecipe(id, updatedRecipe, authentication);
    }
}
