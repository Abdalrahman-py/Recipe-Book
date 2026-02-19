package ucas.recipebook.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ucas.recipebook.AuthActivity;
import ucas.recipebook.MainActivity;
import ucas.recipebook.databinding.FragmentLoginBinding;
import ucas.recipebook.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        // Observe auto login
        viewModel.getAutoLogin().observe(getViewLifecycleOwner(), autoLogin -> {
            if (autoLogin != null && autoLogin) {
                navigateToMainActivity();
            }
        });

        // Observe login success
        viewModel.getLoginSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Login failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            boolean rememberMe = binding.cbRememberMe.isChecked();

            // Validate inputs
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call ViewModel to login
            viewModel.login(email, password, rememberMe);
        });

        binding.tvRegister.setOnClickListener(v -> {
            if (requireActivity() instanceof AuthActivity) {
                ((AuthActivity) requireActivity()).showRegister();
            }
        });
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

