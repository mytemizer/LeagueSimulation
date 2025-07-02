package com.mytemizer.leaguesimulator.ui.teamcreation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.common.util.isError
import com.mytemizer.leaguesimulator.core.common.util.isLoading
import com.mytemizer.leaguesimulator.core.common.util.isSuccess
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.components.ErrorDisplay
import com.mytemizer.leaguesimulator.components.TeamColorsCircle
import org.koin.androidx.compose.koinViewModel

@Composable
fun TeamCreationScreen(
    onTeamsCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamCreationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val teamsResult by viewModel.teamsResult.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {

        // Horizontal layout: Team Quality + Teams Display
        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left side - Team Generation Options
            Column(
                modifier = Modifier.weight(0.2f)
                    .fillMaxHeight()
            ) {

                Text(
                    text = "🎲 Create Teams",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                VerticalTeamGenerationOptions(
                    selectedTier = uiState.selectedTier,
                    onTierSelected = { viewModel.selectTier(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Generate Button
                Button(
                    onClick = { viewModel.generateTeams() },
                    enabled = !uiState.isGenerating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                ) {
                    if (uiState.isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 1.5.dp
                        )
                    } else {
                        Text("🔄 Generate", fontSize = 12.sp)
                    }
                }
            }

            // Right side - Teams Display
            Column(
                modifier = Modifier.weight(0.8f).fillMaxHeight()
            ) {
                when  {
                    teamsResult.isLoading() -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Generating teams...", fontSize = 12.sp)
                            }
                        }
                    }
                    teamsResult.isSuccess() -> {
                        TeamsDisplay(
                            teams = (teamsResult as Resource.Success).data,
                            onTeamsConfirmed = {
                                viewModel.resetTeams()
                                onTeamsCreated()
                            }
                        )
                    }
                    teamsResult.isError() -> {
                        ErrorDisplay(
                            errorTitle = "❌ Failed to generate teams",
                            error = (teamsResult as Resource.Error).exception,
                            onRetry = { viewModel.generateTeams() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerticalTeamGenerationOptions(
    selectedTier: TeamTier,
    onTierSelected: (TeamTier) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(6.dp)
        ) {
            Text(
                text = "Team Quality",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Vertical arrangement of team quality options - compact
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                TeamTier.entries.forEach { tier ->
                    FilterChip(
                        onClick = { onTierSelected(tier) },
                        label = {
                            Text(
                                text = tier.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = selectedTier == tier,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamsDisplay(
    teams: List<Team>,
    onTeamsConfirmed: () -> Unit,
    viewModel: TeamCreationViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "🏆 Tournament Teams",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // 2-column grid for teams
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            teams.chunked(2).forEach { rowTeams ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowTeams.forEach { team ->
                        CompactTeamCard(
                            team = team,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill remaining space if odd number of teams
                    if (rowTeams.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Compact Tournament Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "4 teams • 3 rounds • 6 matches",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Top 2 advance to final",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Start Tournament Button
            Button(
                enabled = teams.isNotEmpty(),
                onClick = {
                    viewModel.initializeTournament()
                    onTeamsConfirmed()
                },
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "⚽ Start Tournament",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Add bottom padding for scrolling
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun CompactTeamCard(
    team: Team,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            // Team Info and Overall Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team name and colors
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TeamColorsCircle(
                        primaryColor = team.primaryColor,
                        secondaryColor = team.secondaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = team.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = team.shortName,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Overall Rating
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
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Compact Team Stats in a row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MiniStatBadge(label = "ATT", value = team.attack)
                MiniStatBadge(label = "MID", value = team.midfield)
                MiniStatBadge(label = "DEF", value = team.defense)
                MiniStatBadge(label = "GK", value = team.goalkeeper)
            }
        }
    }
}

@Composable
private fun MiniStatBadge(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.toString(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                value >= 85 -> Color(0xFF4CAF50)
                value >= 70 -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            }
        )
    }
}

enum class TeamTier(val displayName: String) {
    WORLD_CLASS("World Class"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    MIXED("Mixed")
}
