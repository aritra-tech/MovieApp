# Problems & Solutions 🛠️

This document outlines the key technical challenges faced during the development of MovieApp, the solutions implemented, and the rationale behind specific architectural decisions.

## 1. Shared Element Transitions in Compose

### **Problem**
Implementing a seamless "hero" animation where the movie poster travels from the grid list to the detail screen was challenging.
-   **Context**: Navigating from a `LazyVerticalGrid` item to a new screen (`MovieDetailScreen`).
-   **Challenge**: Compose's Shared Element Transition API is experimental and requires precise scope management (`SharedTransitionScope`, `AnimatedVisibilityScope`) to match the "enter" and "exit" states of the composables correctly.

### **Solution**
-   Used `androidx.compose.animation:animation-ui` (implied or specific version dependent).
-   Wrapped the `NavHost` or root content in `SharedTransitionLayout`.
-   Passed the `sharedTransitionScope` and `animatedContentScope` down to the `MovieItem` and `MovieDetailScreen`.
-   **Key Implementation**:
    -   Used `Modifier.sharedElement()` on the `AsyncImage`.
    -   **Unique Keys**: Constructed unique keys using the movie ID (e.g., `"movie_poster_${movie.id}"`) to match the elements across screens.

### **Tradeoffs**
-   **Complexity**: Passing scopes down the component tree increases boilerplate.
-   **Performance**: Shared element transitions can be expensive; optimized by ensuring images are cached by Coil.

---

## 2. Syncing Favorites

### **Problem**
Keeping the "Favorite" status consistent between the **Movie List**, **Favorites Screen**, and **Movie Detail Screen**.
-   *Scenario*: User toggles "Favorite" on the Detail screen and goes back. The List screen should reflect this change immediately (e.g., show a filled heart icon).

### **Solution**
-   **Single Source of Truth**: The `MovieDetailViewModel` and `MoviesViewModel` both interact with the `FavoriteRepository`.
-   **State Management**:
    -   When toggling a favorite: An API call is made (`addFavorite`).
    -   **Paging Invalidation**: Since the lists are backed by `Paging 3`, simply updating a local list isn't enough. We invalidate the `PagingSource` (via `adapter.refresh()` or `pagingSource.invalidate()`) to re-fetch the latest state from the API.
    -   For the Detail screen, we re-fetch the "Account States" (`getMovieAccountStates`) to invoke the correct UI state.

### **Tradeoffs**
-   **Network Traffic**: Invalidating data triggers a new network fetch. While accurate, it consumes more data than a purely local optimistic update.
-   *Chosen because*: It guarantees data consistency with the Server (TMDB) without complex local database synchronization logic.

---

## 3. Ktor Setup & Networking

### **Problem**
Configuring a networking client that handles authentication transparently and efficiently parses JSON.

### **Solution**
-   **Centralized Module**: Configured `HttpClient` in `AppModule.kt` (Koin module).
-   **Authentication**: Used the `defaultRequest` plugin to inject the `Authorization: Bearer <TOKEN>` header into every request automatically.
-   **Logging**: Installed the `Logging` plugin (Level.ALL) to debug raw JSON responses during development.
-   **Serialization**: Used `ContentNegotiation` with `kotlinx.serialization` for type-safe parsing, configured to `ignoreUnknownKeys` to prevent crashes on API changes.

---

## 4. Persistence

### **Problem**
How to store user data (Favorites) reliably?

### **Solution**
-   **Server-Side Persistence**: Instead of a local database (like Room), the app relies on **TMDB's account features**.
-   `FavoriteRepositoryImpl` does not save to a local db. It calls `apiServices.addFavorite(...)`.
-   **Reasoning**:
    -   Currently, the app requires an internet connection to browse content.
    -   Syncing favorites across devices is a "free" feature when using the API directly.
    -   Reduces app size and complexity by removing the need for local database schema management and migration.

### **Tradeoffs**
-   **Offline Support**: The app currently does not support viewing favorites offline.
-   *Mitigation*: Coil caches images, so recently viewed favorites might still show their posters, but full interaction requires a network connection.

---

## 5. Implementation Steps Summary

### **Ktor Setup**
1.  Added Ktor dependencies (Core, Android, ContentNegotiation, Serialization, Logging).
2.  Created `ApiServices` interface with `suspend` functions.
3.  Configured `HttpClient` in `di/AppModule.kt`.

### **Shared Element Transitions**
1.  Added dependencies.
2.  Wrapped navigation root in `SharedTransitionLayout`.
3.  Added `modifier.sharedElementWithCallerManagedVisibility()` (or `sharedElement`) to the poster image on both `MovieScreen` (List) and `MovieDetailScreen`.
4.  Ensured the `key` passed to `rememberSharedContentState` is identical for the same movie on both screens.

### **Persistence (API-Based)**
1.  Defined `addFavorite` and `getFavoriteMovies` in `ApiServices`.
2.  Implemented `FavoriteRepositoryImpl` to call these endpoints.
3.  Designed the UI to react to loading/success/error states from these network calls.
