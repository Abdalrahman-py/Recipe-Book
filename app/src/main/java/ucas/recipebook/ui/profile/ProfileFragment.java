package ucas.recipebook.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import ucas.recipebook.AuthActivity;
import ucas.recipebook.MainActivity;
import ucas.recipebook.R;
import ucas.recipebook.databinding.FragmentProfileBinding;
import ucas.recipebook.ui.adapter.RecipeAdapter;
import ucas.recipebook.utils.ToastUtils;
import ucas.recipebook.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private RecipeAdapter adapter;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.ivUserPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        setupRecyclerView();
        setupObservers();
        setupListeners();

        // Load user recipes
        viewModel.loadUserRecipes();
    }

    private void setupRecyclerView() {
        binding.rvUserRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RecipeAdapter(recipe -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).showRecipeDetails(recipe);
            }
        });

        binding.rvUserRecipes.setAdapter(adapter);
    }

    private void setupObservers() {
        // Observe user profile
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvName.setText(user.getName());
                binding.tvEmail.setText(user.getEmail());

                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                    binding.ivUserPhoto.setPadding(0, 0, 0, 0);
                    binding.ivUserPhoto.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                    Glide.with(requireContext())
                            .load(user.getPhotoUrl())
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .circleCrop()
                            .into(binding.ivUserPhoto);
                    binding.btnRemovePhoto.setVisibility(View.VISIBLE);
                } else {
                    int p = (int) (12 * getResources().getDisplayMetrics().density);
                    binding.ivUserPhoto.setPadding(p, p, p, p);
                    binding.ivUserPhoto.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
                    binding.ivUserPhoto.setImageResource(R.drawable.ic_profile_placeholder);
                    binding.btnRemovePhoto.setVisibility(View.GONE);
                }
            }
        });

        // Observe user recipes
        viewModel.getUserRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                adapter.setRecipes(recipes);
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                ToastUtils.showLong(requireContext(), "Error: " + error);
            }
        });
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        binding.btnRemovePhoto.setOnClickListener(v -> {
            selectedImageUri = null;
            int p = (int) (12 * getResources().getDisplayMetrics().density);
            binding.ivUserPhoto.setPadding(p, p, p, p);
            binding.ivUserPhoto.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
            binding.ivUserPhoto.setImageResource(R.drawable.ic_profile_placeholder);
            binding.btnRemovePhoto.setVisibility(View.GONE);
            viewModel.updateProfileImage(null);
        });

        binding.ivLogout.setOnClickListener(v -> {
            viewModel.logout();
            ToastUtils.showShort(requireContext(), "Logged out successfully");

            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                selectedImageUri = resultUri;
                binding.ivUserPhoto.setPadding(0, 0, 0, 0);
                binding.ivUserPhoto.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                binding.ivUserPhoto.setImageURI(resultUri);
                binding.btnRemovePhoto.setVisibility(View.VISIBLE);
                viewModel.updateProfileImage(selectedImageUri.toString());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

