package ucas.recipebook.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "RecipeBookSession";
    private static final String KEY_REMEMBER_ME = "remember_me";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveRememberMe(boolean rememberMe) {
        preferences.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply();
    }

    public boolean isRememberMeEnabled() {
        return preferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    public void clearSession() {
        preferences.edit().clear().apply();
    }
}

