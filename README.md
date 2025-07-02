# League Simulator

A modern Android application for simulating football tournaments with realistic team generation, match simulation, and live standings tracking. Built with Jetpack Compose and Clean Architecture principles.

## 🏆 Features

### Core Functionality
- **Random Team Generation**: Create realistic football teams with varying skill levels (World Class, Excellent, Good, Mixed)
- **Match Simulation**: Advanced simulation engine with realistic scoring based on team attributes
- **Live Tournament Progress**: Track matches, standings, and tournament progression in real-time
- **Interactive Navigation**: Seamless navigation between Welcome, Team Creation, Match, and Standings screens

### Team Generation
- **Realistic Team Attributes**: Teams have individual ratings for Attack, Midfield, Defense, and Goalkeeper
- **Quality Tiers**: Choose from different team quality levels to create balanced or varied tournaments
- **Authentic Details**: Teams include realistic names, cities, stadiums, colors, and founding years
- **Dynamic Ratings**: Overall team rating calculated from weighted individual attributes

### Match Simulation
- **Advanced Algorithm**: Considers team strengths, home advantage, and randomness factors
- **Realistic Scoring**: Goal simulation based on attacking strength vs defensive capabilities
- **Round-Robin Format**: Complete group stage with 6 matches across 3 rounds
- **Match Results**: Detailed score tracking with winner determination

### Tournament Management
- **Group Standings**: Rules compliant ranking system (Points → Goal Difference → Goals For → Goals Against → Head-to-Head)
- **Progress Tracking**: Visual indicators for current round, matches played, and tournament completion
- **State Management**: Persistent tournament state throughout the application lifecycle

## 🏗️ Architecture

The application follows **Clean Architecture** principles with a modular design:

```
LeagueSimulator/
├── app/                          # Presentation layer (UI, ViewModels, Navigation)
├── core/
│   ├── common/                   # Shared utilities and extensions
│   ├── design/                   # UI components and theme
│   ├── domain/                   # Business logic, use cases, models
│   ├── data/                     # Repository implementations
```

### Architecture Principles
- **Separation of Concerns**: Clear boundaries between layers
- **Dependency Inversion**: Dependencies point inward toward the domain layer
- **Single Responsibility**: Each module has a focused purpose
- **Testability**: Isolated components for easy unit testing

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI toolkit
- **Compose Navigation**: Type-safe navigation between screens
- **Material 3**: Google's latest design system

### Architecture Components
- **ViewModel**: UI-related data management with lifecycle awareness
- **StateFlow**: Reactive state management
- **Coroutines**: Asynchronous programming and concurrency

### Dependency Injection
- **Koin**: Lightweight dependency injection framework

### Development Tools
- **Gradle Version Catalogs**: Centralized dependency management
- **Kotlin DSL**: Type-safe build scripts

## 📋 Rules

### Tournament Format
The application simulates a **group stage tournament** with 4 teams playing in a round-robin format where each team plays every other team once.

### Match Structure
- **Total Matches**: 6 matches across 3 rounds
- **Round 1**: Team 1 vs Team 2, Team 3 vs Team 4
- **Round 2**: Team 1 vs Team 3, Team 2 vs Team 4
- **Round 3**: Team 1 vs Team 4, Team 2 vs Team 3

### Scoring System
- **Win**: 3 points
- **Draw**: 1 point
- **Loss**: 0 points

### Group Standings
Teams are ranked according to the following criteria in order of priority:

1. **Points** (highest first)
2. **Goal Difference** (highest first)
3. **Goals For** (highest first)
4. **Goals Against** (lowest first)
5. **Head-to-Head Result** (direct match result between tied teams)

### Knockout Stage Advancement
At the end of the group stage, the tournament results show:
- **Final group positions** (1st, 2nd, 3rd, 4th)
- **Complete match results** between all teams
- **Teams advancing to knockout stage**: Top 2 teams (1st and 2nd place) qualify for the next round

The graphical end result displays the final group table with all mutual results, clearly indicating which teams advance based on the sorting criteria.

## 📱 Screens

### 1. Welcome Screen
- Tournament introduction and feature overview
- Start tournament button
- Landscape-optimized layout with feature highlights

### 2. Team Creation Screen
- Team quality selection (World Class, Excellent, Good, Mixed)
- Random team generation with realistic attributes
- Team preview with ratings and colors
- Compact UI design for landscape orientation

### 3. Next Match Screen
- Current match display with team information
- Match simulation with realistic scoring
- Tournament progress tracking
- Navigation to standings

### 4. Standings Screen
- Live group table with rules complaint ranking
- Match results history
- Tournament summary and statistics
- New tournament option when complete

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or higher
- Android SDK with API level 24+ (Android 7.0)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/mytemizer/LeagueSimulation.git
   cd LeagueSimulation
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Sync the project**
   - Android Studio will automatically sync Gradle dependencies
   - Wait for the sync to complete

4. **Run the application**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Ctrl+R` (Windows/Linux) / `Cmd+R` (Mac)

### Build Variants
- **Debug**: Development build with debugging enabled
- **Release**: Production build with optimizations

## 📖 Usage

### Starting a Tournament
1. Launch the app and tap "Start Tournament" on the Welcome screen
2. Select your preferred team quality tier
3. Tap "Generate Teams" to create 4 random teams
4. Review the generated teams and tap "Start Tournament"

### Simulating Matches
1. View the current match details on the Next Match screen
2. Tap "Simulate Match" to run the match simulation
3. View the result and tap "Next Match" to continue
4. Access live standings anytime via "View Standings"

### Viewing Results
1. Check the Standings screen for current group table
2. Review match results and tournament statistics
3. Start a new tournament when the current one is complete

## 🧪 Testing

### Running Tests
```bash
# Unit tests
./gradlew test

# All tests
./gradlew check
```

### Test Structure
- **Unit Tests**: Business logic and use case testing

## 🔧 Development

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names in English
- Maintain SOLID principles

### Architecture Guidelines
- Keep ViewModels lightweight and focused on UI state
- Implement business logic in use cases
- Use repository pattern for data access
- Extract hardcoded strings to string resources

## 📦 Dependencies

### Core Dependencies
```kotlin
// Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose")

// Architecture
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")

// Dependency Injection
implementation("io.insert-koin:koin-android")
implementation("io.insert-koin:koin-androidx-compose")
```

## 🙏 Acknowledgments

- **Jetpack Compose** team for the modern UI toolkit
- **Material Design** for the comprehensive design system
- **Koin** community for the lightweight DI framework
- Football simulation algorithms inspired by real-world match statistics
