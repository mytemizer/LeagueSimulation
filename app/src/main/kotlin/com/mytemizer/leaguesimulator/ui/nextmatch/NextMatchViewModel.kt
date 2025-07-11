package com.mytemizer.leaguesimulator.ui.nextmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.simulation.MatchSimulator
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import com.mytemizer.leaguesimulator.core.domain.usecase.ResetTournamentUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NextMatchViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchSimulator: MatchSimulator,
    private val resetTournamentUseCase: ResetTournamentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NextMatchUiState())
    val uiState: StateFlow<NextMatchUiState> = _uiState.asStateFlow()

    private val _currentMatch = MutableStateFlow<Resource<GroupMatch?>>(Resource.Loading)
    val currentMatch: StateFlow<Resource<GroupMatch?>> = _currentMatch.asStateFlow()

    init {
        loadNextMatch()

        // Observe tournament state changes and auto-refresh
        tournamentRepository.getTournamentStatus()
            .onEach { tournamentState ->
                _uiState.value = _uiState.value.copy(
                    currentRound = tournamentState.currentRound,
                    matchesPlayed = tournamentState.matchesPlayed,
                    totalMatches = tournamentState.totalMatches,
                    totalRounds = tournamentState.totalRounds,
                    hasMoreMatches = tournamentState.hasMoreMatches
                )
                loadNextMatch()
            }
            .launchIn(viewModelScope)
    }

    fun loadNextMatch() {
        viewModelScope.launch {
            try {
                _currentMatch.value = Resource.Loading

                val nextMatch = tournamentRepository.getNextMatch()
                val currentRound = tournamentRepository.getCurrentRound()
                val matchesPlayed = tournamentRepository.getPlayedMatchesCount()
                val hasMoreMatches = tournamentRepository.hasMoreMatches()

                _uiState.value = _uiState.value.copy(
                    currentRound = currentRound,
                    matchesPlayed = matchesPlayed,
                    hasMoreMatches = hasMoreMatches
                )

                _currentMatch.value = Resource.Success(nextMatch)
            } catch (e: Exception) {
                _currentMatch.value = Resource.Error(e)
            }
        }
    }

    fun simulateCurrentMatch() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSimulating = true)

                val currentMatchValue = (_currentMatch.value as? Resource.Success)?.data
                if (currentMatchValue != null) {
                    // Add some delay for dramatic effect
                    delay(1500)

                    // Simulate the match
                    val simulatedMatch = matchSimulator.simulateMatch(
                        homeTeam = currentMatchValue.homeTeam,
                        awayTeam = currentMatchValue.awayTeam,
                        round = currentMatchValue.round,
                        groupId = currentMatchValue.groupId
                    )

                    // Update tournament with result
                    tournamentRepository.updateMatchResult(simulatedMatch)

                    // Update UI with simulated match
                    _currentMatch.value = Resource.Success(simulatedMatch)
                }
            } catch (e: Exception) {
                _currentMatch.value = Resource.Error(e)
            } finally {
                _uiState.value = _uiState.value.copy(isSimulating = false)
            }
        }
    }

    fun resetTournament() {
        viewModelScope.launch {
            resetTournamentUseCase.invoke()
        }
    }

    fun skipAllMatches(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSimulating = true)

                // Add a small delay to show loading state
                delay(500)

                // Simulate all remaining matches
                while (tournamentRepository.hasMoreMatches()) {
                    val nextMatch = tournamentRepository.getNextMatch()
                    if (nextMatch != null) {
                        val simulatedMatch = matchSimulator.simulateMatch(
                            homeTeam = nextMatch.homeTeam,
                            awayTeam = nextMatch.awayTeam,
                            round = nextMatch.round,
                            groupId = nextMatch.groupId
                        )
                        tournamentRepository.updateMatchResult(simulatedMatch)

                        // Small delay between matches for better UX
                        delay(100)
                    }
                }

                // Update UI state to reflect tournament completion
                _uiState.value = _uiState.value.copy(
                    hasMoreMatches = false,
                    isSimulating = false
                )
                // Notify completion
                onComplete()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isSimulating = false)
            }
        }
    }
}

data class NextMatchUiState(
    val currentRound: Int = 1,
    val matchesPlayed: Int = 0,
    val totalMatches: Int = 0,
    val totalRounds: Int = 0,
    val hasMoreMatches: Boolean = true,
    val isSimulating: Boolean = false
)
