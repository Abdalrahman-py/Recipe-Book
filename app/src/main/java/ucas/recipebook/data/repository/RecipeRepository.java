package ucas.recipebook.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ucas.recipebook.data.model.Recipe;

public class RecipeRepository {

    private final FirebaseFirestore firestore;
    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Recipe>> recipeList = new MutableLiveData<>();
    private final MutableLiveData<List<Recipe>> userRecipeList = new MutableLiveData<>();

    public RecipeRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void addRecipe(String title, String ingredientsInput, String stepsInput,
                          String category, String videoUrl, String imageUrl, String creatorId) {

        // Split and trim ingredients
        List<String> ingredients = Arrays.stream(ingredientsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Split and trim steps
        List<String> steps = Arrays.stream(stepsInput.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Create recipe map
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("title", title);
        recipeMap.put("ingredients", ingredients);
        recipeMap.put("steps", steps);
        recipeMap.put("category", category);
        recipeMap.put("videoUrl", videoUrl);
        recipeMap.put("imageUrl", imageUrl);
        recipeMap.put("creatorId", creatorId);
        recipeMap.put("createdAt", FieldValue.serverTimestamp());

        // Add to Firestore
        firestore.collection("recipes")
                .add(recipeMap)
                .addOnSuccessListener(documentReference -> {
                    addSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue(e.getMessage());
                });
    }

    public void getAllRecipes() {
        firestore.collection("recipes")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Recipe> recipes = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Recipe recipe = new Recipe();
                        recipe.setId(document.getId());
                        recipe.setTitle(document.getString("title"));
                        recipe.setCategory(document.getString("category"));
                        recipe.setVideoUrl(document.getString("videoUrl"));
                        recipe.setImageUrl(document.getString("imageUrl"));
                        recipe.setCreatorId(document.getString("creatorId"));

                        // Get ingredients list
                        List<String> ingredientsList = (List<String>) document.get("ingredients");
                        recipe.setIngredients(ingredientsList);

                        // Get steps list
                        List<String> stepsList = (List<String>) document.get("steps");
                        recipe.setSteps(stepsList);

                        // Get timestamp
                        Timestamp timestamp = document.getTimestamp("createdAt");
                        if (timestamp != null) {
                            recipe.setCreatedAt(timestamp.toDate());
                        }

                        recipes.add(recipe);
                    }
                    recipeList.setValue(recipes);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue(e.getMessage());
                });
    }

    public void getRecipesByUser(String uid) {
        if (uid == null || uid.isEmpty()) {
            userRecipeList.setValue(new ArrayList<>());
            return;
        }

        firestore.collection("recipes")
                .whereEqualTo("creatorId", uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        errorMessage.setValue(error.getMessage());
                        userRecipeList.setValue(new ArrayList<>());
                        return;
                    }

                    if (value != null) {
                        List<Recipe> recipes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            Recipe recipe = new Recipe();
                            recipe.setId(document.getId());
                            recipe.setTitle(document.getString("title"));
                            recipe.setCategory(document.getString("category"));
                            recipe.setVideoUrl(document.getString("videoUrl"));
                            recipe.setImageUrl(document.getString("imageUrl"));
                            recipe.setCreatorId(document.getString("creatorId"));

                            // Get ingredients list
                            List<String> ingredientsList = (List<String>) document.get("ingredients");
                            recipe.setIngredients(ingredientsList);

                            // Get steps list
                            List<String> stepsList = (List<String>) document.get("steps");
                            recipe.setSteps(stepsList);

                            // Get timestamp
                            Timestamp timestamp = document.getTimestamp("createdAt");
                            if (timestamp != null) {
                                recipe.setCreatedAt(timestamp.toDate());
                            }

                            recipes.add(recipe);
                        }
                        userRecipeList.setValue(recipes);
                    }
                });
    }

    public LiveData<Boolean> deleteRecipe(String recipeId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        firestore.collection("recipes")
                .document(recipeId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    result.postValue(true);
                })
                .addOnFailureListener(e -> {
                    result.postValue(false);
                });

        return result;
    }

    public LiveData<Boolean> updateRecipe(
            String recipeId,
            String title,
            String ingredientsRaw,
            String stepsRaw,
            String category,
            String videoUrl,
            String imageUrl
    ) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        List<String> ingredientsList = Arrays.stream(ingredientsRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<String> stepsList = Arrays.stream(stepsRaw.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("ingredients", ingredientsList);
        updates.put("steps", stepsList);
        updates.put("category", category);
        updates.put("videoUrl", videoUrl);
        updates.put("imageUrl", imageUrl);

        firestore.collection("recipes")
                .document(recipeId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    result.postValue(true);
                })
                .addOnFailureListener(e -> {
                    result.postValue(false);
                });

        return result;
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

    public LiveData<List<Recipe>> getUserRecipeList() {
        return userRecipeList;
    }
}
