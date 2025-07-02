# League Simulator

A modern Android application for simulating football tournaments with realistic team generation, match simulation, and live standings tracking. Built with Jetpack Compose and Clean Architecture principles.

## 🏆 Features

### Core Functionality
- **Flexible Team Count**: Choose from 4, 6, 8, 10, or 12 teams for varied tournament sizes
- **Random Team Generation**: Create realistic football teams with varying skill levels (World Class, Excellent, Good, Mixed)
- **Match Simulation**: Advanced simulation engine with realistic scoring based on team attributes
- **Skip All Matches**: Fast-forward through remaining matches with one-click tournament completion
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
- **Dynamic Round-Robin Format**: Scalable tournament structure (4 teams = 3 rounds, 6 teams = 5 rounds, etc.)
- **Circle Method Scheduling**: Proper round-robin algorithm ensuring each team plays once per round
- **Match Results**: Detailed score tracking with winner determination
- **Real-time Updates**: Live match results updating during simulation

### Tournament Management
- **FIFA-Compliant Standings**: Official ranking system (Points → Goal Difference → Goals For → Goals Against → Head-to-Head)
- **Dynamic Round Display**: Shows all rounds based on tournament size in match results
- **Progress Tracking**: Visual indicators for current round, matches played, and tournament completion
- **Knockout Advancement**: Clear indication of which teams advance to next stage
- **State Management**: Persistent tournament state throughout the application lifecycle
- **Reset Confirmation**: Back button protection with confirmation dialog

## 🏗️ Architecture

The application follows **Clean Architecture** principles with a modular design:

```
LeagueSimulator/
├── app/                          # Presentation layer (UI, ViewModels, Navigation)
├── core/
│   ├── common/                   # Shared utilities and extensions
│   ├── design/                   # UI components and theme (LeagueSimulatorTheme)
│   ├── domain/                   # Business logic, use cases, models
│   └── data/                     # Repository implementations
└── gradle/                       # Gradle wrapper and configuration
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
The application simulates a **group stage tournament** with 4/6/8/10/12 teams playing in a round-robin format where each team plays every other team once.

### Match Structure
- **Total Matches**: 6 matches across 3 rounds for 4 teams
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
- **Team Count Selection**: Choose between 4, 6, 8, 10, or 12 teams with horizontal selector
- **Team Quality Selection**: World Class, Excellent, Good, Mixed tiers
- **Random Team Generation**: Realistic attributes, names, cities, and colors
- **Dynamic Tournament Info**: Shows total rounds and matches based on team count
- **Team Preview**: Ratings, colors, and team details
- **Compact UI Design**: Optimized for landscape orientation

### 3. Next Match Screen
- **Dynamic Round Display**: Shows current round vs total rounds (e.g., "Round 2/5")
- **Current Match Display**: Team information with ratings and colors
- **Match Simulation**: Realistic scoring with loading animations
- **Skip All Matches**: One-click button to simulate remaining matches with loading state
- **Tournament Progress**: Visual indicators for rounds and matches
- **Navigation Options**: View standings or continue to next match
- **Reset Protection**: Back button confirmation dialog

### 4. Standings Screen
- **Live Group Table**: FIFA-compliant ranking with real-time updates
- **Dynamic Match Results**: Shows all rounds organized by round number
- **Qualification Status**: Clear indication of advancing teams (top 2)
- **Tournament Summary**: Statistics and completion status
- **Real-time Updates**: Automatically refreshes during match simulation
- **New Tournament**: Option to start fresh tournament when complete

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
2. **Select team count** (4, 6, 8, 10, or 12 teams) using the horizontal selector
3. **Choose team quality tier** (World Class, Excellent, Good, Mixed)
4. Tap "Generate Teams" to create random teams with realistic attributes
5. Review the generated teams and tournament info (rounds/matches)
6. Tap "Start Tournament" to begin

### Simulating Matches
1. View the current match details on the Next Match screen
2. **Individual Match**: Tap "Simulate Match" to run single match simulation
3. **Skip All Matches**: Tap "⏭️ Skip All" to simulate all remaining matches at once
4. View results and tap "Next Match" to continue (or auto-navigate after skip)
5. **Access Standings**: Tap "📊 Standings" anytime to view live results
6. **Tournament Progress**: Monitor rounds and matches in real-time

### Viewing Results
1. **Live Standings**: Check group table with FIFA-compliant ranking
2. **Match Results**: Review all rounds and match outcomes organized by round
3. **Qualification Status**: See which teams advance to knockout stage (top 2)
4. **Tournament Statistics**: View goals, matches played, and completion status
5. **New Tournament**: Start fresh when current tournament is complete

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
