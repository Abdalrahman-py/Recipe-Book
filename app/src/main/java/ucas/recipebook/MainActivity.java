package ucas.recipebook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.databinding.ActivityMainBinding;
import ucas.recipebook.ui.home.HomeFragment;
import ucas.recipebook.ui.profile.ProfileFragment;
import ucas.recipebook.ui.recipe.AddRecipeFragment;
import ucas.recipebook.ui.recipe.EditRecipeFragment;
import ucas.recipebook.ui.recipe.RecipeDetailsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), false);
        }
    }

    public void showAddRecipe() {
        replaceFragment(new AddRecipeFragment(), true);
    }

    public void showRecipeDetails(Recipe recipe) {
        Fragment fragment = RecipeDetailsFragment.newInstance(recipe);
        replaceFragment(fragment, true);
    }

    public void showEditRecipe(Recipe recipe) {
        Fragment fragment = EditRecipeFragment.newInstance(recipe);
        replaceFragment(fragment, true);
    }

    public void showProfile() {
        replaceFragment(new ProfileFragment(), true);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}

