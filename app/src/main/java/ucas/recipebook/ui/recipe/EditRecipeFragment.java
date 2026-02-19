package ucas.recipebook.ui.recipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import ucas.recipebook.data.model.Recipe;
import ucas.recipebook.databinding.FragmentEditRecipeBinding;
import ucas.recipebook.viewmodel.RecipeViewModel;

public class EditRecipeFragment extends Fragment {

    private static final String ARG_RECIPE = "recipe";
    private FragmentEditRecipeBinding binding;
    private RecipeViewModel viewModel;
    private Recipe recipe;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri sourceUri = result.getData().getData();
                            Uri destinationUri = Uri.fromFile(
                                    new File(requireContext().getCacheDir(),
                                            "cropped_" + System.currentTimeMillis() + ".jpg")
                            );

                            UCrop.of(sourceUri, destinationUri)
                                    .withAspectRatio(1, 1)
                                    .withMaxResultSize(800, 800)
                                    .start(requireContext(), this);
                        }
                    });

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

        binding.ivRecipeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        populateFields();
        setupListeners();
    }

    private void populateFields() {
        if (recipe != null) {
            if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
                Glide.with(requireContext())
                        .load(recipe.getImageUrl())
                        .into(binding.ivRecipeImage);
            }

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
            String title = binding.etTitle.getText().toString().trim();
            String ingredients = binding.etIngredients.getText().toString().trim();
            String steps = binding.etSteps.getText().toString().trim();
            String category = binding.etCategory.getText().toString().trim();
            String videoUrl = binding.etVideoUrl.getText().toString().trim();

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

            if (!videoUrl.isEmpty() && !Patterns.WEB_URL.matcher(videoUrl).matches()) {
                Toast.makeText(requireContext(), "Invalid video URL", Toast.LENGTH_SHORT).show();
                return;
            }

            if (recipe != null && recipe.getId() != null) {
                String imageUrl = recipe.getImageUrl();
                if (selectedImageUri != null) {
                    imageUrl = selectedImageUri.toString();
                }

                viewModel.updateRecipe(
                        recipe.getId(),
                        title,
                        ingredients,
                        steps,
                        category,
                        videoUrl,
                        imageUrl
                ).observe(getViewLifecycleOwner(), success -> {
                    if (success != null && success) {
                        Toast.makeText(requireContext(), "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Failed to update recipe", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                selectedImageUri = resultUri;
                binding.ivRecipeImage.setImageURI(resultUri);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}