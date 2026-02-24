package ucas.recipebook.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ucas.recipebook.AuthActivity;
import ucas.recipebook.MainActivity;
import ucas.recipebook.databinding.FragmentLoginBinding;
import ucas.recipebook.utils.ToastUtils;
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
                ToastUtils.showShort(requireContext(), "Login successful!");
                navigateToMainActivity();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                ToastUtils.showLong(requireContext(), "Login failed: " + error);
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
                ToastUtils.showShort(requireContext(), "Please enter email");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ToastUtils.showShort(requireContext(), "Invalid email format");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                ToastUtils.showShort(requireContext(), "Please enter password");
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

