package ucas.recipebook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.data.model.User;
import ucas.recipebook.data.repository.AuthRepository;
import ucas.recipebook.data.repository.RecipeRepository;
import ucas.recipebook.utils.SessionManager;

public class ProfileViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final RecipeRepository recipeRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<User> userProfile = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Recipe>> userRecipes = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
        recipeRepository = new RecipeRepository();
        sessionManager = new SessionManager(application);

        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = authRepository.getCurrentUser();
        if (currentUser != null) {
            authRepository.getUserProfile(currentUser.getUid());

            authRepository.getUserProfile().observeForever(user -> {
                if (user != null) {
                    userProfile.setValue(user);
                }
            });

            authRepository.getErrorMessage().observeForever(error -> {
                if (error != null) {
                    errorMessage.setValue(error);
                }
            });
        }
    }

    public void loadUserRecipes() {
        FirebaseUser currentUser = authRepository.getCurrentUser();
        if (currentUser == null) {
            userRecipes.setValue(null);
            return;
        }

        String uid = currentUser.getUid();
        recipeRepository.getRecipesByUser(uid);

        recipeRepository.getUserRecipeList().observeForever(recipes -> {
            if (recipes != null) {
                userRecipes.setValue(recipes);
            }
        });

        recipeRepository.getErrorMessage().observeForever(error -> {
            if (error != null) {
                errorMessage.setValue(error);
            }
        });
    }

    public void logout() {
        authRepository.logout();
        sessionManager.clearSession();
    }

    public LiveData<User> getUserProfile() {
        return userProfile;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Recipe>> getUserRecipes() {
        return userRecipes;
    }
}

