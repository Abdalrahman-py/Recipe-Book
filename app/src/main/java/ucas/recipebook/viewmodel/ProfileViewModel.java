package ucas.recipebook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
        }
    }

    public void loadUserRecipes() {
        FirebaseUser currentUser = authRepository.getCurrentUser();
        if (currentUser == null) return;
        recipeRepository.getRecipesByUser(currentUser.getUid());
    }

    public void logout() {
        authRepository.logout();
        sessionManager.clearSession();
    }

    public void updateProfileImage(String photoUrl) {
        authRepository.updateProfileImage(photoUrl);
    }

    public LiveData<User> getUserProfile() {
        return authRepository.getUserProfile();
    }

    public LiveData<String> getErrorMessage() {
        return authRepository.getProfileErrorMessage();
    }

    public LiveData<List<Recipe>> getUserRecipes() {
        return recipeRepository.getUserRecipeList();
    }
}
