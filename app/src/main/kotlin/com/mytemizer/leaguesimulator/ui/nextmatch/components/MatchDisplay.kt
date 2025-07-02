package com.mytemizer.leaguesimulator.ui.nextmatch.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MatchDisplay(
    match: GroupMatch,
    isSimulating: Boolean,
    onSimulateMatch: () -> Unit,
    onNextMatch: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Compact Match Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Teams Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home Team
                    TeamDisplay(
                        team = match.homeTeam,
                        isHome = true,
                        modifier = Modifier.weight(1f)
                    )

                    // VS or Score
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.8f).padding(horizontal = 8.dp)
                    ) {
                        if (match.isPlayed) {
                            // Show Score
                            AnimatedContent(
                                targetState = "${match.homeGoals} - ${match.awayGoals}",
                                transitionSpec = {
                                    slideInVertically { it } + fadeIn() with
                                            slideOutVertically { -it } + fadeOut()
                                }
                            ) { score ->
                                Text(
                                    text = score,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = stringResource(R.string.next_match_final),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.next_match_vs),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Away Team
                    TeamDisplay(
                        team = match.awayTeam,
                        isHome = false,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Match Action Button
                if (!match.isPlayed) {
                    Button(
                        onClick = onSimulateMatch,
                        enabled = !isSimulating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        if (isSimulating) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(stringResource(R.string.next_match_simulating), fontSize = 14.sp)
                            }
                        } else {
                            Text(
                                text = stringResource(R.string.next_match_simulate_button),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // Match Result Summary
                    MatchResultSummary(match = match)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNextMatch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next_match_next_button),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MatchDisplayPreview() {
    MatchDisplay(
        match = GroupMatch(
            homeTeam = Team(
                id = 1,
                name = "Manchester United",
                shortName = "MUN",
                overallRating = 80
            ),
            awayTeam = Team(
                id = 2,
                name = "Liverpool FC",
                shortName = "LIV",
                overallRating = 85
            ),
            isPlayed = false,
            round = 1,
            groupId = 1L
        ),
        isSimulating = false,
        onSimulateMatch = {},
        onNextMatch = {}
    )
}
