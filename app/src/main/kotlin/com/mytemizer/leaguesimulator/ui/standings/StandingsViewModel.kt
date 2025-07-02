package com.mytemizer.leaguesimulator.ui.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytemizer.leaguesimulator.core.common.util.Resource
import com.mytemizer.leaguesimulator.core.domain.model.GroupTable
import com.mytemizer.leaguesimulator.core.domain.usecase.ResetTournamentUseCase
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class StandingsViewModel(
    private val tournamentRepository: TournamentRepository,
    private val resetTournamentUseCase: ResetTournamentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StandingsUiState())
    val uiState: StateFlow<StandingsUiState> = _uiState.asStateFlow()

    private val _groupTable = MutableStateFlow<Resource<GroupTable>>(Resource.Loading)
    val groupTable: StateFlow<Resource<GroupTable>> = _groupTable.asStateFlow()

    init {
        refreshStandings()

        // Observe tournament state changes and auto-refresh
        tournamentRepository.getTournamentStatus()
            .onEach {
                refreshStandings()
            }
            .launchIn(viewModelScope)
    }

    fun refreshStandings() {
        viewModelScope.launch {
            try {
                _groupTable.value = Resource.Loading

                val currentGroupTable = tournamentRepository.getCurrentGroupTable()
                val isTournamentComplete = tournamentRepository.isTournamentComplete()

                _uiState.value = _uiState.value.copy(
                    isTournamentComplete = isTournamentComplete
                )

                _groupTable.value = Resource.Success(currentGroupTable)
            } catch (e: Exception) {
                _groupTable.value = Resource.Error(e)
            }
        }
    }

    fun onNewTournament() {
        viewModelScope.launch {
            resetTournamentUseCase.invoke()
            _uiState.value = _uiState.value.copy(isTournamentComplete = false)
        }
    }
}

data class StandingsUiState(
    val isTournamentComplete: Boolean = false
)
