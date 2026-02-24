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
        sessionManager.saveRememberMe(rememberMe);
        authRepository.login(email, password);
    }

    public LiveData<Boolean> getLoginSuccess() {
        return authRepository.getLoginSuccess();
    }

    public LiveData<String> getErrorMessage() {
        return authRepository.getErrorMessage();
    }

    public LiveData<Boolean> getAutoLogin() {
        return autoLogin;
    }
}

