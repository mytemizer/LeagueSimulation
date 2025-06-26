package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Domain model representing a league table entry
 */
data class LeagueTableEntry(
    val teamId: Long,
    val position: Int,
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
    val goalDifference: Int
        get() = goalsFor - goalsAgainst
    
    /**
     * Calculate points per game
     */
    val pointsPerGame: Double
        get() = if (played > 0) points.toDouble() / played else 0.0
    
    /**
     * Calculate win percentage
     */
    val winPercentage: Double
        get() = if (played > 0) (won.toDouble() / played) * 100 else 0.0
    
    /**
     * Get form based on recent results
     */
    fun getForm(): String {
        // This would typically be calculated from recent match results
        // For now, return a placeholder
        return when {
            pointsPerGame >= 2.5 -> "Excellent"
            pointsPerGame >= 2.0 -> "Good"
            pointsPerGame >= 1.5 -> "Average"
            pointsPerGame >= 1.0 -> "Poor"
            else -> "Very Poor"
        }
    }
}

/**
 * Complete league table with all teams
 */
data class LeagueTable(
    val leagueId: Long,
    val season: Int,
    val entries: List<LeagueTableEntry>,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    /**
     * Get table sorted by position
     */
    fun getSortedTable(): List<LeagueTableEntry> {
        return entries.sortedWith(
            compareByDescending<LeagueTableEntry> { it.points }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
                .thenBy { it.teamId } // For consistent ordering when all else is equal
        ).mapIndexed { index, entry ->
            entry.copy(position = index + 1)
        }
    }
    
    /**
     * Get teams in relegation zone (bottom 3)
     */
    fun getRelegationZone(): List<LeagueTableEntry> {
        val sorted = getSortedTable()
        return sorted.takeLast(1)
    }

    /**
     * Get teams in European competition spots (top 6)
     */
    fun getEuropeanSpots(): List<LeagueTableEntry> {
        val sorted = getSortedTable()
        return sorted.take(2)
    }

    /**
     * Get champion (1st place)
     */
    fun getChampion(): LeagueTableEntry? {
        return getSortedTable().firstOrNull()
    }
}
