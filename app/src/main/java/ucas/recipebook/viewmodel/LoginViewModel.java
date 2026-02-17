package ucas.recipebook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ucas.recipebook.data.repository.AuthRepository;
import ucas.recipebook.utils.SessionManager;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> autoLogin = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
        sessionManager = new SessionManager(application);

        checkAutoLogin();
    }

    private void checkAutoLogin() {
        if (authRepository.isUserLoggedIn() && sessionManager.isRememberMeEnabled()) {
            autoLogin.setValue(true);
        }
    }

    public void login(String email, String password, boolean rememberMe) {
        authRepository.login(email, password);

        authRepository.getLoginSuccess().observeForever(success -> {
            if (success != null && success) {
                sessionManager.saveRememberMe(rememberMe);
                loginSuccess.setValue(true);
            }
        });

        authRepository.getErrorMessage().observeForever(error -> {
            if (error != null) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getAutoLogin() {
        return autoLogin;
    }
}

