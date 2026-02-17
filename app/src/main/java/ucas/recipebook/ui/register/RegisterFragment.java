package ucas.recipebook.ui.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ucas.recipebook.R;
import ucas.recipebook.databinding.FragmentRegisterBinding;
import ucas.recipebook.viewmodel.RegisterViewModel;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        setupCountrySpinner();
        setupObservers();
        setupListeners();
    }

    private void setupCountrySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.arab_countries,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(adapter);

        // Set default selection to Palestine (index 0)
        binding.spinnerCountry.setSelection(0);
    }

    private void setupObservers() {
        // Observe registration success
        viewModel.getRegisterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Registration failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String country = binding.spinnerCountry.getSelectedItem().toString();

            // Validate inputs
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call ViewModel to register with country
            viewModel.register(name, email, password, country);
        });

        binding.tvLogin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

