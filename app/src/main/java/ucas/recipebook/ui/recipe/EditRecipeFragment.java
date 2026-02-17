package ucas.recipebook.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.databinding.FragmentEditRecipeBinding;
import ucas.recipebook.viewmodel.RecipeViewModel;

public class EditRecipeFragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private FragmentEditRecipeBinding binding;
    private RecipeViewModel viewModel;
    private Recipe recipe;

    public static EditRecipeFragment newInstance(Recipe recipe) {
        EditRecipeFragment fragment = new EditRecipeFragment();
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
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        populateFields();
        setupListeners();
    }

    private void populateFields() {
        if (recipe != null) {
            binding.etTitle.setText(recipe.getTitle());
            binding.etCategory.setText(recipe.getCategory());
            binding.etVideoUrl.setText(recipe.getVideoUrl());

            if (recipe.getIngredients() != null) {
                binding.etIngredients.setText(String.join("\n", recipe.getIngredients()));
            }

            if (recipe.getSteps() != null) {
                binding.etSteps.setText(String.join("\n", recipe.getSteps()));
            }
        }
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(v -> {
            // Update recipe logic will be implemented later
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


