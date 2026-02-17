package ucas.recipebook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ucas.recipebook.databinding.ActivityAuthBinding;
import ucas.recipebook.ui.login.LoginFragment;
import ucas.recipebook.ui.register.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            showLogin();
        }
    }

    public void showLogin() {
        replaceFragment(new LoginFragment(), false);
    }

    public void showRegister() {
        replaceFragment(new RegisterFragment(), true);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.authFragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}

