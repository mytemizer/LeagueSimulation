package com.mytemizer.leaguesimulator.core.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a football match
 */
data class Match(
    val id: Long = 0,
    val leagueId: Long,
    val homeTeamId: Long,
    val awayTeamId: Long,
    val matchday: Int,
    val season: Int,
    val scheduledDateTime: LocalDateTime,
    val homeScore: Int? = null,
    val awayScore: Int? = null,
    val status: MatchStatus = MatchStatus.SCHEDULED,
    val homeTeamStats: MatchStats? = null,
    val awayTeamStats: MatchStats? = null,
    val events: List<MatchEvent> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if match has been played
     */
    fun isPlayed(): Boolean = status == MatchStatus.FINISHED
    
    /**
     * Get match result for home team perspective
     */
    fun getHomeTeamResult(): MatchResult? {
        if (!isPlayed() || homeScore == null || awayScore == null) return null
        
        return when {
            homeScore > awayScore -> MatchResult.WIN
            homeScore < awayScore -> MatchResult.LOSS
            else -> MatchResult.DRAW
        }
    }
    
    /**
     * Get match result for away team perspective
     */
    fun getAwayTeamResult(): MatchResult? {
        return when (getHomeTeamResult()) {
            MatchResult.WIN -> MatchResult.LOSS
            MatchResult.LOSS -> MatchResult.WIN
            MatchResult.DRAW -> MatchResult.DRAW
            null -> null
        }
    }
    
    /**
     * Get total goals scored in the match
     */
    fun getTotalGoals(): Int? {
        return if (homeScore != null && awayScore != null) {
            homeScore + awayScore
        } else null
    }
}

enum class MatchStatus {
    SCHEDULED,
    LIVE,
    FINISHED,
    POSTPONED,
    CANCELLED
}

enum class MatchResult {
    WIN,
    DRAW,
    LOSS
}

data class MatchStats(
    val possession: Int, // percentage
    val shots: Int,
    val shotsOnTarget: Int,
    val corners: Int,
    val fouls: Int,
    val yellowCards: Int,
    val redCards: Int,
    val offsides: Int
)

data class MatchEvent(
    val id: Long = 0,
    val matchId: Long,
    val teamId: Long,
    val playerId: Long? = null,
    val minute: Int,
    val type: MatchEventType,
    val description: String? = null
)

enum class MatchEventType {
    GOAL,
    YELLOW_CARD,
    RED_CARD,
    SUBSTITUTION,
    PENALTY,
    OWN_GOAL,
    ASSIST
}
