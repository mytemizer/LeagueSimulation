package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Domain model representing a football team
 */
data class Team(
    val id: Long = 0,
    val name: String,
    val shortName: String,
    val logoUrl: String? = null,
    val foundedYear: Int? = null,
    val stadium: String? = null,
    val city: String? = null,
    val country: String? = null,
    val primaryColor: String? = null,
    val secondaryColor: String? = null,
    val overallRating: Int = 50, // 0-100 scale
    val attack: Int = 50,
    val midfield: Int = 50,
    val defense: Int = 50,
    val goalkeeper: Int = 50,
    val isUserTeam: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Calculate team strength based on individual ratings
     */
    fun calculateTeamStrength(): Double {
        return (attack * 0.3 + midfield * 0.25 + defense * 0.25 + goalkeeper * 0.2)
    }
    
    /**
     * Get team rating category
     */
    fun getRatingCategory(): TeamRating {
        return when (overallRating) {
            in 90..100 -> TeamRating.WORLD_CLASS
            in 80..89 -> TeamRating.EXCELLENT
            in 70..79 -> TeamRating.GOOD
            in 60..69 -> TeamRating.AVERAGE
            in 50..59 -> TeamRating.BELOW_AVERAGE
            else -> TeamRating.POOR
        }
    }
}

enum class TeamRating(val displayName: String) {
    WORLD_CLASS("World Class"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    AVERAGE("Average"),
    BELOW_AVERAGE("Below Average"),
    POOR("Poor")
}
