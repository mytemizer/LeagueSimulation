pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LeagueSimulator"

// App module
include(":app")

// Core modules
include(":core:common")
include(":core:design")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:database")

// Feature modules
include(":feature:league")
include(":feature:simulation")
include(":feature:teams")
include(":feature:players")
