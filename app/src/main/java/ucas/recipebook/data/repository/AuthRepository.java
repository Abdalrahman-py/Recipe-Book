package ucas.recipebook.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ucas.recipebook.data.model.User;

public class AuthRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<User> userProfile = new MutableLiveData<>();

    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void register(String name, String email, String password, String country) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        // Save user profile to Firestore
                        saveUserProfile(firebaseUser.getUid(), name, email, country, null);
                    } else {
                        errorMessage.setValue("Registration failed: User creation error");
                    }
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue(e.getMessage());
                });
    }

    public void saveUserProfile(String uid, String name, String email, String country, String photoUrl) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("country", country);
        userMap.put("photoUrl", photoUrl);

        firestore.collection("users")
                .document(uid)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    registerSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    // Firestore save failed, delete the FirebaseAuth user
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        currentUser.delete();
                    }
                    errorMessage.setValue("Profile save failed: " + e.getMessage());
                });
    }

    public void getUserProfile(String uid) {
        firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = new User();
                        user.setUid(uid);
                        user.setName(documentSnapshot.getString("name"));
                        user.setEmail(documentSnapshot.getString("email"));
                        user.setCountry(documentSnapshot.getString("country"));
                        user.setPhotoUrl(documentSnapshot.getString("photoUrl"));
                        userProfile.setValue(user);
                    } else {
                        errorMessage.setValue("User profile not found");
                    }
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to load profile: " + e.getMessage());
                });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    loginSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue(e.getMessage());
                });
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<User> getUserProfile() {
        return userProfile;
    }
}

