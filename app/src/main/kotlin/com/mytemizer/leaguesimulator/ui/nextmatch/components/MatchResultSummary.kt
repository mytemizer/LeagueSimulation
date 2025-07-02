package com.mytemizer.leaguesimulator.ui.nextmatch.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.Team


@Composable
fun MatchResultSummary(match: GroupMatch) {
    val winner = match.getWinner()
    val isDraw = match.isDraw()

    fun getPoints(team: Team): Int = when {
        isDraw -> 1
        winner == team -> 3
        else -> 0
    }

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
                    isDraw -> stringResource(R.string.match_result_draw)
                    winner != null -> stringResource(R.string.match_result_wins, winner.shortName)
                    else -> stringResource(R.string.match_result_completed)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("${match.homeTeam.shortName}: ${getPoints(match.homeTeam)}${stringResource(R.string.match_result_points_suffix)}")
                Text("${match.awayTeam.shortName}: ${getPoints(match.awayTeam)}${stringResource(R.string.match_result_points_suffix)}")
            }
        }
    }
}

@Preview
@Composable
private fun MatchResultSummaryPreview() {
    MatchResultSummary(
        match = GroupMatch(
            homeTeam = Team(name = "Team A", shortName = "A"),
            awayTeam = Team(name = "Team B", shortName = "B"),
            homeGoals = 2,
            awayGoals = 1,
            isPlayed = true
        )
    )
}
