# CodeOrbit To-Do List App

A native Android To-Do List application developed using **Kotlin** and **Jetpack Compose** as part of the CodeOrbit Tech Android Development Internship.

The app helps users manage daily tasks, organize them by priority, track completion, and keep their tasks saved locally.

## Features

- **Create tasks** — Add tasks with a title, description, and priority.
- **Edit tasks** — Update existing task details.
- **Delete tasks** — Remove tasks with a confirmation dialog.
- **Track completion** — Mark tasks as completed or pending.
- **Task priorities** — Assign Low, Medium, or High priority.
- **Sort tasks** — Sort by newest first, oldest first, or priority.
- **Local persistence** — Store tasks locally using Room.
- **Reactive UI** — Display task data through Kotlin Flow and Jetpack Compose.

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary programming language |
| Jetpack Compose | Declarative UI development |
| Material 3 | UI components and theming |
| Room | Local SQLite database |
| Kotlin Coroutines | Asynchronous operations |
| Kotlin Flow | Reactive data streams |
| ViewModel | UI state and lifecycle-aware logic |
| Hilt | Dependency injection |
| Gradle | Build and dependency management |

## Architecture

The application follows a layered architecture to separate UI, business logic, and data access.

- **Presentation:** Compose screens, reusable UI components, ViewModels, and UI state.
- **Domain:** Task models, priority and sorting enums, and repository contracts.
- **Data:** Room entities, DAOs, database configuration, mappers, and repository implementation.
- **Dependency Injection:** Hilt modules for providing application dependencies.

### Data Flow

```text
Jetpack Compose UI
        ↓
     ViewModel
        ↓
    Repository
        ↓
     Room DAO
        ↓
   Local Database
```

Task updates flow back through the repository and ViewModel to refresh the UI.

## Getting Started

### Prerequisites

- Android Studio
- Android SDK compatible with the project configuration
- JDK version required by the project's Gradle setup
- Git

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/ubaidraye/CodeOrbit_To-do_List_App
   ```

2. Open the project in Android Studio.

3. Allow Gradle to sync and download the required dependencies.

4. Connect an Android device or start an emulator.

5. Run the `app` configuration from Android Studio.

Alternatively, build the debug APK from the project root:

```bash
./gradlew assembleDebug
```

The APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

```text
app/src/main/java/com/raye/codeorbit_todo_list_app/
├── data/
│   ├── local/
│   ├── mapper/
│   └── repository/
├── di/
├── domain/
│   ├── model/
│   └── repository/
├── presentation/
│   └── tasklist/
│       ├── components/
│       ├── TaskListScreen.kt
│       ├── TaskListUiState.kt
│       └── TaskListViewModel.kt
└── MainActivity.kt
```

## Data Storage

Tasks are stored locally using Room, backed by SQLite. Each task includes:

- ID
- Title
- Description
- Completion status
- Priority
- Creation timestamp

The application does not require a remote backend for its core task-management functionality.

## Future Improvements

- Search and filter tasks
- Due dates and reminders
- Improved accessibility
- Unit and UI tests
- Database migration support
- Additional sorting and filtering options

## Author

Developed as part of the **CodeOrbit Tech Android Development Internship**.

