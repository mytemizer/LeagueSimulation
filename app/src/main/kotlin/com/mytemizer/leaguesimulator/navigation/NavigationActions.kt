package com.mytemizer.leaguesimulator.navigation

import androidx.navigation.NavController

/**
 * Navigation actions for the League Simulator app
 * Provides type-safe navigation methods
 */
class NavigationActions(private val navController: NavController) {

    /**
     * Navigate to Welcome screen
     */
    fun navigateToWelcome() {
        navController.navigate(Screen.Welcome.route) {
            // Clear the back stack to prevent going back to previous screens
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    /**
     * Navigate to Team Creation screen
     */
    fun navigateToTeamCreation() {
        navController.navigate(Screen.TeamCreation.route)
    }

    /**
     * Navigate to Next Match screen
     */
    fun navigateToNextMatch() {
        navController.navigate(Screen.NextMatch.route)
    }

    /**
     * Navigate to Standings screen
     */
    fun navigateToStandings() {
        navController.navigate(Screen.Standings.route)
    }

    /**
     * Navigate back to the previous screen
     */
    fun navigateBack() {
        navController.popBackStack()
    }

    /**
     * Navigate back to Next Match screen from Standings
     */
    fun navigateBackToMatch() {
        navController.popBackStack(Screen.NextMatch.route, inclusive = false)
    }

    /**
     * Start a new tournament - navigate to Welcome and clear back stack
     */
    fun startNewTournament() {
        navigateToWelcome()
    }
}
