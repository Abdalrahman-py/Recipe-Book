# Recipe Book App Architecture

## Overview

Recipe Book is an Android application built using:

- Java
- MVVM architecture
- Two Activities
- Fragments for screens
- Firebase Authentication
- Firebase Firestore
- Material 3 UI
- ViewBinding
- RecyclerView

No Navigation Component is used.
Fragment navigation is handled manually using FragmentManager.

---

# High Level Structure

## Activities

### 1. AuthActivity
Responsible for authentication flow.

Hosts:
- LoginFragment
- RegisterFragment

When login is successful:
- Start MainActivity
- Call finish() on AuthActivity

---

### 2. MainActivity
Responsible for all authenticated user screens.

Hosts:
- HomeFragment
- AddRecipeFragment
- EditRecipeFragment
- RecipeDetailsFragment
- ProfileFragment

On logout:
- Start AuthActivity
- Call finish() on MainActivity

---

# Fragment Navigation Rules

Navigation is handled using:

- getSupportFragmentManager()
- beginTransaction()
- replace()
- addToBackStack()

Rules:

- Login → Register (addToBackStack)
- Login → MainActivity (finish AuthActivity)
- Home → AddRecipe (addToBackStack)
- Home → RecipeDetails (addToBackStack)
- RecipeDetails → EditRecipe (addToBackStack)
- Home → Profile (addToBackStack)

Back button behavior:
- Pops fragment stack
- If stack empty in MainActivity → finish activity
- If stack empty in AuthActivity → finish activity

---

# MVVM Structure

## UI Layer

Package: ui

Sub-packages:
- ui.login
- ui.register
- ui.home
- ui.recipe
- ui.profile

Fragments contain:
- UI logic
- ViewBinding
- Observing ViewModel
- Navigation triggers

No business logic inside Fragments.

---

## ViewModel Layer

Package: viewmodel

ViewModels:

- LoginViewModel
- RegisterViewModel
- HomeViewModel
- RecipeViewModel
- ProfileViewModel

Responsibilities:
- Handle Firebase calls via Repository
- Expose LiveData
- No Android Context references
- No direct UI references

---

## Data Layer

Package: data

Sub-packages:

- data.model
- data.repository
- data.remote

### Models

User
- uid
- name
- email
- country
- photoUrl

Recipe
- id
- title
- ingredients: List<String>
- steps: List<String>
- category
- videoUrl
- imageUrl
- creatorId
- createdAt

---

### Repository

AuthRepository
- register()
- login()
- logout()
- getCurrentUser()

RecipeRepository
- addRecipe()
- updateRecipe()
- deleteRecipe()
- getAllRecipes()
- getRecipesByCategory()
- getRecipesByUser()

Repositories communicate with:
- FirebaseAuth
- FirebaseFirestore

---

# Firestore Structure

Collection: users
Document ID: FirebaseAuth uid

Fields:
- name: string
- email: string
- country: string
- photoUrl: string

Collection: recipes
Document ID: auto-generated

Fields:
- title: string
- ingredients: array<string>
- steps: array<string>
- category: string
- videoUrl: string
- imageUrl: string
- creatorId: string
- createdAt: timestamp

---

# UI Stack

- Material 3
- ViewBinding enabled
- RecyclerView for lists
- No Navigation Component

---

# Rules

- Every create, update, delete, login, logout must show Toast feedback.
- Only recipe creator can edit or delete recipe.
- Ingredients and steps stored as arrays, not single string.
- No business logic inside Activities or Fragments.
- All Firebase operations go through Repository layer.
- Activities are responsible only for fragment hosting and activity transitions.

---

# Backstack Security

AuthActivity and MainActivity are separated to prevent:

- Returning to login after successful authentication.
- Returning to authenticated screens after logout.

---

# Coding Standard

- Use camelCase for all fields.
- Keep consistent naming between Firestore and model classes.
- Avoid hardcoded strings, use resources where possible.
- Keep methods small and focused.

---

End of architecture specification.
