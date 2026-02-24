package ucas.recipebook.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ucas.recipebook.MainActivity;
import ucas.recipebook.R;
import ucas.recipebook.databinding.FragmentHomeBinding;
import ucas.recipebook.ui.adapter.RecipeAdapter;
import ucas.recipebook.utils.ToastUtils;
import ucas.recipebook.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private RecipeAdapter adapter;
    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();

        // Load recipes on start
        viewModel.loadAllRecipes();
    }

    private void setupTabs(List<String> categoryList) {
        // Clear existing tabs and listeners
        binding.tabLayout.clearOnTabSelectedListeners();
        binding.tabLayout.removeAllTabs();

        // Add tabs dynamically from category list
        for (String category : categoryList) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(category));
        }

        // Set tab selection listener
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText() != null ? tab.getText().toString() : "All";
                viewModel.setSelectedCategory(selectedCategory);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupRecyclerView() {
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RecipeAdapter(recipe -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).showRecipeDetails(recipe);
            }
        });

        binding.rvRecipes.setAdapter(adapter);
    }

    private void setupObservers() {
        // Observe raw recipe data from repository and process it
        viewModel.observeRecipeData().observe(getViewLifecycleOwner(), recipes -> {
            viewModel.processRecipeData(recipes);
        });

        // Observe repository errors
        viewModel.observeRepositoryErrors().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                ToastUtils.showLong(requireContext(), "Error: " + error);
            }
        });

        // Observe categories and setup tabs dynamically
        viewModel.getCategories().observe(getViewLifecycleOwner(), categoryList -> {
            if (categoryList != null && !categoryList.isEmpty()) {
                setupTabs(categoryList);
            }
        });

        // Observe filtered recipe list
        viewModel.getFilteredRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                adapter.setRecipes(recipes);
            }
        });

        // Observe error messages from ViewModel
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                ToastUtils.showLong(requireContext(), "Error: " + error);
            }
        });

        // Observe current user profile for profile icon
        viewModel.getCurrentUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                binding.ivProfile.setPadding(0, 0, 0, 0);
                binding.ivProfile.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                Glide.with(requireContext())
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .circleCrop()
                        .into(binding.ivProfile);
            } else {
                int p = (int) (6 * getResources().getDisplayMetrics().density);
                binding.ivProfile.setPadding(p, p, p, p);
                binding.ivProfile.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
                binding.ivProfile.setImageResource(R.drawable.ic_profile_placeholder);
            }
        });
    }

    private void setupListeners() {
        // Search functionality
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // FAB for adding recipe
        binding.fabAddRecipe.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).showAddRecipe();
            }
        });

        // Profile icon
        binding.ivProfile.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).showProfile();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
        } else {
            // Reload recipes when returning from another fragment
            viewModel.loadAllRecipes();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



