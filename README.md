# Recipe Book 📱

A modern Android recipe management application built with Java, following MVVM architecture and Material 3 design principles.

## 📖 Overview

Recipe Book is an Android application that allows users to manage their favorite recipes. Users can create, edit, view, and delete recipes, complete with ingredients, step-by-step instructions, and multimedia support. The app features user authentication and stores all data securely in Firebase Cloud.

## ✨ Features

### Current Features (Slice 1 - Complete ✅)
- **User Authentication**
  - User registration with name, email, password, and country
  - User login with email and password
  - Secure logout functionality
  - Session management

- **Recipe Management**
  - Browse all recipes with RecyclerView
  - View detailed recipe information
  - Add new recipes with title, ingredients, steps, category, and video URL
  - Edit existing recipes (creator only)
  - Delete recipes (creator only)

- **User Interface**
  - Material 3 design components
  - Clean and intuitive navigation
  - Tab-based recipe filtering
  - Search functionality
  - User profile display

### Upcoming Features (Slice 2 - Planned)
- Firebase Authentication integration
- Firestore database operations
- Form validation
- Image upload and display
- Real-time data synchronization
- Error handling and loading states
- Toast notifications

## 🏗️ Architecture

Recipe Book follows **Clean MVVM Architecture** with clear separation of concerns:

```
┌─────────────────────────────────────┐
│         UI Layer (Fragments)         │
│  - View Binding                      │
│  - Material 3 Components             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       ViewModel Layer                │
│  - LiveData                          │
│  - UI State Management               │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Repository Layer                │
│  - Data Access Logic                 │
│  - Firebase Operations               │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     Data Layer (Models)              │
│  - User Model                        │
│  - Recipe Model                      │
└──────────────────────────────────────┘
```

### Key Architectural Decisions
- **Two Activities**: AuthActivity for authentication, MainActivity for app features
- **Seven Fragments**: Login, Register, Home, AddRecipe, EditRecipe, RecipeDetails, Profile
- **Manual Fragment Navigation**: Using FragmentManager (no Navigation Component)
- **ViewBinding**: Enabled throughout for type-safe view access
- **No Business Logic in UI**: All logic handled by ViewModels and Repositories

## 🛠️ Technologies & Tools

### Core Technologies
- **Language**: Java 11
- **Platform**: Android (Min SDK 24, Target SDK 36)
- **Architecture**: MVVM (Model-View-ViewModel)

### Android Components
- **Lifecycle**: AndroidX Lifecycle (ViewModel, LiveData, Runtime)
- **UI**: Material 3, RecyclerView, ViewBinding
- **Activities & Fragments**: FragmentManager for navigation

### Firebase Services
- **Firebase Authentication**: User authentication and management
- **Firebase Firestore**: NoSQL cloud database
- **Firebase Analytics**: App analytics and insights
- **Firebase BOM**: Version 34.9.0 for dependency management

### Build System
- **Gradle**: Kotlin DSL (build.gradle.kts)
- **Build Tools**: Android Gradle Plugin

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio**: Ladybug Feature Drop | 2024.2.2 or later
- **Java Development Kit (JDK)**: Version 11 or later
- **Android SDK**: API Level 36 or later
- **Gradle**: 8.0 or later (included with Android Studio)
- **Firebase Account**: For Firebase services setup

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Abdalrahman-py/Recipe-Book.git
cd Recipe-Book
```

### 2. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use an existing one
3. Add an Android app to your Firebase project
   - Package name: `ucas.recipebook`
   - Download `google-services.json`
4. Place `google-services.json` in the `app/` directory

**⚠️ Security Note**: The `google-services.json` file is in `.gitignore` and should never be committed to version control.

### 3. Build the Project

Using Gradle Wrapper:
```bash
./gradlew assembleDebug
```

On Windows:
```bash
gradlew.bat assembleDebug
```

### 4. Run the Application

#### Using Android Studio
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Click the "Run" button or press `Shift + F10`

#### Using Command Line
```bash
./gradlew installDebug
```

## 📱 Usage

### First Time Launch
1. App opens to the Login screen
2. Click "Register" to create a new account
3. Fill in your details (name, email, password, country)
4. Click "Login" after registration to access the app

### Managing Recipes
1. **View Recipes**: Browse recipes on the Home screen
2. **Add Recipe**: Click the FAB (Floating Action Button)
3. **View Details**: Click any recipe card
4. **Edit Recipe**: Click edit icon (only for your recipes)
5. **Delete Recipe**: Click delete icon (only for your recipes)

### Navigation Flow
```
Login Screen
  ├─ Register → Back to Login
  └─ Login → Home Screen
              ├─ Add Recipe
              ├─ Recipe Details → Edit Recipe
              ├─ Profile
              └─ Logout → Login Screen
