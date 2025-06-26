package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Domain model representing a football league
 */
data class League(
    val id: Long = 0,
    val name: String,
    val shortName: String,
    val country: String,
    val level: Int = 1, // 1 = top division, 2 = second division, etc.
    val numberOfTeams: Int,
    val seasonLength: Int, // number of matchdays
    val currentSeason: Int,
    val logoUrl: String? = null,
    val description: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calculate total number of matches in a season
     * Each team plays every other team twice (home and away)
     */
    fun getTotalMatches(): Int {
        return numberOfTeams * (numberOfTeams - 1)
    }
    
    /**
     * Calculate matches per matchday
     */
    fun getMatchesPerMatchday(): Int {
        return numberOfTeams / 2
    }
    
    /**
     * Validate if the league configuration is valid
     */
    fun isValidConfiguration(): Boolean {
        return numberOfTeams >= 4 && 
               numberOfTeams <= 20 && 
               numberOfTeams % 2 == 0 && // Must be even number for proper scheduling
               seasonLength > 0
    }
}
