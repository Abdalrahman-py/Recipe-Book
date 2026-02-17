package ucas.recipebook.ui.recipe;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ucas.recipebook.databinding.FragmentAddRecipeBinding;
import ucas.recipebook.viewmodel.RecipeViewModel;

public class AddRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;
    private RecipeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        // Observe add success
        viewModel.getAddSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Recipe added successfully", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Failed to add recipe: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString().trim();
            String ingredients = binding.etIngredients.getText().toString().trim();
            String steps = binding.etSteps.getText().toString().trim();
            String category = binding.etCategory.getText().toString().trim();
            String videoUrl = binding.etVideoUrl.getText().toString().trim();

            // Validate required fields
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(requireContext(), "Please enter recipe title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(ingredients)) {
                Toast.makeText(requireContext(), "Please enter ingredients", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(steps)) {
                Toast.makeText(requireContext(), "Please enter steps", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(category)) {
                Toast.makeText(requireContext(), "Please enter category", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get current user ID
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String creatorId = currentUser.getUid();
            String imageUrl = null; // Will be implemented later

            // Call ViewModel to add recipe
            viewModel.addRecipe(title, ingredients, steps, category, videoUrl, imageUrl, creatorId);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