```

## 📂 Project Structure

```
Recipe-Book/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/ucas/recipebook/
│   │   │   │   ├── AuthActivity.java
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   └── Recipe.java
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── AuthRepository.java
│   │   │   │   │       └── RecipeRepository.java
│   │   │   │   ├── ui/
│   │   │   │   │   ├── adapter/
│   │   │   │   │   │   └── RecipeAdapter.java
│   │   │   │   │   ├── login/
│   │   │   │   │   │   └── LoginFragment.java
│   │   │   │   │   ├── register/
│   │   │   │   │   │   └── RegisterFragment.java
│   │   │   │   │   ├── home/
│   │   │   │   │   │   └── HomeFragment.java
│   │   │   │   │   ├── recipe/
│   │   │   │   │   │   ├── AddRecipeFragment.java
│   │   │   │   │   │   ├── EditRecipeFragment.java
│   │   │   │   │   │   └── RecipeDetailsFragment.java
│   │   │   │   │   └── profile/
│   │   │   │   │       └── ProfileFragment.java
│   │   │   │   ├── viewmodel/
│   │   │   │   │   ├── LoginViewModel.java
│   │   │   │   │   ├── RegisterViewModel.java
│   │   │   │   │   ├── HomeViewModel.java
│   │   │   │   │   ├── RecipeViewModel.java
│   │   │   │   │   └── ProfileViewModel.java
│   │   │   │   └── utils/
│   │   │   │       └── SessionManager.java
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       ├── values/
│   │   │       └── drawable/
│   │   └── test/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── ARCHITECTURE.md
├── README_SLICE1.md
└── README.md
```

## 🔧 Development

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Code Style

This project follows standard Java conventions:
- Use camelCase for variables and methods
- Use PascalCase for class names
- Keep methods small and focused
- No hardcoded strings - use string resources
- Document public methods and classes

### Building for Release

```bash
./gradlew assembleRelease
```

## 📊 Development Status

### Completed (Slice 1) ✅
- [x] Complete app skeleton
- [x] MVVM architecture implementation
- [x] All Activities and Fragments
- [x] ViewBinding setup
- [x] Material 3 UI components
- [x] Fragment navigation system
- [x] RecyclerView with adapter
- [x] Data models (User, Recipe)
- [x] ViewModel shells
- [x] Project compiles successfully

### In Progress (Slice 2) 🚧
- [ ] Firebase Authentication implementation
- [ ] Firestore database operations
- [ ] Repository layer completion
- [ ] Form validation
- [ ] Error handling
- [ ] Loading states
- [ ] Toast notifications
- [ ] Image upload/display

## 📚 Documentation

- **[ARCHITECTURE.md](ARCHITECTURE.md)**: Detailed architecture specification
- **[README_SLICE1.md](README_SLICE1.md)**: Slice 1 implementation details
- **[SLICE1_IMPLEMENTATION.md](SLICE1_IMPLEMENTATION.md)**: Implementation report
- **[PROMPTS.md](PROMPTS.md)**: Development prompts and guidelines

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Follow the existing code style and architecture
4. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

### Development Guidelines
- Follow MVVM architecture strictly
- No business logic in Activities or Fragments
- All Firebase operations through Repository layer
- Use ViewBinding (no findViewById)
- Material 3 components only
- Add Toast feedback for all operations

## 🐛 Troubleshooting

### Build Issues

```bash
# Clean and rebuild
./gradlew clean assembleDebug

# In Android Studio
File → Invalidate Caches / Restart
```

### Firebase Issues
- Ensure `google-services.json` is in the `app/` directory
- Verify package name matches Firebase configuration
- Check Firebase project settings

### Gradle Sync Issues
- Update Android Studio to the latest version
- Clear Gradle cache: `./gradlew clean`
- Sync Gradle files in Android Studio

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Abdalrahman**
- GitHub: [@Abdalrahman-py](https://github.com/Abdalrahman-py)

## 🙏 Acknowledgments

- Material Design 3 guidelines
- Firebase documentation
- Android Developers documentation
- MVVM architecture best practices

---

**Made with ❤️ for recipe enthusiasts**
