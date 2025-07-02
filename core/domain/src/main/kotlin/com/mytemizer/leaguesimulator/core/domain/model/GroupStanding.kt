package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Represents a team's standing in a group
 */
data class GroupStanding(
    val team: Team,
    val position: Int = 0,
    val played: Int = 0,
    val won: Int = 0,
    val drawn: Int = 0,
    val lost: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val points: Int = 0
) {
    /**
     * Calculate goal difference
     */
    val goalDifference: Int get() = goalsFor - goalsAgainst

    /**
     * Create updated standing with new match result
     */
    fun withMatchResult(
        result: MatchResult,
        goalsScored: Int,
        goalsConceded: Int
    ): GroupStanding {
        return when (result) {
            MatchResult.WIN -> copy(
                played = played + 1,
                won = won + 1,
                goalsFor = goalsFor + goalsScored,
                goalsAgainst = goalsAgainst + goalsConceded,
                points = points + 3
            )
            MatchResult.DRAW -> copy(
                played = played + 1,
                drawn = drawn + 1,
                goalsFor = goalsFor + goalsScored,
                goalsAgainst = goalsAgainst + goalsConceded,
                points = points + 1
            )
            MatchResult.LOSS -> copy(
                played = played + 1,
                lost = lost + 1,
                goalsFor = goalsFor + goalsScored,
                goalsAgainst = goalsAgainst + goalsConceded
            )
        }
    }
}

/**
 * Represents the complete group table
 */
data class GroupTable(
    val groupId: Long,
    val standings: List<GroupStanding>,
    val matches: List<GroupMatch>,
    val isComplete: Boolean = false
) {
    /**
     * Get total goals scored in the group
     */
    val totalGoals: Int get() = matches.filter { it.isPlayed }.sumOf { it.homeGoals + it.awayGoals }

    /**
     * Get average goals per match
     */
    val averageGoalsPerMatch: Double get() {
        val playedMatches = matches.count { it.isPlayed }
        return if (playedMatches > 0) totalGoals.toDouble() / playedMatches else 0.0
    }

}

enum class MatchResult {
    WIN,
    DRAW,
    LOSS
}
