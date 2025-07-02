package com.mytemizer.leaguesimulator.ui.nextmatch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.common.util.isError
import com.mytemizer.leaguesimulator.core.common.util.isLoading
import com.mytemizer.leaguesimulator.core.common.util.isSuccess
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.components.ErrorDisplay
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.ui.nextmatch.components.MatchDisplay
import com.mytemizer.leaguesimulator.ui.nextmatch.components.TournamentProgress
import com.mytemizer.leaguesimulator.ui.nextmatch.dialog.ResetTournamentDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun NextMatchScreen(
    onMatchCompleted: () -> Unit,
    onViewStandings: () -> Unit,
    onResetTournament: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NextMatchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentMatch by viewModel.currentMatch.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    // Handle back button press
    BackHandler {
        showResetDialog = true
    }

    // Reset tournament confirmation dialog
    if (showResetDialog) {
        ResetTournamentDialog(
            onConfirm = {
                showResetDialog = false
                viewModel.resetTournament()
                onResetTournament()
            },
            onDismiss = {
                showResetDialog = false
            }
        )
    }

    NextMatchScreenContent(modifier,
        getUiState = { uiState },
        getCurrentMatch = { currentMatch },
        onMatchCompleted, onViewStandings,
        simulateCurrentMatch = viewModel::simulateCurrentMatch,
        loadNextMatch = viewModel::loadNextMatch
    )
}

@Composable
fun NextMatchScreenContent(
    modifier: Modifier,
    getUiState: () -> NextMatchUiState,
    getCurrentMatch: () -> Resource<GroupMatch?>,
    onMatchCompleted: () -> Unit,
    onViewStandings: () -> Unit,
    simulateCurrentMatch: () -> Unit,
    loadNextMatch: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        // Compact Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.next_match_title, getUiState().currentRound, getUiState().totalRounds),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = onViewStandings,
                modifier = Modifier.height(32.dp)
            ) {
                Text(stringResource(R.string.next_match_standings_button), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Match Display and Tournament Progress in a row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left side - Match Display
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                when  {
                    getCurrentMatch().isLoading() -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    getCurrentMatch().isSuccess() -> {
                        val matchData = (getCurrentMatch() as Resource.Success).data
                        if (matchData != null) {
                            MatchDisplay(
                                match = matchData,
                                isSimulating = getUiState().isSimulating,
                                onSimulateMatch = { simulateCurrentMatch() },
                                onNextMatch = {
                                    if (getUiState().hasMoreMatches) {
                                        loadNextMatch()
                                    } else {
                                        onMatchCompleted()
                                    }
                                }
                            )
                        }
                    }
                    getCurrentMatch().isError() -> {
                        ErrorDisplay(
                            errorTitle = stringResource(R.string.next_match_error_title),
                            error = (getCurrentMatch() as Resource.Error).exception,
                            onRetry = { loadNextMatch() }
                        )
                    }
                }
            }

            // Right side - Tournament Progress
            Column(
                modifier = Modifier.weight(0.4f)
            ) {
                TournamentProgress(
                    currentRound = getUiState().currentRound,
                    totalRounds = getUiState().totalRounds,
                    matchesPlayed = getUiState().matchesPlayed,
                    totalMatches = getUiState().totalMatches
                )
            }
        }

        // Add bottom padding to ensure content is visible
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun NextMatchScreenPreview() {
    LeagueSimulatorTheme {
        NextMatchScreenContent(
            modifier = Modifier,
            getUiState = { NextMatchUiState(currentRound = 2, matchesPlayed = 4, totalMatches = 6, totalRounds = 3, hasMoreMatches = true) },
            getCurrentMatch = {
                Resource.Success(
                    GroupMatch(
                        homeTeam = Team(
                            id = 1,
                            name = "Manchester United",
                            shortName = "MUN",
                            overallRating = 80
                        ),
                        awayTeam = Team(
                            id = 2,
                            name = "Liverpool FC",
                            shortName = "LIV",
                            overallRating = 85
                        ),
                        isPlayed = false,
                        round = 2,
                        groupId = 1L
                    )
                )
            },
            onMatchCompleted = {},
            onViewStandings = {},
            simulateCurrentMatch = {},
            loadNextMatch = {}
        )
    }
}
