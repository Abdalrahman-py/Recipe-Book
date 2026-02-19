# Recipe Book

An Android app that lets users register, log in, share recipes, and discover recipes posted by others. Built with Java and Firebase.

## Overview

Recipe Book is a community-driven recipe sharing app. After signing up, users can browse all recipes on the home screen, add their own, and manage them from their profile. All data is stored and synced in real time using Firebase Firestore.

## Features

**Authentication**
- Register with name, email, password, and country
- Log in and log out securely
- Session management to keep users signed in

**Recipe Management**
- Browse all recipes from all users on the home screen
- View full recipe details including ingredients, steps, category, and video URL
- Add new recipes
- Edit or delete your own recipes (only visible to the recipe creator)

**Home Screen**
- Search recipes by keyword in real time
- Filter recipes by category using tabs
- Recipes reload automatically when returning to the home screen

**Profile**
- View your own profile info
- See all recipes you have posted
- Update your profile picture (with image cropping support)
- Log out from the profile screen

## Tech Stack

- **Language:** Java
- **Architecture:** MVVM (ViewModel + LiveData)
- **Backend:** Firebase Authentication, Firebase Firestore
- **Image Loading:** Glide
- **Image Cropping:** uCrop
- **UI:** Material 3, ViewBinding, RecyclerView, Fragment-based navigation
