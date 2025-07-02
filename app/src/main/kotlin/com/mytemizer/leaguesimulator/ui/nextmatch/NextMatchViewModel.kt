package com.mytemizer.leaguesimulator.ui.nextmatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.simulation.MatchSimulator
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NextMatchViewModel(
    private val tournamentRepository: TournamentRepository,
    private val matchSimulator: MatchSimulator
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NextMatchUiState())
    val uiState: StateFlow<NextMatchUiState> = _uiState.asStateFlow()
    
    private val _currentMatch = MutableStateFlow<Resource<GroupMatch?>>(Resource.Loading)
    val currentMatch: StateFlow<Resource<GroupMatch?>> = _currentMatch.asStateFlow()
    
    init {
        loadNextMatch()

        // Observe tournament state changes and auto-refresh
        tournamentRepository.getTournamentStatus()
            .onEach {
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
}

data class NextMatchUiState(
    val currentRound: Int = 1,
    val matchesPlayed: Int = 0,
    val hasMoreMatches: Boolean = true,
    val isSimulating: Boolean = false
)
