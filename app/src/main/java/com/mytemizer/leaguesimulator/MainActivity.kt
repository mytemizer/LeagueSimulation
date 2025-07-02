package com.mytemizer.leaguesimulator

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mytemizer.leaguesimulator.navigation.Screen
import com.mytemizer.leaguesimulator.ui.nextmatch.NextMatchScreen
import com.mytemizer.leaguesimulator.ui.standings.StandingsScreen
import com.mytemizer.leaguesimulator.ui.teamcreation.TeamCreationScreen
import com.mytemizer.leaguesimulator.ui.welcome.WelcomeScreen
import com.mytemizer.leaguesimulator.ui.theme.LeagueSimulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force landscape orientation programmatically
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        enableEdgeToEdge()
        setContent {
            LeagueSimulatorTheme {
                var currentScreen by remember { mutableStateOf(Screen.Welcome.route) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        Screen.Welcome.route -> {
                            WelcomeScreen(
                                onStartTournament = { currentScreen = Screen.TeamCreation.route },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        Screen.TeamCreation.route -> {
                            TeamCreationScreen(
                                onTeamsCreated = { currentScreen = Screen.NextMatch.route },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        Screen.NextMatch.route -> {
                            NextMatchScreen(
                                onMatchCompleted = { currentScreen = Screen.Standings.route },
                                onViewStandings = { currentScreen = Screen.Standings.route },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        Screen.Standings.route -> {
                            StandingsScreen(
                                onBackToMatch = { currentScreen = Screen.NextMatch.route },
                                onNewTournament = { currentScreen = Screen.Welcome.route },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
