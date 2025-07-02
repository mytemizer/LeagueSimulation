package com.mytemizer.leaguesimulator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mytemizer.leaguesimulator.ui.nextmatch.NextMatchScreen
import com.mytemizer.leaguesimulator.ui.standings.StandingsScreen
import com.mytemizer.leaguesimulator.ui.teamcreation.TeamCreationScreen
import com.mytemizer.leaguesimulator.ui.welcome.WelcomeScreen

/**
 * Navigation graph for the League Simulator app
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navigationActions = NavigationActions(navController)

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStartTournament = navigationActions::navigateToTeamCreation
            )
        }

        composable(Screen.TeamCreation.route) {
            TeamCreationScreen(
                onTeamsCreated = navigationActions::navigateToNextMatch
            )
        }

        composable(Screen.NextMatch.route) {
            NextMatchScreen(
                onMatchCompleted = navigationActions::navigateToStandings,
                onViewStandings = navigationActions::navigateToStandings,
                onResetTournament = navigationActions::startNewTournament
            )
        }

        composable(Screen.Standings.route) {
            StandingsScreen(
                onBackToMatch = navigationActions::navigateBackToMatch,
                onNewTournament = navigationActions::startNewTournament
            )
        }
    }
}
