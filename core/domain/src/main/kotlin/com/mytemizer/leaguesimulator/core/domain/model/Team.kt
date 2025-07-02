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
)