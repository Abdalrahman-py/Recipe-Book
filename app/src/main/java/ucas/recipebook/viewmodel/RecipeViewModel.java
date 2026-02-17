package ucas.recipebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.data.repository.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;

    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Recipe>> recipeList = new MutableLiveData<>();

    public RecipeViewModel() {
        recipeRepository = new RecipeRepository();
    }

    public void addRecipe(String title, String ingredientsInput, String stepsInput,
                          String category, String videoUrl, String imageUrl, String creatorId) {
        recipeRepository.addRecipe(title, ingredientsInput, stepsInput, category, videoUrl, imageUrl, creatorId);

        recipeRepository.getAddSuccess().observeForever(success -> {
            if (success != null && success) {
                addSuccess.setValue(true);
            }
        });

        recipeRepository.getErrorMessage().observeForever(error -> {
            if (error != null) {
                errorMessage.setValue(error);
            }
        });
    }

    public void loadAllRecipes() {
        recipeRepository.getAllRecipes();

        recipeRepository.getRecipeList().observeForever(recipes -> {
            if (recipes != null) {
                recipeList.setValue(recipes);
            }
        });

        recipeRepository.getErrorMessage().observeForever(error -> {
            if (error != null) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<Boolean> getAddSuccess() {
        return addSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }
}

