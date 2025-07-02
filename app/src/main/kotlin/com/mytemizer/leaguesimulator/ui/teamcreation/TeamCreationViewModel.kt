package com.mytemizer.leaguesimulator.ui.teamcreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.core.domain.usecase.GenerateTeamsUseCase
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.mytemizer.leaguesimulator.core.common.util.Constants

class TeamCreationViewModel(
    private val generateTeamsUseCase: GenerateTeamsUseCase,
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamCreationUiState())
    val uiState: StateFlow<TeamCreationUiState> = _uiState.asStateFlow()

    private val _teamsResult = MutableStateFlow<Resource<List<Team>>>(Resource.Success(emptyList()))
    val teamsResult: StateFlow<Resource<List<Team>>> = _teamsResult.asStateFlow()

    fun generateTeams() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isGenerating = true)
                _teamsResult.value = Resource.Loading

                val generationType = when (_uiState.value.selectedTier) {
                    TeamTier.WORLD_CLASS -> GenerateTeamsUseCase.GenerationType.WORLD_CLASS
                    TeamTier.EXCELLENT -> GenerateTeamsUseCase.GenerationType.EXCELLENT
                    TeamTier.GOOD -> GenerateTeamsUseCase.GenerationType.GOOD
                    TeamTier.MIXED -> GenerateTeamsUseCase.GenerationType.MIXED
                }

                val params = GenerateTeamsUseCase.GenerateTeamsParams(
                    teamCount = _uiState.value.selectedTeamCount,
                    generationType = generationType
                )

                val result = generateTeamsUseCase(params)
                if (result.isSuccess) {
                    _teamsResult.value = Resource.Success(result.getOrThrow())
                } else {
                    _teamsResult.value = Resource.Error(result.exceptionOrNull())
                }
            } catch (e: Exception) {
                _teamsResult.value = Resource.Error(e)
            } finally {
                _uiState.value = _uiState.value.copy(isGenerating = false)
            }
        }
    }

    fun selectTier(tier: TeamTier) {
        _uiState.value = _uiState.value.copy(selectedTier = tier)
    }

    fun selectTeamCount(teamCount: Int) {
        _uiState.value = _uiState.value.copy(selectedTeamCount = teamCount)
    }

    fun getGeneratedTeams(): List<Team> {
        return when (val result = _teamsResult.value) {
            is Resource.Success -> result.data
            else -> emptyList()
        }
    }

    fun initializeTournament() {
        val teams = getGeneratedTeams()
        if (teams.size >= 4 && teams.size % 2 == 0) {
            tournamentRepository.initializeTournament(teams)
        }
    }

    fun resetTeams() {
        _teamsResult.value = Resource.Success(emptyList())
    }
}

data class TeamCreationUiState(
    val selectedTier: TeamTier = TeamTier.MIXED,
    val selectedTeamCount: Int = Constants.DEFAULT_TEAMS_IN_LEAGUE,
    val isGenerating: Boolean = false
)
