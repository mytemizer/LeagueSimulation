package com.mytemizer.leaguesimulator.ui.teamcreation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.ui.teamcreation.TeamTier
import com.mytemizer.leaguesimulator.ui.teamcreation.getDisplayName


@Composable
fun VerticalTeamGenerationOptions(
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
                text = stringResource(R.string.team_creation_quality_title),
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
                                text = tier.getDisplayName(),
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

@Preview
@Composable
private fun VerticalTeamGenerationOptionsPreview() {
    VerticalTeamGenerationOptions(
        selectedTier = TeamTier.MIXED,
        onTierSelected = {}
    )
}
