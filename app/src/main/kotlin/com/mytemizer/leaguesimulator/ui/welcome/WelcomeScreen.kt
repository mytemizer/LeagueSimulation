package com.mytemizer.leaguesimulator.ui.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun WelcomeScreen(
    onStartTournament: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left side - Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.icon_match_simulation),
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // App Title
                Text(
                    text = stringResource(id = R.string.welcome_app_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Right side - Start Button
            Button(
                onClick = onStartTournament,
                modifier = Modifier
                    .width(160.dp)
                    .height(32.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.welcome_start_button),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Features List - Horizontal Layout
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
                    text = stringResource(R.string.welcome_features_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Two rows of features for better landscape layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureItem(
                        icon = stringResource(R.string.icon_random_teams),
                        title = stringResource(R.string.feature_random_teams_title),
                        description = stringResource(R.string.feature_random_teams_desc),
                        modifier = Modifier.weight(1f)
                    )

                    FeatureItem(
                        icon = stringResource(R.string.icon_match_simulation),
                        title = stringResource(R.string.feature_match_simulation_title),
                        description = stringResource(R.string.feature_match_simulation_desc),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureItem(
                        icon = stringResource(R.string.icon_live_standings),
                        title = stringResource(R.string.feature_live_standings_title),
                        description = stringResource(R.string.feature_live_standings_desc),
                        modifier = Modifier.weight(1f)
                    )

                    FeatureItem(
                        icon = stringResource(R.string.icon_tournament_progress),
                        title = stringResource(R.string.feature_tournament_progress_title),
                        description = "Follow match results",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    LeagueSimulatorTheme {
        WelcomeScreen(onStartTournament = {})
    }
}