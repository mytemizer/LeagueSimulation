package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Qualification status for knockout stage
 */
enum class QualificationStatus(val displayName: String, val description: String) {
    QUALIFIED_FIRST("1st Place", "Qualified for Round of 16"),
    QUALIFIED_SECOND("2nd Place", "Qualified for Round of 16"),
    ELIMINATED_THIRD("3rd Place", "Eliminated from tournament"),
    ELIMINATED_FOURTH("4th Place", "Eliminated from tournament")
}
