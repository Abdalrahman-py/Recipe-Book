package ucas.recipebook.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ucas.recipebook.R;
import ucas.recipebook.databinding.FragmentRegisterBinding;
import ucas.recipebook.utils.ToastUtils;
import ucas.recipebook.viewmodel.RegisterViewModel;

import com.yalantis.ucrop.UCrop;
import java.io.File;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.ivProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

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
                ToastUtils.showShort(requireContext(), "Registration successful! Please login.");
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                ToastUtils.showLong(requireContext(), "Registration failed: " + error);
            }
        });
    }

    private void setupListeners() {
        binding.btnRemovePhoto.setOnClickListener(v -> {
            selectedImageUri = null;
            int p = (int) (14 * getResources().getDisplayMetrics().density);
            binding.ivProfileImage.setPadding(p, p, p, p);
            binding.ivProfileImage.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
            binding.ivProfileImage.setImageResource(R.drawable.ic_profile_placeholder);
            binding.btnRemovePhoto.setVisibility(View.GONE);
        });

        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String country = binding.spinnerCountry.getSelectedItem().toString();

            // Validate inputs
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort(requireContext(), "Please enter name");
                return;
            }

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

            if (password.length() < 6) {
                ToastUtils.showShort(requireContext(), "Password must be at least 6 characters");
                return;
            }

            String photoUrl = null;
            if (selectedImageUri != null) {
                photoUrl = selectedImageUri.toString();
            }

            // Call ViewModel to register with country
            viewModel.register(name, email, password, country, photoUrl);
        });

        binding.tvLogin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                selectedImageUri = resultUri;
                binding.ivProfileImage.setPadding(0, 0, 0, 0);
                binding.ivProfileImage.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                binding.ivProfileImage.setImageURI(resultUri);
                binding.btnRemovePhoto.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

