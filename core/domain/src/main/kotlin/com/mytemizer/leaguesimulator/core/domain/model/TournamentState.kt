package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Data class representing the current tournament state
 */
data class TournamentState(
    val currentRound: Int = 1,
    val matchesPlayed: Int = 0,
    val totalMatches: Int = 6,
    val isComplete: Boolean = false,
    val hasMoreMatches: Boolean = true
)