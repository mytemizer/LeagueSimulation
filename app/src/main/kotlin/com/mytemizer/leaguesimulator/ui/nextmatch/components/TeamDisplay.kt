package com.mytemizer.leaguesimulator.ui.nextmatch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.components.TeamColorsCircle
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.Team


@Composable
fun TeamDisplay(
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
            text = if (isHome) stringResource(R.string.next_match_home) else stringResource(R.string.next_match_away),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun TeamDisplayPreview() {
    LeagueSimulatorTheme {
        TeamDisplay(
            team = Team(
                id = 0,
                name = "Manchester United",
                shortName = "MUN",
                overallRating = 80
            ),
            isHome = true
        )
    }
}
