package com.mytemizer.leaguesimulator.ui.teamcreation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.ui.teamcreation.TeamCreationViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun TeamsDisplay(
    teams: List<Team>,
    onTeamsConfirmed: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = stringResource(R.string.team_creation_tournament_teams),
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
                        TeamCard(
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
                    text = stringResource(R.string.team_creation_tournament_info),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.team_creation_advance_info),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Start Tournament Button
            Button(
                enabled = teams.isNotEmpty(),
                onClick = {
                    onTeamsConfirmed()
                },
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = stringResource(R.string.team_creation_start_tournament),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Add bottom padding for scrolling
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun TeamsDisplayPreview() {
    LeagueSimulatorTheme {
        TeamsDisplay(
            teams = listOf(
                Team(
                    id = 1,
                    name = "Manchester United",
                    shortName = "MUN",
                    overallRating = 80
                ),
                Team(
                    id = 2,
                    name = "Liverpool FC",
                    shortName = "LIV",
                    overallRating = 85
                ),
                Team(
                    id = 3,
                    name = "Arsenal FC",
                    shortName = "ARS",
                    overallRating = 75
                ),
                Team(
                    id = 4,
                    name = "Chelsea FC",
                    shortName = "CHE",
                    overallRating = 82
                )
            ),
            onTeamsConfirmed = {}
        )
    }
}
