package com.mytemizer.leaguesimulator.ui.standings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.common.util.isError
import com.mytemizer.leaguesimulator.core.common.util.isLoading
import com.mytemizer.leaguesimulator.core.common.util.isSuccess
import com.mytemizer.leaguesimulator.core.domain.model.GroupTable
import com.mytemizer.leaguesimulator.components.ErrorDisplay
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.GroupStanding
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.ui.standings.components.GroupTableCard
import com.mytemizer.leaguesimulator.ui.standings.components.MatchResultsCard
import com.mytemizer.leaguesimulator.ui.standings.components.QualificationCard
import com.mytemizer.leaguesimulator.ui.standings.components.TournamentSummaryCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun StandingsScreen(
    onBackToMatch: () -> Unit,
    onNewTournament: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StandingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val groupTable by viewModel.groupTable.collectAsStateWithLifecycle()

    // Refresh standings when screen is displayed
    LaunchedEffect(Unit) {
        viewModel.refreshStandings()
    }

    StandingsScreenContainer(
        modifier, uiState, groupTable, onBackToMatch,
        onNewTournament = {
            viewModel.onNewTournament()
            onNewTournament()
        },
        refreshStandings = {
            viewModel.refreshStandings()
        }
    )
}

@Composable
private fun StandingsScreenContainer(
    modifier: Modifier,
    uiState: StandingsUiState,
    groupTable: Resource<GroupTable>,
    onBackToMatch: () -> Unit,
    onNewTournament: () -> Unit,
    refreshStandings: () -> Unit
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
                text = if (uiState.isTournamentComplete)
                    stringResource(R.string.standings_title_final)
                else
                    stringResource(R.string.standings_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (!uiState.isTournamentComplete) {
                OutlinedButton(
                    onClick = onBackToMatch,
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(stringResource(R.string.standings_back_button), fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            groupTable.isLoading() -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            groupTable.isSuccess() -> {
                StandingsContent(
                    groupTable = (groupTable as Resource.Success).data,
                    isTournamentComplete = uiState.isTournamentComplete,
                    onNewTournament = {
                        onNewTournament()
                    }
                )
            }

            groupTable.isError() -> {
                ErrorDisplay(
                    errorTitle = stringResource(R.string.standings_error_title),
                    error = (groupTable as Resource.Error).exception,
                    onRetry = { refreshStandings() }
                )
            }
        }

        // Add bottom padding
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun StandingsContent(
    groupTable: GroupTable,
    isTournamentComplete: Boolean,
    onNewTournament: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Standings and Match Results in a row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Left side - Group Table
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                GroupTableCard(
                    standings = groupTable.standings,
                    isTournamentComplete = isTournamentComplete,
                    matches = groupTable.matches
                )

                Spacer(modifier = Modifier.height(12.dp))


                if (isTournamentComplete) {
                    QualificationCard(standings = groupTable.standings)
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (isTournamentComplete) {
                    // Compact New Tournament Button
                    Button(
                        onClick = onNewTournament,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.standings_new_tournament),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Right side - Match Results
            Column(
                modifier = Modifier.weight(0.35f)
            ) {
                MatchResultsCard(matches = groupTable.matches)
                Spacer(modifier = Modifier.height(12.dp))


                // Tournament Summary
                TournamentSummaryCard(
                    groupTable = groupTable,
                    isTournamentComplete = isTournamentComplete
                )
            }
        }
    }
}

@Preview
@Composable
private fun StandingsScreenPreview() {
    LeagueSimulatorTheme {
        StandingsScreenContainer(
            modifier = Modifier,
            uiState = StandingsUiState(isTournamentComplete = true),
            groupTable = Resource.Success(
                GroupTable(
                    groupId = 1,
                    standings = listOf(
                        GroupStanding(
                            team = Team(name = "Team 1", shortName = "T1"),
                            points = 3,
                            goalsFor = 2,
                            goalsAgainst = 1,
                        ),
                        GroupStanding(
                            team = Team(name = "Team 2", shortName = "T2"),
                            points = 3,
                            goalsFor = 2,
                            goalsAgainst = 1,
                        ),
                        GroupStanding(
                            team = Team(name = "Team 3", shortName = "T3"),
                            points = 3,
                            goalsFor = 2,
                            goalsAgainst = 1,
                        ),
                        GroupStanding(
                            team = Team(name = "Team 4", shortName = "T4"),
                            points = 3,
                            goalsFor = 2,
                            goalsAgainst = 1,
                        )
                    ),
                    matches = listOf(
                        GroupMatch(
                            homeTeam = Team(name = "Team 1", shortName = "T1"),
                            awayTeam = Team(name = "Team 2", shortName = "T2"),
                            homeGoals = 2,
                            awayGoals = 1,
                            isPlayed = true
                        ),
                        GroupMatch(
                            homeTeam = Team(name = "Team 3", shortName = "T3"),
                            awayTeam = Team(name = "Team 4", shortName = "T4"),
                            homeGoals = 1,
                            awayGoals = 1,
                            isPlayed = true
                        ),
                        GroupMatch(
                            homeTeam = Team(name = "Team 1", shortName = "T1"),
                            awayTeam = Team(name = "Team 3", shortName = "T3"),
                            homeGoals = 1,
                            awayGoals = 2,
                            isPlayed = true
                        ),
                        GroupMatch(
                            homeTeam = Team(name = "Team 2", shortName = "T2"),
                            awayTeam = Team(name = "Team 4", shortName = "T4"),
                            homeGoals = 1,
                            awayGoals = 1,
                            isPlayed = true
                        ),
                        GroupMatch(
                            homeTeam = Team(name = "Team 1", shortName = "T1"),
                            awayTeam = Team(name = "Team 4", shortName = "T4"),
                            homeGoals = 3,
                            awayGoals = 0,
                            isPlayed = true
                        ),
                        GroupMatch(
                            homeTeam = Team(name = "Team 2", shortName = "T2"),
                            awayTeam = Team(name = "Team 3", shortName = "T3"),
                            homeGoals = 1,
                            awayGoals = 1,
                            isPlayed = true
                        )
                    )
                )
            ),
            onNewTournament = {},
            refreshStandings = {},
            onBackToMatch = {}
        )
    }
}
