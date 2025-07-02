package com.mytemizer.leaguesimulator.core.domain.simulation

import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.Team
import kotlin.math.*
import kotlin.random.Random

/**
 * Simulates football matches based on team strength
 */
class MatchSimulator {
    
    /**
     * Simulate a match between two teams
     */
    fun simulateMatch(
        homeTeam: Team,
        awayTeam: Team,
        round: Int = 1,
        groupId: Long = 0,
        random: Random = Random.Default
    ): GroupMatch {
        // Calculate team strengths
        val homeStrength = calculateTeamStrength(homeTeam, isHome = true)
        val awayStrength = calculateTeamStrength(awayTeam, isHome = false)
        
        // Simulate goals for each team
        val homeGoals = simulateGoals(homeStrength, awayTeam.defense, random)
        val awayGoals = simulateGoals(awayStrength, homeTeam.defense, random)
        
        return GroupMatch(
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            isPlayed = true,
            round = round,
            groupId = groupId,
            playedAt = System.currentTimeMillis()
        )
    }
    
    /**
     * Calculate effective team strength for match simulation
     */
    private fun calculateTeamStrength(team: Team, isHome: Boolean): Double {
        // Base strength from team ratings
        val baseStrength = (team.attack * 0.4 + team.midfield * 0.3 + team.defense * 0.2 + team.goalkeeper * 0.1)
        
        // Home advantage (typically 3-5 points boost)
        val homeAdvantage = if (isHome) 3.0 else 0.0
        
        // Add some randomness to make matches less predictable
        val randomFactor = Random.nextDouble(-2.0, 2.0)
        
        return (baseStrength + homeAdvantage + randomFactor).coerceIn(30.0, 100.0)
    }
    
    /**
     * Simulate goals scored by a team
     */
    private fun simulateGoals(attackStrength: Double, defenseRating: Int, random: Random): Int {
        // Calculate expected goals based on attack vs defense
        val attackDefenseRatio = attackStrength / defenseRating.coerceAtLeast(30)
        
        // Base expected goals (typically 0.5 to 3.5 per team)
        val expectedGoals = when {
            attackDefenseRatio >= 1.5 -> 2.5 + (attackDefenseRatio - 1.5) * 0.5
            attackDefenseRatio >= 1.2 -> 2.0 + (attackDefenseRatio - 1.2) * 1.67
            attackDefenseRatio >= 1.0 -> 1.5 + (attackDefenseRatio - 1.0) * 2.5
            attackDefenseRatio >= 0.8 -> 1.0 + (attackDefenseRatio - 0.8) * 2.5
            else -> 0.5 + attackDefenseRatio * 0.625
        }.coerceIn(0.3, 4.0)
        
        // Use Poisson distribution for realistic goal distribution
        return generatePoissonRandom(expectedGoals, random)
    }
    
    /**
     * Generate random number following Poisson distribution
     * This creates realistic goal distributions (most matches have 0-3 goals per team)
     */
    private fun generatePoissonRandom(lambda: Double, random: Random): Int {
        val l = exp(-lambda)
        var k = 0
        var p = 1.0
        
        do {
            k++
            p *= random.nextDouble()
        } while (p > l)
        
        return (k - 1).coerceAtMost(8) // Cap at 8 goals to avoid unrealistic scores
    }
    
    /**
     * Simulate multiple matches
     */
    fun simulateMatches(matches: List<Pair<Team, Team>>, groupId: Long = 0): List<GroupMatch> {
        return matches.mapIndexed { index, (homeTeam, awayTeam) ->
            val round = (index / 2) + 1 // 2 matches per round for 4 teams
            simulateMatch(homeTeam, awayTeam, round, groupId)
        }
    }
    
    /**
     * Generate all possible matches for a group of 4 teams (round-robin)
     */
    fun generateGroupMatches(teams: List<Team>): List<Pair<Team, Team>> {
        require(teams.size == 4) { "Group must have exactly 4 teams" }
        
        val matches = mutableListOf<Pair<Team, Team>>()
        
        // Round 1: Team 1 vs Team 2, Team 3 vs Team 4
        matches.add(teams[0] to teams[1])
        matches.add(teams[2] to teams[3])
        
        // Round 2: Team 1 vs Team 3, Team 2 vs Team 4
        matches.add(teams[0] to teams[2])
        matches.add(teams[1] to teams[3])
        
        // Round 3: Team 1 vs Team 4, Team 2 vs Team 3
        matches.add(teams[0] to teams[3])
        matches.add(teams[1] to teams[2])
        
        return matches
    }
    
    /**
     * Simulate an entire group stage
     */
    fun simulateGroup(teams: List<Team>, groupId: Long = 0): List<GroupMatch> {
        val matchPairs = generateGroupMatches(teams)
        return simulateMatches(matchPairs, groupId)
    }
}
