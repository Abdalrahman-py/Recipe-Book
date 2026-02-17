package ucas.recipebook.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import ucas.recipebook.AuthActivity;
import ucas.recipebook.MainActivity;
import ucas.recipebook.databinding.FragmentProfileBinding;
import ucas.recipebook.ui.adapter.RecipeAdapter;
import ucas.recipebook.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private RecipeAdapter adapter;

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
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.ivLogout.setOnClickListener(v -> {
            viewModel.logout();
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

