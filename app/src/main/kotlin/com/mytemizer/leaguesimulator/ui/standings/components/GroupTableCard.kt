package com.mytemizer.leaguesimulator.ui.standings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mytemizer.leaguesimulator.R
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.GroupStanding
import com.mytemizer.leaguesimulator.core.domain.model.Team
import kotlin.collections.forEach


@Composable
fun GroupTableCard(
    standings: List<GroupStanding>,
    isTournamentComplete: Boolean,
    matches: List<GroupMatch> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.group_table_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Compact Table Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.group_table_header_pos),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_team),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1.2f)
                )

                Text(
                    stringResource(R.string.group_table_header_rate),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    stringResource(R.string.group_table_header_played),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_won),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_drawn),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_lost),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_goals_for),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_goals_against),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_goal_difference),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    stringResource(R.string.group_table_header_form),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1.2f)
                )
                Text(
                    stringResource(R.string.group_table_header_points),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.5f)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            // Table Rows
            standings.forEach { standing ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = when {
                                isTournamentComplete && standing.position <= 2 ->
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)

                                standing.position <= 2 ->
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position with qualification indicator
                    Row(
                        modifier = Modifier.weight(0.5f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${standing.position}",
                            fontSize = 12.sp,
                            fontWeight = if (standing.position <= 2) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isTournamentComplete && standing.position <= 2) {
                            Text(
                                text = "✅",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = standing.team.shortName,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1.2f),
                        fontWeight = if (standing.position <= 2) FontWeight.Bold else FontWeight.Normal
                    )
                    Text("${standing.team.overallRating}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.played}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.won}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.drawn}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.lost}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text("${standing.goalsFor}", fontSize = 12.sp, modifier = Modifier.weight(0.5f))
                    Text(
                        "${standing.goalsAgainst}",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(0.5f)
                    )
                    Text(
                        text = "${if (standing.goalDifference >= 0) "+" else ""}${standing.goalDifference}",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(0.5f),
                        color = when {
                            standing.goalDifference > 0 -> Color(0xFF4CAF50)
                            standing.goalDifference < 0 -> Color(0xFFF44336)
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )

                    // Form indicator
                    Row(
                        modifier = Modifier.weight(1.2f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        val teamForm = calculateTeamForm(standing.team, matches)

                        // Show last 3 matches or fill with placeholders
                        val noResultString = stringResource(R.string.form_no_result)
                        repeat(3) { index ->
                            val result = teamForm.getOrNull(index) ?: noResultString

                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = when (result) {
                                            stringResource(R.string.form_win) -> Color(0xFF4CAF50)
                                            stringResource(R.string.form_draw) -> Color(0xFFFF9800)
                                            stringResource(R.string.form_loss) -> Color(0xFFF44336)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        },
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = result,
                                    style = TextStyle(                       // ⬅️ single place for all text attrs
                                        fontSize = 8.sp,
                                        lineHeight = 8.sp,                  // ⬅️ key: no extra leading
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        color = if (result == noResultString)
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        else
                                            Color.White
                                    )
                                )
                            }


                        }
                    }

                    Text(
                        text = "${standing.points}",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(0.5f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


/**
 * Calculate team form based on recent matches
 */
@Composable
private fun calculateTeamForm(team: Team, matches: List<GroupMatch>): List<String> {
    val teamMatches = matches.filter {
        (it.homeTeam.name == team.name || it.awayTeam.name == team.name) && it.isPlayed
    }.sortedBy { it.round }

    val winString = stringResource(R.string.form_win)
    val drawString = stringResource(R.string.form_draw)
    val lossString = stringResource(R.string.form_loss)

    return teamMatches.takeLast(3).map { match ->
        when {
            match.homeTeam.name == team.name -> {
                when {
                    match.homeGoals > match.awayGoals -> winString
                    match.homeGoals == match.awayGoals -> drawString
                    else -> lossString
                }
            }

            else -> {
                when {
                    match.awayGoals > match.homeGoals -> winString
                    match.awayGoals == match.homeGoals -> drawString
                    else -> lossString
                }
            }
        }
    }
}

@Preview
@Composable
private fun GroupTableCardPreview() {
    LeagueSimulatorTheme {
        GroupTableCard(
            standings = listOf(
                GroupStanding(
                    team = Team(
                        name = "Team 1",
                        shortName = "T1",
                    ),
                    position = 1,
                    played = 3,
                    won = 2,
                    drawn = 0,
                    lost = 1,
                    goalsFor = 8,
                    goalsAgainst = 2,
                    points = 6
                ),
                GroupStanding(
                    team = Team(
                        name = "Team 2",
                        shortName = "T2",
                    ),
                    position = 2,
                    played = 3,
                    won = 1,
                    drawn = 1,
                    lost = 1,
                    goalsFor = 4,
                    goalsAgainst = 4,
                    points = 4
                ),
                GroupStanding(
                    team = Team(
                        name = "Team 3",
                        shortName = "T3",
                    ),
                    position = 3,
                    played = 3,
                    won = 1,
                    drawn = 0,
                    lost = 2,
                    goalsFor = 3,
                    goalsAgainst = 5,
                    points = 3
                ),
                GroupStanding(
                    team = Team(
                        name = "Team 4",
                        shortName = "T4",
                    ),
                    position = 4,
                    played = 3,
                    won = 0,
                    drawn = 1,
                    lost = 2,
                    goalsFor = 1,
                    goalsAgainst = 5,
                    points = 1
                ),
            ),
            isTournamentComplete = false,
            matches = emptyList()
        )
    }
}