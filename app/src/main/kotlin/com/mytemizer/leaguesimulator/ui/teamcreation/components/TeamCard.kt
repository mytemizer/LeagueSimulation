package com.mytemizer.leaguesimulator.ui.teamcreation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.components.TeamColorsCircle
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.Team


@Composable
fun TeamCard(
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
                MiniStatBadge(label = stringResource(R.string.team_stat_attack), value = team.attack)
                MiniStatBadge(label = stringResource(R.string.team_stat_midfield), value = team.midfield)
                MiniStatBadge(label = stringResource(R.string.team_stat_defense), value = team.defense)
                MiniStatBadge(label = stringResource(R.string.team_stat_goalkeeper), value = team.goalkeeper)
            }
        }
    }
}

@Preview
@Composable
private fun TeamCardPreview() {
    LeagueSimulatorTheme {
        TeamCard(
            team = Team(
                id = 0,
                name = "Manchester United",
                shortName = "MUN",
                overallRating = 80
            )
        )
    }
}
