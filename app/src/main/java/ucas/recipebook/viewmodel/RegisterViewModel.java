package ucas.recipebook.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ucas.recipebook.data.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public RegisterViewModel() {
        authRepository = new AuthRepository();
    }

    public void register(String name, String email, String password, String country, String photoUrl) {
        authRepository.register(name, email, password, country, photoUrl);

        authRepository.getRegisterSuccess().observeForever(success -> {
            if (success != null && success) {
                registerSuccess.setValue(true);
            }
        });

        authRepository.getErrorMessage().observeForever(error -> {
            if (error != null) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}

