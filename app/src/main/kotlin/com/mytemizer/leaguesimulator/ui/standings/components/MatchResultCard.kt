package com.mytemizer.leaguesimulator.ui.standings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.Team


@Composable
fun MatchResultsCard(matches: List<GroupMatch>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.match_results_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            (1..3).forEach { round ->
                val roundMatches = matches.filter { it.round == round }
                if (roundMatches.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.match_results_round, round),
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

@Preview
@Composable
private fun MatchResultsCardPreview() {
    MatchResultsCard(
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
                isPlayed = false
            ),
            GroupMatch(
                homeTeam = Team(name = "Team 1", shortName = "T1"),
                awayTeam = Team(name = "Team 4", shortName = "T4"),
                isPlayed = false
            ),
            GroupMatch(
                homeTeam = Team(name = "Team 2", shortName = "T2"),
                awayTeam = Team(name = "Team 3", shortName = "T3"),
                isPlayed = false
            )
        )
    )
}
