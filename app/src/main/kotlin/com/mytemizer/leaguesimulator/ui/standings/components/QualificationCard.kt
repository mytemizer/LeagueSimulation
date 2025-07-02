package com.mytemizer.leaguesimulator.ui.standings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.GroupStanding
import com.mytemizer.leaguesimulator.core.domain.model.Team


@Composable
fun QualificationCard(standings: List<GroupStanding>) {
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
                text = stringResource(R.string.qualification_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val qualifiedTeams = standings.take(2)
            val eliminatedTeams = standings.drop(2)

            Text(
                text = stringResource(R.string.qualification_qualified),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            qualifiedTeams.forEach { standing ->
                val placeString = if (standing.position == 1)
                    stringResource(R.string.qualification_place_1st)
                else
                    stringResource(R.string.qualification_place_2nd)
                Text(
                    text = "• ${standing.team.name} (${standing.position}$placeString ${
                        stringResource(
                            R.string.qualification_place_suffix
                        )
                    })",
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.qualification_eliminated),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF44336)
            )

            eliminatedTeams.forEach { standing ->
                val placeString = if (standing.position == 3)
                    stringResource(R.string.qualification_place_3rd)
                else
                    stringResource(R.string.qualification_place_4th)
                Text(
                    text = "• ${standing.team.name} (${standing.position}$placeString ${
                        stringResource(
                            R.string.qualification_place_suffix
                        )
                    })",
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun QualificationCardPreview() {
    LeagueSimulatorTheme {
        QualificationCard(
            standings = listOf(
                GroupStanding(team = Team(name = "Team 1", shortName = "T1"), position = 1),
                GroupStanding(team = Team(name = "Team 2", shortName = "T2"), position = 2),
                GroupStanding(team = Team(name = "Team 3", shortName = "T3"), position = 3),
                GroupStanding(team = Team(name = "Team 4", shortName = "T4"), position = 4)
            )
        )
    }
}

