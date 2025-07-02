package com.mytemizer.leaguesimulator.ui.teamcreation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme

@Composable
fun TeamCountSelector(
    selectedTeamCount: Int,
    onTeamCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val teamCountOptions = listOf(4, 6, 8, 10, 12)

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.team_creation_team_count),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            teamCountOptions.forEach { teamCount ->
                TeamCountOption(
                    teamCount = teamCount,
                    isSelected = teamCount == selectedTeamCount,
                    onSelected = { onTeamCountSelected(teamCount) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TeamCountOption(
    teamCount: Int,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelected
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$teamCount",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Preview
@Composable
private fun TeamCountSelectorPreview() {
    LeagueSimulatorTheme {
        TeamCountSelector(
            selectedTeamCount = 6,
            onTeamCountSelected = {}
        )
    }
}
