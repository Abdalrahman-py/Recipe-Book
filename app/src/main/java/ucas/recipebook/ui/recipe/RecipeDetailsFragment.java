package ucas.recipebook.ui.recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
                Glide.with(requireContext())
                        .load(recipe.getImageUrl())
                        .into(binding.ivRecipeImage);
            }

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

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null && recipe.getCreatorId() != null
                    && recipe.getCreatorId().equals(currentUser.getUid())) {
                binding.ivEdit.setVisibility(View.VISIBLE);
                binding.ivDelete.setVisibility(View.VISIBLE);
            } else {
                binding.ivEdit.setVisibility(View.GONE);
                binding.ivDelete.setVisibility(View.GONE);
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
            if (recipe != null && recipe.getId() != null) {
                viewModel.deleteRecipe(recipe.getId()).observe(getViewLifecycleOwner(), success -> {
                    if (success != null && success) {
                        Toast.makeText(requireContext(), "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.tvVideoUrl.setOnClickListener(v -> {
            if (recipe.getVideoUrl() != null && !recipe.getVideoUrl().isEmpty()) {
                String url = recipe.getVideoUrl().trim();

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Invalid video URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

