# Star Wars SWAPI Explorer

A modern, high-performance Android application for exploring the Star Wars universe using data from the Star Wars API (SWAPI). Built with Jetpack Compose, Material 3 Adaptive Layouts, Clean Architecture with strict `contract` vs `impl` module separation, MVI pattern, and full static analysis & automated CI/CD setup.

---

## 🌟 Overview & Key Features

- **Multi-Category SWAPI Explorer**: Discover entities across all major Star Wars categories:
  - **People**: Characters (Luke Skywalker, Darth Vader, etc.)
  - **Starships**: Iconic vessels (X-wing, Millennium Falcon, etc.)
  - **Planets**: Worlds (Tatooine, Hoth, Coruscant, etc.)
  - **Species**: Alien species (Wookiee, Yoda's species, etc.)
  - **Films**: All canonical episodes with complete cast and lore details.
- **Hub & Spoke Material 3 Adaptive Layout**: Leverages Material 3 `ListDetailPaneScaffold` to deliver a responsive Hub & Spoke navigation experience across phones, foldables, and large-screen tablets.
- **Local Assets Fallback Image Resolution**: Features an intelligent `CharacterImageResolver` that maps remote entity URLs and character names to optimized local asset high-resolution photos with fallback mechanics.
- **Deep Recursive Entity Navigation**: Seamlessly traverse entity relationships recursively (e.g. Character -> Film -> Starship -> Species -> Homeworld Planet) with stack history.
- **Minimalist Star Wars Visual Theme**: Custom M3 dark theme crafted for deep immersion:
  - **Background**: Deep Space Obsidian (`#050505`)
  - **Surface & Cards**: Imperial Grey (`#1A1A1A`)
  - **Primary Highlights**: Lightsaber Yellow (`#FFE81F`)
  - **Accents**: Jedi Blue (`#2E67F8`) & Sith Red (`#EB212E`)

---

## 🏗️ Clean Architecture & Package Structure

The project enforces strict Clean Architecture principles with explicit separation between **Public Contracts/Interfaces** (`contract`) and **Encapsulated Implementations** (`impl`) across `data`, `domain`, and `presentation` layers.

```
com.slashgil.starwars/
├── MainActivity.kt
├── StarWarsApplication.kt
├── data/
│   ├── contract/                   # Public data interfaces & models
│   │   └── PersonRepository.kt
│   └── impl/                       # Data sources, DTOs, Mappers, Repositories
│       ├── EntityMappers.kt
│       ├── FilmDto.kt
│       ├── PersonDto.kt
│       ├── PlanetDto.kt
│       ├── SpeciesDto.kt
│       ├── StarshipDto.kt
│       ├── RetrofitClient.kt
│       ├── SwapiService.kt
│       └── StarWarsRepositoryImpl.kt
├── di/                             # Hilt Dependency Injection Modules
│   ├── NetworkModule.kt
│   ├── RepositoryModule.kt
│   └── UseCaseModule.kt
├── domain/
│   ├── contract/                   # Domain entities, Repository interfaces, UseCase contracts
│   │   ├── Category.kt
│   │   ├── StarWarsEntity.kt       # Sealed hierarchy (Person, Film, Planet, Starship, Species)
│   │   ├── CharacterImageResolver.kt
│   │   ├── StarWarsRepository.kt
│   │   ├── GetEntitiesUseCase.kt
│   │   ├── GetEntityByIdUseCase.kt
│   │   └── SearchEntitiesUseCase.kt
│   └── impl/                       # UseCase implementations & Resolvers
│       ├── CharacterImageResolverImpl.kt
│       ├── GetEntitiesUseCaseImpl.kt
│       ├── GetEntityByIdUseCaseImpl.kt
│       └── SearchEntitiesUseCaseImpl.kt
├── presentation/
│   ├── contract/                   # View contracts, Intent, and UiState
│   │   ├── StarWarsUiState.kt      # Immutable UI State
│   │   ├── StarWarsIntent.kt       # User Intents
│   │   └── StarWarsApp.kt          # Top-level composable contract
│   └── impl/                       # ViewModel & Compose Screen implementations
│       ├── StarWarsViewModel.kt
│       ├── HubScreen.kt            # Category list & entity grid
│       ├── SpokeScreen.kt          # Adaptive detail view & entity relationships
│       └── SharedElementUtils.kt
├── ui/theme/                       # M3 Star Wars Palette & Typography
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── util/
    └── StringExtensions.kt
```

---

## 🎨 Design Patterns & Architecture

1. **MVI (Model-View-Intent)**:
   - `StarWarsUiState`: Unidirectional flow with immutable state containing active category, search queries, pagination state, selected entity, and deep stack history.
   - `StarWarsIntent`: Expressive intents handling category switching, query changes, entity selection, recursion stack navigation, and retry operations.
2. **Repository Pattern**:
   - `StarWarsRepository`: Centralized access layer handling network requests to SWAPI, paging logic, and fallback domain mapping.
3. **Use Cases**:
   - Business logic encapsulated into single-responsibility use cases (`GetEntitiesUseCase`, `GetEntityByIdUseCase`, `SearchEntitiesUseCase`).
4. **Domain Resolvers**:
   - `CharacterImageResolver`: Resolves entity images and cross-references lore metadata (film titles, character names, homeworlds) from SWAPI URIs.
5. **Compose Adaptive Navigation**:
   - `ListDetailPaneScaffold` dynamically renders single-pane (mobile) or dual-pane (tablet/desktop) configurations based on window size classes.

---

## 🛠️ Tech Stack & Dependencies

| Layer / Concern | Technology / Library |
| :--- | :--- |
| **UI Framework** | Jetpack Compose Material 3 Expressive, Material Icons Extended |
| **Adaptive Layouts** | `androidx.compose.material3.adaptive` |
| **Dependency Injection** | Hilt (`com.google.dagger:hilt-android`) & KSP |
| **Networking** | Retrofit 2 + OkHttp3 Logging Interceptor |
| **Serialization** | `kotlinx.serialization.json` |
| **Image Loading** | Coil Compose (`io.coil-kt:coil-compose`) |
| **Static Analysis** | Detekt (`1.23.8`), Android Lint |
| **Unit Testing** | MockK, Kotlinx Coroutines Test, JUnit 4 |
| **UI & Integration Testing** | Robolectric (`4.17`), Compose UI Test |
| **Snapshot Testing** | CashApp Paparazzi (`2.0.0-alpha05`) |
| **CI/CD** | GitHub Actions & Firebase App Distribution |

---

## 🧪 Testing Strategy

The application features a comprehensive multi-layered test suite ensuring stability, visual regression prevention, and contract compliance:

### 1. MockK Unit Tests
- Verifies business logic in `StarWarsViewModel`, `StarWarsRepositoryImpl`, `CharacterImageResolverImpl`, and Use Cases.
- Isolates network operations and tests error handling, pagination, and data transformations.

### 2. Robolectric UI Interactive Tests
- Runs interactive Jetpack Compose component tests directly on the JVM without requiring an emulator.
- Exercises user flows in `StarWarsAppRobolectricTest`, `HubScreenRobolectricTest`, and `SpokeScreenRobolectricTest`.

### 3. Paparazzi Golden Screenshot Tests
- Captures pixel-perfect visual snapshots of composables (`HubScreenPaparazziTest`, `SpokeScreenPaparazziTest`).
- Prevents visual regressions across loading, error, and content states.

---

## 🔍 Static Analysis & Verification

Static analysis tools are configured to enforce Kotlin coding standards and prevent defects:

- **Detekt**: Configured via `config/detekt/detekt.yml` with rules for code complexity, naming conventions, and coroutines safety.
- **Android Lint**: Configured via `app/lint.xml` and `build.gradle.kts`.

### Running Verification Commands

```bash
# Run Detekt static code analysis
./gradlew detekt

# Run Android Lint on debug build
./gradlew lintDebug

# Run Unit & Robolectric Tests
./gradlew testDebugUnitTest

# Build Debug APK
./gradlew assembleDebug

# Run Full Verification Pipeline
./gradlew detekt lintDebug testDebugUnitTest assembleDebug
```

---

## 🚀 CI/CD Pipeline

The project includes an automated GitHub Actions pipeline (`.github/workflows/firebase_distribution.yml`):
1. **Checkout & JDK Setup**: Configures JDK 17 environment.
2. **Static Analysis**: Runs `./gradlew detekt lintDebug`.
3. **Unit Tests**: Runs `./gradlew testDebugUnitTest`.
4. **Build**: Assembles debug and release APKs (`./gradlew assembleDebug assembleRelease`).
5. **Distribution**: Automatically uploads the generated debug APK to Firebase App Distribution.
