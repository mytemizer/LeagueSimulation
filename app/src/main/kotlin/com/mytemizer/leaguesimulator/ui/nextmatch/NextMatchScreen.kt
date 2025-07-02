package com.mytemizer.leaguesimulator.ui.nextmatch

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.common.util.isError
import com.mytemizer.leaguesimulator.core.common.util.isLoading
import com.mytemizer.leaguesimulator.core.common.util.isSuccess
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.components.ErrorDisplay
import com.mytemizer.leaguesimulator.components.TeamColorsCircle
import org.koin.androidx.compose.koinViewModel

@Composable
fun NextMatchScreen(
    onMatchCompleted: () -> Unit,
    onViewStandings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NextMatchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentMatch by viewModel.currentMatch.collectAsStateWithLifecycle()

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
                text = "⚽ Next Match - Round ${uiState.currentRound}/3",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = onViewStandings,
                modifier = Modifier.height(32.dp)
            ) {
                Text("📊 Standings", fontSize = 12.sp)
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
                    currentMatch.isLoading() -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    currentMatch.isSuccess() -> {
                        val matchData = (currentMatch as Resource.Success).data
                        if (matchData != null) {
                            MatchDisplay(
                                match = matchData,
                                isSimulating = uiState.isSimulating,
                                onSimulateMatch = { viewModel.simulateCurrentMatch() },
                                onNextMatch = {
                                    if (uiState.hasMoreMatches) {
                                        viewModel.loadNextMatch()
                                    } else {
                                        onMatchCompleted()
                                    }
                                }
                            )
                        } else {
                            TournamentCompleted(
                                onViewFinalStandings = {
                                    onMatchCompleted()
                                }
                            )
                        }
                    }
                    currentMatch.isError() -> {
                        ErrorDisplay(
                            errorTitle = "❌ Error loading match",
                            error = (currentMatch as Resource.Error).exception,
                            onRetry = { viewModel.loadNextMatch() }
                        )
                    }
                }
            }

            // Right side - Tournament Progress
            Column(
                modifier = Modifier.weight(0.4f)
            ) {
                TournamentProgress(
                    currentRound = uiState.currentRound,
                    totalRounds = 3,
                    matchesPlayed = uiState.matchesPlayed,
                    totalMatches = 6
                )
            }
        }

        // Add bottom padding to ensure content is visible
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MatchDisplay(
    match: GroupMatch,
    isSimulating: Boolean,
    onSimulateMatch: () -> Unit,
    onNextMatch: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Compact Match Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Teams Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home Team
                    TeamDisplay(
                        team = match.homeTeam,
                        isHome = true,
                        modifier = Modifier.weight(1f)
                    )

                    // VS or Score
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.8f)
                    ) {
                        if (match.isPlayed) {
                            // Show Score
                            AnimatedContent(
                                targetState = "${match.homeGoals} - ${match.awayGoals}",
                                transitionSpec = {
                                    slideInVertically { it } + fadeIn() with
                                    slideOutVertically { -it } + fadeOut()
                                }
                            ) { score ->
                                Text(
                                    text = score,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = "FINAL",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "VS",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Away Team
                    TeamDisplay(
                        team = match.awayTeam,
                        isHome = false,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Match Action Button
                if (!match.isPlayed) {
                    Button(
                        onClick = onSimulateMatch,
                        enabled = !isSimulating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        if (isSimulating) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simulating...", fontSize = 14.sp)
                            }
                        } else {
                            Text(
                                text = "⚽ Simulate Match",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // Match Result Summary
                    MatchResultSummary(match = match)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNextMatch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "➡️ Next Match",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamDisplay(
    team: Team,
    isHome: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (isHome) Alignment.Start else Alignment.End
    ) {
        // Team name and colors in a row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isHome) Arrangement.Start else Arrangement.End
        ) {
            if (isHome) {
                // For home team: Circle first, then names
                TeamColorsCircle(
                    primaryColor = team.primaryColor,
                    secondaryColor = team.secondaryColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = team.shortName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = team.name,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }
            } else {
                // For away team: Names first, then circle
                Column {
                    Text(
                        text = team.shortName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = team.name,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                TeamColorsCircle(
                    primaryColor = team.primaryColor,
                    secondaryColor = team.secondaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = when {
                    team.overallRating >= 85 -> Color(0xFF4CAF50)
                    team.overallRating >= 75 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }
            )
        ) {
            Text(
                text = "${team.overallRating}",
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isHome) "🏠 Home" else "✈️ Away",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MatchResultSummary(match: GroupMatch) {
    val winner = match.getWinner()
    val isDraw = match.isDraw()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isDraw -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when {
                    isDraw -> "🤝 Draw!"
                    winner != null -> "🏆 ${winner.shortName} Wins!"
                    else -> "Match Completed"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("${match.homeTeam.shortName}: ${if (isDraw) 1 else if (winner == match.homeTeam) 3 else 0} pts")
                Text("${match.awayTeam.shortName}: ${if (isDraw) 1 else if (winner == match.awayTeam) 3 else 0} pts")
            }
        }
    }
}

@Composable
private fun TournamentProgress(
    currentRound: Int,
    totalRounds: Int,
    matchesPlayed: Int,
    totalMatches: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "🏆 Tournament Progress",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Round Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Round:", fontSize = 12.sp)
                Text("$currentRound / $totalRounds", fontSize = 12.sp)
            }

            LinearProgressIndicator(
                progress = currentRound.toFloat() / totalRounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            // Match Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Matches:", fontSize = 12.sp)
                Text("$matchesPlayed / $totalMatches", fontSize = 12.sp)
            }

            LinearProgressIndicator(
                progress = matchesPlayed.toFloat() / totalMatches,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun TournamentCompleted(
    onViewFinalStandings: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆",
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tournament Complete!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "All matches have been played.\nCheck the final standings!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onViewFinalStandings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📊 View Final Standings")
            }
        }
    }
}

