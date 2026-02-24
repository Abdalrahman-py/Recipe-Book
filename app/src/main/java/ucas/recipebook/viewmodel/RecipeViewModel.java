package ucas.recipebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.data.repository.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;

    public RecipeViewModel() {
        recipeRepository = new RecipeRepository();
    }

    public void addRecipe(String title, String ingredientsInput, String stepsInput,
                          String category, String videoUrl, String imageUrl, String creatorId) {
        recipeRepository.addRecipe(title, ingredientsInput, stepsInput, category, videoUrl, imageUrl, creatorId);
    }

    public void loadAllRecipes() {
        recipeRepository.getAllRecipes();
    }

    public LiveData<Boolean> deleteRecipe(String recipeId) {
        return recipeRepository.deleteRecipe(recipeId);
    }

    public LiveData<Boolean> updateRecipe(
            String recipeId,
            String title,
            String ingredients,
            String steps,
            String category,
            String videoUrl,
            String imageUrl
    ) {
        return recipeRepository.updateRecipe(recipeId, title, ingredients, steps, category, videoUrl, imageUrl);
    }

    public LiveData<Boolean> getAddSuccess() {
        return recipeRepository.getAddSuccess();
    }

    public LiveData<String> getErrorMessage() {
        return recipeRepository.getErrorMessage();
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeRepository.getRecipeList();
    }
}
