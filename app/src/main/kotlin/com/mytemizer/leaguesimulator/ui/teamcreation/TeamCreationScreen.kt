package com.mytemizer.leaguesimulator.ui.teamcreation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.common.util.isError
import com.mytemizer.leaguesimulator.core.common.util.isLoading
import com.mytemizer.leaguesimulator.core.common.util.isSuccess
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.components.ErrorDisplay
import com.mytemizer.leaguesimulator.core.design.theme.LeagueSimulatorTheme
import com.mytemizer.leaguesimulator.ui.teamcreation.components.TeamsDisplay
import com.mytemizer.leaguesimulator.ui.teamcreation.components.VerticalTeamGenerationOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun TeamCreationScreen(
    onTeamsCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamCreationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val teamsResult by viewModel.teamsResult.collectAsStateWithLifecycle()

    TeamCreationScreenContent(
        modifier,
        getUiState = { uiState },
        getTeamsResult = { teamsResult },
        onTeamsCreated = {
            viewModel.resetTeams()
            onTeamsCreated()
        },
        selectTier = { tier: TeamTier -> viewModel.selectTier(tier) },
        generateTeams = { viewModel.generateTeams() },
        initializeTournament = { viewModel.initializeTournament() }
    )

}

@Composable
private fun TeamCreationScreenContent(
    modifier: Modifier,
    getUiState: () -> TeamCreationUiState,
    getTeamsResult: () -> Resource<List<Team>>,
    onTeamsCreated: () -> Unit,
    selectTier: (TeamTier) -> Unit,
    generateTeams: () -> Unit,
    initializeTournament: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {

        // Horizontal layout: Team Quality + Teams Display
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left side - Team Generation Options
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight()
            ) {

                Text(
                    text = stringResource(R.string.team_creation_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                VerticalTeamGenerationOptions(
                    selectedTier = getUiState().selectedTier,
                    onTierSelected = { selectTier(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Generate Button
                Button(
                    onClick = { generateTeams() },
                    enabled = !getUiState().isGenerating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                ) {
                    if (getUiState().isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 1.5.dp
                        )
                    } else {
                        Text(
                            stringResource(R.string.team_creation_generate_button),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Right side - Teams Display
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxHeight()
            ) {
                when {
                    getTeamsResult().isLoading() -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    stringResource(R.string.team_creation_generating_text),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    getTeamsResult().isSuccess() -> {
                        TeamsDisplay(
                            teams = (getTeamsResult() as Resource.Success).data,
                            onTeamsConfirmed = {
                                initializeTournament()
                                onTeamsCreated()
                            }
                        )
                    }

                    getTeamsResult().isError() -> {
                        ErrorDisplay(
                            errorTitle = stringResource(R.string.team_creation_error_title),
                            error = (getTeamsResult() as Resource.Error).exception,
                            onRetry = { generateTeams() }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TeamCreationScreenPreview() {
    LeagueSimulatorTheme {
        TeamCreationScreenContent(
            modifier = Modifier,
            getUiState = {
                TeamCreationUiState()
            },
            getTeamsResult = {
                Resource.Success(emptyList())
            },
            onTeamsCreated = {},
            selectTier = {},
            generateTeams = {},
            initializeTournament = {},
        )
    }
}
