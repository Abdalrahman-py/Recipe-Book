package ucas.recipebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ucas.recipebook.data.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {

    private final AuthRepository authRepository;

    public RegisterViewModel() {
        authRepository = new AuthRepository();
    }

    public void register(String name, String email, String password, String country, String photoUrl) {
        authRepository.register(name, email, password, country, photoUrl);
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return authRepository.getRegisterSuccess();
    }

    public LiveData<String> getErrorMessage() {
        return authRepository.getErrorMessage();
    }
}

