package com.mytemizer.leaguesimulator.ui.nextmatch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme


@Composable
fun TournamentProgress(
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
                text = stringResource(R.string.tournament_progress_title),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Round Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.tournament_progress_round), fontSize = 12.sp)
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
                Text(stringResource(R.string.tournament_progress_matches), fontSize = 12.sp)
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

@Preview
@Composable
private fun TournamentProgressPreview() {
    LeagueSimulatorTheme {
        TournamentProgress(
            currentRound = 2,
            totalRounds = 3,
            matchesPlayed = 4,
            totalMatches = 6
        )
    }
}