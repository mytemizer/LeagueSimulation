package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Represents a match in a group stage simulation
 */
data class GroupMatch(
    val id: Long = 0,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeGoals: Int = 0,
    val awayGoals: Int = 0,
    val isPlayed: Boolean = false,
    val round: Int = 1,
    val groupId: Long = 0,
    val playedAt: Long = System.currentTimeMillis()
) {
    /**
     * Get the winner of the match
     */
    fun getWinner(): Team? = when {
        homeGoals > awayGoals -> homeTeam
        awayGoals > homeGoals -> awayTeam
        else -> null // Draw
    }

    /**
     * Check if the match is a draw
     */
    fun isDraw(): Boolean = homeGoals == awayGoals && isPlayed
    
    /**
     * Get points for home team (3 for win, 1 for draw, 0 for loss)
     */
    fun getHomeTeamPoints(): Int = when {
        !isPlayed -> 0
        homeGoals > awayGoals -> 3
        homeGoals == awayGoals -> 1
        else -> 0
    }
    
    /**
     * Get points for away team (3 for win, 1 for draw, 0 for loss)
     */
    fun getAwayTeamPoints(): Int = when {
        !isPlayed -> 0
        awayGoals > homeGoals -> 3
        homeGoals == awayGoals -> 1
        else -> 0
    }

    /**
     * Get match result as string
     */
    fun getResultString(): String = if (isPlayed) "$homeGoals - $awayGoals" else "vs"
}
