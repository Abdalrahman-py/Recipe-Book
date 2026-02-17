package ucas.recipebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.data.repository.RecipeRepository;

public class HomeViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final MutableLiveData<List<Recipe>> recipeList = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<String>> categories = new MutableLiveData<>();
    private final MutableLiveData<List<Recipe>> filteredRecipes = new MutableLiveData<>();

    private List<Recipe> fullRecipeList = new ArrayList<>();
    private String currentCategory = "All";
    private String currentSearchQuery = "";

    public HomeViewModel() {
        recipeRepository = new RecipeRepository();
    }

    public void loadAllRecipes() {
        recipeRepository.getAllRecipes();

        recipeRepository.getRecipeList().observeForever(recipes -> {
            if (recipes != null) {
                fullRecipeList = recipes;
                recipeList.setValue(recipes);

                // Extract unique categories
                extractCategories(recipes);

                // Apply filters
                applyFilters();
            }
        });

        recipeRepository.getErrorMessage().observeForever(error -> {
            if (error != null) {
                errorMessage.setValue(error);
            }
        });
    }

    private void extractCategories(List<Recipe> recipes) {
        Set<String> uniqueCategories = new LinkedHashSet<>();

        for (Recipe recipe : recipes) {
            if (recipe.getCategory() != null && !recipe.getCategory().isEmpty()) {
                uniqueCategories.add(recipe.getCategory());
            }
        }

        List<String> categoryList = new ArrayList<>();
        categoryList.add("All");
        categoryList.addAll(uniqueCategories);

        categories.setValue(categoryList);
    }

    public void setSelectedCategory(String category) {
        this.currentCategory = category != null ? category : "All";
        applyFilters();
    }

    public void setSearchQuery(String query) {
        this.currentSearchQuery = query != null ? query.trim() : "";
        applyFilters();
    }

    private void applyFilters() {
        List<Recipe> result = new ArrayList<>(fullRecipeList);

        // Filter by category if not "All"
        if (!currentCategory.equals("All")) {
            List<Recipe> categoryFiltered = new ArrayList<>();
            for (Recipe recipe : result) {
                if (currentCategory.equals(recipe.getCategory())) {
                    categoryFiltered.add(recipe);
                }
            }
            result = categoryFiltered;
        }

        // Filter by search query if not empty
        if (!currentSearchQuery.isEmpty()) {
            List<Recipe> searchFiltered = new ArrayList<>();
            String lowerQuery = currentSearchQuery.toLowerCase();
            for (Recipe recipe : result) {
                if (recipe.getTitle() != null &&
                    recipe.getTitle().toLowerCase().contains(lowerQuery)) {
                    searchFiltered.add(recipe);
                }
            }
            result = searchFiltered;
        }

        filteredRecipes.setValue(result);
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }

    public LiveData<List<Recipe>> getFilteredRecipes() {
        return filteredRecipes;
    }
}

