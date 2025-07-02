package com.mytemizer.leaguesimulator.core.domain.repository

import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.GroupTable
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.core.domain.model.TournamentState
import kotlinx.coroutines.flow.StateFlow

interface TournamentRepository {

    fun getTournamentStatus(): StateFlow<TournamentState>
    fun initializeTournament(teams: List<Team>)
    fun getNextMatch(): GroupMatch?
    fun updateMatchResult(match: GroupMatch)
    fun getCurrentRound(): Int
    fun getPlayedMatchesCount(): Int
    fun hasMoreMatches(): Boolean
    fun isTournamentComplete(): Boolean
    fun getCurrentGroupTable(): GroupTable
    fun getAllMatches(): List<GroupMatch>
    fun getPlayedMatches(): List<GroupMatch>
    fun resetTournament()
}