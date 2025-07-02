package com.mytemizer.leaguesimulator.navigation

/**
 * Sealed class representing all screens in the app
 */
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object TeamCreation : Screen("team_creation")
    object NextMatch : Screen("next_match")
    object Standings : Screen("standings")
}
