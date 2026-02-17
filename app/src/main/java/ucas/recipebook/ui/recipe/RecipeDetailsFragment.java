package ucas.recipebook.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ucas.recipebook.MainActivity;
import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.databinding.FragmentRecipeDetailsBinding;
import ucas.recipebook.viewmodel.RecipeViewModel;

public class RecipeDetailsFragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private FragmentRecipeDetailsBinding binding;
    private RecipeViewModel viewModel;
    private Recipe recipe;

    public static RecipeDetailsFragment newInstance(Recipe recipe) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        displayRecipe();
        setupListeners();
    }

    private void displayRecipe() {
        if (recipe != null) {
            binding.tvTitle.setText(recipe.getTitle());
            binding.tvCategory.setText(recipe.getCategory());
            binding.tvVideoUrl.setText(recipe.getVideoUrl());

            if (recipe.getIngredients() != null) {
                binding.tvIngredients.setText(String.join("\n• ", recipe.getIngredients()));
            }

            if (recipe.getSteps() != null) {
                StringBuilder steps = new StringBuilder();
                for (int i = 0; i < recipe.getSteps().size(); i++) {
                    steps.append(i + 1).append(". ").append(recipe.getSteps().get(i)).append("\n");
                }
                binding.tvSteps.setText(steps.toString());
            }
        }
    }

    private void setupListeners() {
        binding.ivEdit.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity && recipe != null) {
                ((MainActivity) requireActivity()).showEditRecipe(recipe);
            }
        });

        binding.ivDelete.setOnClickListener(v -> {
            // Delete logic will be implemented later
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.tvVideoUrl.setOnClickListener(v -> {
            // Open video URL logic will be implemented later
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

