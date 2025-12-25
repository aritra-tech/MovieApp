# MovieApp 🎬

A modern, feature-rich Android application designed for movie enthusiasts. **MovieApp** leverages **The Movie Database (TMDB) API** to provide an immersive experience for discovering, searching, and managing favorite movies. Built with **Kotlin** and **Jetpack Compose**, it showcases modern Android development best practices including Clean Architecture and MVVM.

## ✨ Features

- **🚀 Discover Movies**: Browse an infinite list of popular and trending movies with smooth pagination.
- **🔍 Search**: Quickly find movies by title.
- **📱 Movie Details**: View comprehensive details including synopsis, release date, rating, runtime, and genres.
- **❤️ Favorites**: Mark movies as favorites. Your favorites are **synced directly with your TMDB account**, ensuring they are persisted across devices.
- **🎨 Modern UI**: a sleek, dark-themed interface built with Material3 and Jetpack Compose.
- **✨ Shared Element Transitions**: Seamless visual transitions when navigating from movie lists to details.

## 📸 Screenshots

*(Add screenshots of the Home Screen, Detail Screen, and Favorites Screen here)*

## 🛠️ Architecture

The project follows **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern to ensure separation of concerns, testability, and maintainability.

### Layers

1.  **Domain Layer** (`com.aritradas.movieapp.domain`):
    -   Contains proper **Entities** (Models) and **Repository Interfaces**.
    -   Pure Kotlin code, independent of Android frameworks.
2.  **Data Layer** (`com.aritradas.movieapp.data`):
    -   Implements Repository interfaces.
    -   Handles data sourcing from the **Network** (TMDB API).
    -   Manages **Paging** sources for infinite scrolling.
3.  **Presentation Layer** (`com.aritradas.movieapp.presentation`):
    -   **ViewModels**: Manage UI state and handle business logic.
    -   **UI**: Jetpack Compose screens and components.


## 📦 Package Structure

```
com.aritradas.movieapp
├── data                # Data layer implementation
│   ├── model           # DTOs (Data Transfer Objects)
│   ├── paging          # PagingSource implementations
│   ├── remote          # API Services & Network definitions
│   └── repository      # Repository implementations
├── di                  # Dependency Injection (Koin) modules
├── domain              # Domain layer definitions
│   ├── model           # Domain models
│   └── repository      # Repository interfaces
├── presentation        # UI layer
│   ├── common          # Shared UI components
│   ├── movieDetails    # Detail screen & ViewModel
│   ├── movies          # Movie list screen & ViewModel
│   └── favourites      # Favorites screen & ViewModel
└── ui                  # Theme and Color definitions
```

## 📚 Libraries & Tech Stack

-   **Kotlin**: 100% Kotlin-based.
-   **Jetpack Compose**: Modern toolkit for building native UI.
-   **Material3**: Material Design components.
-   **Ktor Client**: Asynchronous HTTP client for networking.
-   **Koin**: Lightweight Dependency Injection framework.
-   **Paging 3**: For efficient loading of large data sets.
-   **Coil**: Image loading library for Android backed by Kotlin Coroutines.
-   **Coroutines & Flow**: For asynchronous programming and reactive streams.
-   **Serialization**: Kotlinx Serialization for parsing JSON.

## ⚙️ Setup

1.  Clone the repository.
2.  Obtain an API Read Access Token (Bearer Token) from [TMDB](https://www.themoviedb.org/settings/api).
3.  Add your token to `local.properties`:
    ```properties
    TMDB_BEARER_TOKEN=your_token_here
    ```
4.  Build and run the project.
