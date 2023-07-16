package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.models.UserPrincipal;
import recipes.repositories.RecipeRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecipeServices {
    @Autowired
    private RecipeRepository recipeRepository;

    public ResponseEntity<Map<String, Object>> addRecipe(Recipe recipe, Authentication authentication) {
        if (!isRecipeValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }

        User currentUser = getUserFromAuthentication(authentication);
        recipe.setAuthor(String.valueOf(currentUser));
        recipe.setDate(LocalDateTime.now());
        Recipe savedRecipe = recipeRepository.save(recipe);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedRecipe.getId());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> getRecipe(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            Map<String, Object> response = createRecipeResponse(recipe);
            response.remove("id"); // Remove the "id" field from the response
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<Map<String, Object>>> searchRecipes(String category, String name) {
        if ((category == null && name == null) || (category != null && name != null)) {
            return ResponseEntity.badRequest().build();
        }

        List<Recipe> recipes;
        if (category != null) {
            recipes = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        } else {
            recipes = recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Recipe recipe : recipes) {
            Map<String, Object> recipeData = createRecipeResponse(recipe);
            response.add(recipeData);
        }

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Void> deleteRecipe(Long id, Authentication authentication) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            User currentUser = getUserFromAuthentication(authentication);
            if (!recipe.getAuthor().equals(String.valueOf(currentUser))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            recipeRepository.delete(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> updateRecipe(Long id, Recipe updatedRecipe,
                                             Authentication authentication) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            User currentUser = getUserFromAuthentication(authentication);
            if (!recipe.getAuthor().equals(String.valueOf(currentUser))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (!isRecipeValid(updatedRecipe)) {
                return ResponseEntity.badRequest().build();
            }

            recipe.setName(updatedRecipe.getName());
            recipe.setCategory(updatedRecipe.getCategory());
            recipe.setDescription(updatedRecipe.getDescription());
            recipe.setIngredients(updatedRecipe.getIngredients());
            recipe.setDirections(updatedRecipe.getDirections());
            recipe.setDate(LocalDateTime.now());

            recipeRepository.save(recipe);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private User getUserFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        }
        return null;
    }

    private boolean isRecipeValid(Recipe recipe) {
        return recipe != null &&
                isValidString(recipe.getName()) &&
                isValidString(recipe.getCategory()) &&
                isValidString(recipe.getDescription()) &&
                isValidList(recipe.getIngredients()) &&
                isValidList(recipe.getDirections());
    }

    private boolean isValidString(String value) {
        return value != null && !value.isEmpty() && !value.isBlank();
    }

    private boolean isValidList(List<String> list) {
        return list != null && !list.isEmpty();
    }

    private Map<String, Object> createRecipeResponse(Recipe recipe) {
        Map<String, Object> response = new HashMap<>();
        response.put("name", recipe.getName());
        response.put("category", recipe.getCategory());
        response.put("date", recipe.getDate().toString());
        response.put("description", recipe.getDescription());
        response.put("ingredients", recipe.getIngredients());
        response.put("directions", recipe.getDirections());
        return response;
    }
}