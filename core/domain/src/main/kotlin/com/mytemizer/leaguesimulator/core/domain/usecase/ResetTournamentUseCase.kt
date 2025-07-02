package com.mytemizer.leaguesimulator.core.domain.usecase

import com.mytemizer.leaguesimulator.core.domain.repository.TeamRepository
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository

class ResetTournamentUseCase(
    private val teamRepository: TeamRepository,
    private val tournamentRepository: TournamentRepository

) {

    suspend operator fun invoke() {
        teamRepository.resetTeams()
        tournamentRepository.resetTournament()
    }
}