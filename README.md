# Rockty

A modern Android application showcasing best practices for building scalable, maintainable, and testable apps. It features a list of rocket launches and detailed views for each launch, leveraging GraphQL for data fetching and Jetpack Compose for the UI.

## Features

*   **Launch List:** Displays a paginated list of rocket launches.
*   **Launch Details:** Shows detailed information about a specific launch, including mission patch, rocket details, and launch site.
*   **Pagination:** Efficiently handles large datasets with custom pagination logic.
*   **Clean Architecture:** Separation of concerns with Domain, Data, and UI layers.
*   **Unidirectional Data Flow (UDF):** Predictable state management using Intent-based architecture.
*   **Dependency Injection:** Uses Hilt for managing dependencies.
*   **GraphQL Integration:** Fetches data using Apollo Kotlin.
*   **Material Design 3:** Modern UI components and theming.
*   **Testing:** comprehensive Unit and Instrumented tests.

## Tech Stack

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Architecture:** MVVM + Clean Architecture + UDF
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
*   **Network/GraphQL:** [Apollo Kotlin](https://www.apollographql.com/docs/kotlin/)
*   **Async Operations:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
*   **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) with Type Safety
*   **Testing:**
    *   JUnit 4
    *   Mockito (Core & Inline)
    *   Kotlinx Coroutines Test
    *   Hilt Testing
    *   Compose UI Test

## Project Structure

The project follows a clean architecture structure:

```
com.tutorial.rockty
├── data                # Data layer: Repositories, Mappers, Data Sources
│   ├── mapper          # Mappers to convert Data models to Domain models
│   └── repository      # Repository implementations
├── di                  # Dependency Injection modules
├── domain              # Domain layer: UseCases, Models, Repository Interfaces
│   ├── model           # Domain entities
│   ├── pagination      # Reusable pagination logic
│   ├── repository      # Repository interfaces
│   └── usecase         # Business logic use cases
├── navigation          # Navigation graph and routes
└── ui                  # UI layer: Screens, ViewModels, Components
    ├── detail          # Launch Detail feature
    ├── list            # Launch List feature
    └── theme           # App theme and styling
```

## Testing Strategy

This project emphasizes a robust testing strategy:

*   **Unit Tests:**
    *   **ViewModels:** Verify state updates and side effects (e.g., `LaunchListViewModelTest`, `LaunchDetailViewModelTest`).
    *   **Domain Logic:** Test core business logic like `PaginationManagerTest`.
    *   **Mocking:** Uses Mockito to mock repositories and use cases.
*   **Instrumented Tests:**
    *   **Screen Tests:** Verify UI rendering and interactions in isolation (e.g., `LaunchListScreenTest`, `LaunchDetailScreenTest`).
    *   **Navigation Tests:** Verify navigation flows between screens (e.g., `NavigationTest`).
    *   **Hilt Testing:** Uses `HiltAndroidRule` to inject mock dependencies in Android tests.

## Setup & Build

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/Rockty.git
    ```
2.  **Open in Android Studio:** Open the project in the latest stable version of Android Studio.
3.  **Sync Gradle:** Allow Gradle to sync dependencies.
4.  **Run the App:** Select the `app` configuration and run it on an emulator or physical device.

## License

This project is for educational purposes.

```
MIT License
...
```
