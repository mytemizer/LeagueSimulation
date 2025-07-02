package com.mytemizer.leaguesimulator.ui.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.TextStyle
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.common.util.isError
import com.mytemizer.leaguesimulator.core.common.util.isLoading
import com.mytemizer.leaguesimulator.core.common.util.isSuccess
import com.mytemizer.leaguesimulator.core.domain.model.GroupStanding
import com.mytemizer.leaguesimulator.core.domain.model.GroupTable
import com.mytemizer.leaguesimulator.components.ErrorDisplay
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
                text = "📊 Standings" + if (uiState.isTournamentComplete) " - Final" else "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (!uiState.isTournamentComplete) {
                OutlinedButton(
                    onClick = onBackToMatch,
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("⚽ Back", fontSize = 12.sp)
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
                        viewModel.onNewTournament()
                        onNewTournament()
                    }
                )
            }

            groupTable.isError() -> {
                ErrorDisplay(
                    errorTitle = "❌ Error loading standings",
                    error = (groupTable as Resource.Error).exception,
                    onRetry = { viewModel.refreshStandings() }
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
                            text = "🆕 Start New Tournament",
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

/**
 * Calculate team form based on recent matches
 */
private fun calculateTeamForm(team: Team, matches: List<GroupMatch>): List<String> {
    val teamMatches = matches.filter {
        (it.homeTeam.name == team.name || it.awayTeam.name == team.name) && it.isPlayed
    }.sortedBy { it.round }

    return teamMatches.takeLast(3).map { match ->
        when {
            match.homeTeam.name == team.name -> {
                when {
                    match.homeGoals > match.awayGoals -> "W"
                    match.homeGoals == match.awayGoals -> "D"
                    else -> "L"
                }
            }

            else -> {
                when {
                    match.awayGoals > match.homeGoals -> "W"
                    match.awayGoals == match.homeGoals -> "D"
                    else -> "L"
                }
            }
        }
    }
}

@Composable
private fun GroupTableCard(
    standings: List<GroupStanding>,
    isTournamentComplete: Boolean,
    matches: List<GroupMatch> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "🏆 Group Table",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Compact Table Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Pos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "Team",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1.2f)
                )

                Text(
                    "Rate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    "P",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "W",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "D",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "L",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "GF",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "GA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "GD",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    "Form",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1.2f)
                )
                Text(
                    "Pts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            // Table Rows
            standings.forEach { standing ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = when {
                                isTournamentComplete && standing.position <= 2 ->
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)

                                standing.position <= 2 ->
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position with qualification indicator
                    Row(
                        modifier = Modifier.weight(0.5f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${standing.position}",
                            fontSize = 12.sp,
                            fontWeight = if (standing.position <= 2) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isTournamentComplete && standing.position <= 2) {
                            Text(
                                text = "✅",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = standing.team.shortName,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1.2f),
                        fontWeight = if (standing.position <= 2) FontWeight.Bold else FontWeight.Normal
                    )
                    Text("${standing.team.overallRating}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.played}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.won}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.drawn}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.lost}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.goalsFor}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text(
                        "${standing.goalsAgainst}",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(0.5f)
                    )
                    Text(
                        text = "${if (standing.goalDifference >= 0) "+" else ""}${standing.goalDifference}",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(0.5f),
                        color = when {
                            standing.goalDifference > 0 -> Color(0xFF4CAF50)
                            standing.goalDifference < 0 -> Color(0xFFF44336)
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )

                    // Form indicator
                    Row(
                        modifier = Modifier.weight(1.2f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        val teamForm = calculateTeamForm(standing.team, matches)

                        // Show last 3 matches or fill with placeholders
                        repeat(3) { index ->
                            val result = teamForm.getOrNull(index) ?: "-"

                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = when (result) {
                                            "W" -> Color(0xFF4CAF50)
                                            "D" -> Color(0xFFFF9800)
                                            "L" -> Color(0xFFF44336)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = result,
                                    style = TextStyle(                       // ⬅️ single place for all text attrs
                                        fontSize = 8.sp,
                                        lineHeight = 8.sp,                  // ⬅️ key: no extra leading
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        color = if (result == "-")
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        else
                                            Color.White
                                    )
                                )
                            }


                        }
                    }

                    Text(
                        text = "${standing.points}",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(0.5f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchResultsCard(matches: List<GroupMatch>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "⚽ Match Results",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            (1..3).forEach { round ->
                val roundMatches = matches.filter { it.round == round }
                if (roundMatches.isNotEmpty()) {
                    Text(
                        text = "Round $round",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )

                    roundMatches.forEach { match ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = match.homeTeam.shortName,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (match.isPlayed)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = match.getResultString(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }

                            Text(
                                text = match.awayTeam.shortName,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    if (round < 3) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TournamentSummaryCard(
    groupTable: GroupTable,
    isTournamentComplete: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📈 Tournament Statistics",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Matches Played:")
                    Text("Total Goals:")
                    Text("Avg. Goals/Match:")
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text("${groupTable.matches.count { it.isPlayed }} / ${groupTable.matches.size}")
                    Text("${groupTable.totalGoals}")
                    Text("${"%.1f".format(groupTable.averageGoalsPerMatch)}")
                }
            }

            if (isTournamentComplete) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "🏆 Tournament Complete!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun QualificationCard(standings: List<GroupStanding>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🎯 Final Qualification",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val qualifiedTeams = standings.take(2)
            val eliminatedTeams = standings.drop(2)

            Text(
                text = "✅ Qualified for Final:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            qualifiedTeams.forEach { standing ->
                Text(
                    text = "• ${standing.team.name} (${standing.position}${if (standing.position == 1) "st" else "nd"} place)",
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "❌ Eliminated:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF44336)
            )

            eliminatedTeams.forEach { standing ->
                Text(
                    text = "• ${standing.team.name} (${standing.position}${if (standing.position == 3) "rd" else "th"} place)",
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

