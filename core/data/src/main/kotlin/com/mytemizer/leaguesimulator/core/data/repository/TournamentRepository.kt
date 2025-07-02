package com.mytemizer.leaguesimulator.core.data.repository

import com.mytemizer.leaguesimulator.core.common.util.Constants
import com.mytemizer.leaguesimulator.core.domain.model.TournamentState
import com.mytemizer.leaguesimulator.core.domain.model.*
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import com.mytemizer.leaguesimulator.core.domain.simulation.GroupTableCalculator
import com.mytemizer.leaguesimulator.core.domain.simulation.MatchSimulator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages the current tournament state and progression
 */
class TournamentRepositoryImpl(
    private val matchSimulator: MatchSimulator,
    private val groupTableCalculator: GroupTableCalculator
): TournamentRepository {
    private var currentTeams: List<Team> = emptyList()
    private var allMatches: List<GroupMatch> = emptyList()
    private var playedMatches: MutableList<GroupMatch> = mutableListOf()
    private var currentMatchIndex: Int = 0

    // State flow to notify about tournament changes
    private val _tournamentState = MutableStateFlow(TournamentState())
    val tournamentState: StateFlow<TournamentState> = _tournamentState.asStateFlow()


    override fun getTournamentStatus(): StateFlow<TournamentState> {
        return tournamentState
    }
    /**
     * Initialize a new tournament with teams
     */
    override fun initializeTournament(teams: List<Team>) {
        require(teams.size >= Constants.MIN_TEAMS_IN_LEAGUE) { "Tournament must have at least ${Constants.MIN_TEAMS_IN_LEAGUE} teams" }
        require(teams.size % 2 == 0) { "Tournament must have an even number of teams" }
        require(teams.size <= Constants.MAX_TEAMS_IN_LEAGUE) { "Tournament cannot have more than ${Constants.MAX_TEAMS_IN_LEAGUE} teams" }

        currentTeams = teams
        allMatches = generateAllMatches(teams)
        playedMatches.clear()
        currentMatchIndex = 0

        // Emit state change
        emitStateChange()
    }

    /**
     * Generate all matches for the tournament
     */
    private fun generateAllMatches(teams: List<Team>): List<GroupMatch> {
        val matchPairs = matchSimulator.generateGroupMatches(teams)
        val matchesPerRound = teams.size / 2
        return matchPairs.mapIndexed { index, (homeTeam, awayTeam) ->
            val round = (index / matchesPerRound) + 1
            GroupMatch(
                id = index.toLong(),
                homeTeam = homeTeam,
                awayTeam = awayTeam,
                homeGoals = 0,
                awayGoals = 0,
                isPlayed = false,
                round = round,
                groupId = 1L
            )
        }
    }

    /**
     * Get the next unplayed match
     */
    override fun getNextMatch(): GroupMatch? {
        return if (currentMatchIndex < allMatches.size) {
            allMatches[currentMatchIndex]
        } else {
            null
        }
    }

    /**
     * Update match result and advance to next match
     */
    override fun updateMatchResult(match: GroupMatch) {
        if (currentMatchIndex < allMatches.size) {
            playedMatches.add(match)
            currentMatchIndex++

            // Emit state change
            emitStateChange()
        }
    }

    /**
     * Get current round number
     */
    override fun getCurrentRound(): Int {
        return if (currentMatchIndex < allMatches.size) {
            allMatches[currentMatchIndex].round
        } else {
            // Tournament complete - return total rounds
            if (currentTeams.isNotEmpty()) currentTeams.size - 1 else 1
        }
    }

    /**
     * Get number of played matches
     */
    override fun getPlayedMatchesCount(): Int = playedMatches.size

    /**
     * Check if there are more matches to play
     */
    override fun hasMoreMatches(): Boolean = currentMatchIndex < allMatches.size

    /**
     * Check if tournament is complete
     */
    override fun isTournamentComplete(): Boolean = playedMatches.size == allMatches.size

    /**
     * Get current group table
     */
    override fun getCurrentGroupTable(): GroupTable {
        return groupTableCalculator.calculateGroupTable(
            teams = currentTeams,
            matches = playedMatches,
            groupId = 1L
        )
    }

    /**
     * Get all matches (played and unplayed)
     */
    override fun getAllMatches(): List<GroupMatch> = allMatches

    /**
     * Get played matches
     */
    override fun getPlayedMatches(): List<GroupMatch> = playedMatches.toList()

    /**
     * Reset tournament
     */
    override fun resetTournament() {
        currentTeams = emptyList()
        allMatches = emptyList()
        playedMatches.clear()
        currentMatchIndex = 0

        // Emit state change
        emitStateChange()
    }

    /**
     * Emit tournament state change
     */
    private fun emitStateChange() {
        val totalRounds = if (currentTeams.isNotEmpty()) currentTeams.size - 1 else 0
        _tournamentState.value = TournamentState(
            currentRound = getCurrentRound(),
            matchesPlayed = getPlayedMatchesCount(),
            totalMatches = allMatches.size,
            totalRounds = totalRounds,
            isComplete = isTournamentComplete(),
            hasMoreMatches = hasMoreMatches()
        )
    }
}
