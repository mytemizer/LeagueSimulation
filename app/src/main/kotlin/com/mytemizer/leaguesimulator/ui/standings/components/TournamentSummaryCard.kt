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
import androidx.compose.material3.HorizontalDivider
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
import com.mytemizer.leaguesimulator.core.domain.model.GroupTable
import kotlin.collections.count


@Composable
fun TournamentSummaryCard(
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
                text = stringResource(R.string.tournament_stats_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.tournament_stats_matches_played))
                    Text(stringResource(R.string.tournament_stats_total_goals))
                    Text(stringResource(R.string.tournament_stats_avg_goals))
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text("${groupTable.matches.count { it.isPlayed }} / ${groupTable.matches.size}")
                    Text("${groupTable.totalGoals}")
                    Text("%.1f".format(groupTable.averageGoalsPerMatch))
                }
            }

            if (isTournamentComplete) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.tournament_complete),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun TournamentSummaryCardPreview() {
    TournamentSummaryCard(
        groupTable = GroupTable(
            groupId = 1,
            standings = emptyList(),
            matches = emptyList()
        ),
        isTournamentComplete = false
    )
}
