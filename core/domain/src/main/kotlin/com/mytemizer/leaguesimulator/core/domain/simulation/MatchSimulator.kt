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

        // Quality bonus for stronger teams (amplifies differences)
        val qualityBonus = when {
            team.overallRating >= 85 -> 2.0 // World class teams get extra boost
            team.overallRating >= 75 -> 1.0 // Excellent teams get small boost
            else -> 0.0
        }

        // Reduced randomness to make matches more predictable based on strength
        val randomFactor = Random.nextDouble(-1.0, 1.0)

        return (baseStrength + homeAdvantage + qualityBonus + randomFactor).coerceIn(30.0, 100.0)
    }

    /**
     * Simulate goals scored by a team
     */
    private fun simulateGoals(attackStrength: Double, defenseRating: Int, random: Random): Int {
        // Calculate expected goals based on attack vs defense with more aggressive scaling
        val attackDefenseRatio = attackStrength / defenseRating.coerceAtLeast(30)

        // More aggressive expected goals calculation that favors stronger teams
        val expectedGoals = when {
            attackDefenseRatio >= 1.6 -> 3.0 + (attackDefenseRatio - 1.6) * 0.8 // Dominant teams score more
            attackDefenseRatio >= 1.4 -> 2.5 + (attackDefenseRatio - 1.4) * 2.5
            attackDefenseRatio >= 1.2 -> 2.0 + (attackDefenseRatio - 1.2) * 2.5
            attackDefenseRatio >= 1.0 -> 1.5 + (attackDefenseRatio - 1.0) * 2.5
            attackDefenseRatio >= 0.8 -> 1.0 + (attackDefenseRatio - 0.8) * 2.5
            attackDefenseRatio >= 0.6 -> 0.6 + (attackDefenseRatio - 0.6) * 2.0
            else -> 0.3 + attackDefenseRatio * 0.5 // Weaker teams struggle more
        }.coerceIn(0.2, 5.0) // Increased upper limit for dominant performances

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

        return (k - 1).coerceAtMost(10) // Increased cap to allow dominant performances
    }

    /**
     * Simulate multiple matches
     */
    fun simulateMatches(matches: List<Pair<Team, Team>>, groupId: Long = 0, teamsCount: Int = 4): List<GroupMatch> {
        val matchesPerRound = teamsCount / 2

        return matches.mapIndexed { index, (homeTeam, awayTeam) ->
            val round = (index / matchesPerRound) + 1
            simulateMatch(homeTeam, awayTeam, round, groupId)
        }
    }

    /**
     * Generate all possible matches for a group (round-robin)
     * Supports any even number of teams >= 4
     * Uses balanced home/away distribution
     */
    fun generateGroupMatches(teams: List<Team>): List<Pair<Team, Team>> {
        require(teams.size >= 4) { "Group must have at least 4 teams" }
        require(teams.size % 2 == 0) { "Group must have an even number of teams" }

        return generateBalancedMatches(teams)
    }

    /**
     * Generate matches with balanced home/away distribution
     * Organizes matches into proper rounds where each team plays exactly once per round
     */
    private fun generateBalancedMatches(teams: List<Team>): List<Pair<Team, Team>> {
        val n = teams.size
        val rounds = n - 1
        val matchesPerRound = n / 2
        val matches = mutableListOf<Pair<Team, Team>>()
        val homeCount = IntArray(n) { 0 }
        val awayCount = IntArray(n) { 0 }

        // Generate rounds using circle method but with balanced home/away assignment
        for (round in 0 until rounds) {
            val roundMatches = mutableListOf<Pair<Int, Int>>()

            // Generate pairings for this round using circle method
            for (match in 0 until matchesPerRound) {
                val team1Index: Int
                val team2Index: Int

                if (match == 0) {
                    // First match: fixed team vs rotating team
                    team1Index = n - 1 // Fixed team (last team)
                    team2Index = round % (n - 1)
                } else {
                    // Other matches: pair remaining teams
                    val pos1 = (round + match) % (n - 1)
                    val pos2 = (round + n - 1 - match) % (n - 1)
                    team1Index = pos1
                    team2Index = pos2
                }

                if (team1Index != team2Index) {
                    roundMatches.add(team1Index to team2Index)
                }
            }

            // For each pairing in this round, decide home/away to balance distribution
            roundMatches.forEach { (i, j) ->
                val team1 = teams[i]
                val team2 = teams[j]

                // Decide who plays at home based on current balance
                val team1HomeDiff = homeCount[i] - awayCount[i]
                val team2HomeDiff = homeCount[j] - awayCount[j]

                if (team1HomeDiff < team2HomeDiff) {
                    // Team 1 needs more home games
                    matches.add(team1 to team2)
                    homeCount[i]++
                    awayCount[j]++
                } else if (team2HomeDiff < team1HomeDiff) {
                    // Team 2 needs more home games
                    matches.add(team2 to team1)
                    homeCount[j]++
                    awayCount[i]++
                } else {
                    // Equal balance, use team index as tiebreaker for consistency
                    if (i < j) {
                        matches.add(team1 to team2)
                        homeCount[i]++
                        awayCount[j]++
                    } else {
                        matches.add(team2 to team1)
                        homeCount[j]++
                        awayCount[i]++
                    }
                }
            }
        }

        return matches
    }

    /**
     * Generate matches using the original circle method (for reference)
     * This method may produce unbalanced home/away distribution
     */
    private fun generateCircleMethodMatches(teams: List<Team>): List<Pair<Team, Team>> {
        val matches = mutableListOf<Pair<Team, Team>>()
        val n = teams.size
        val rounds = n - 1

        // Circle method for round-robin tournament
        // Fix one team (last team) and rotate others
        for (round in 0 until rounds) {
            for (match in 0 until n / 2) {
                val team1Index: Int
                val team2Index: Int

                if (match == 0) {
                    // First match: fixed team vs rotating team
                    team1Index = n - 1 // Fixed team (last team)
                    team2Index = round % (n - 1)
                } else {
                    // Other matches: pair remaining teams
                    val pos1 = (round + match) % (n - 1)
                    val pos2 = (round + n - 1 - match) % (n - 1)
                    team1Index = pos1
                    team2Index = pos2
                }

                if (team1Index != team2Index) {
                    matches.add(teams[team1Index] to teams[team2Index])
                }
            }
        }

        return matches
    }

    /**
     * Simulate an entire group stage
     */
    fun simulateGroup(teams: List<Team>, groupId: Long = 0): List<GroupMatch> {
        val matchPairs = generateGroupMatches(teams)
        return simulateMatches(matchPairs, groupId, teams.size)
    }
}
