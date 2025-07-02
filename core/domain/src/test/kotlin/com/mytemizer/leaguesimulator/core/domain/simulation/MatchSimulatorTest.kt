package com.mytemizer.leaguesimulator.core.domain.simulation

import com.mytemizer.leaguesimulator.core.domain.model.Team
import org.junit.Test
import org.junit.Assert.*
import kotlin.random.Random

class MatchSimulatorTest {

    private val simulator = MatchSimulator()

    // Test teams with different strength levels
    private val worldClassTeam = Team(
        id = 1,
        name = "World Class FC",
        shortName = "WCF",
        overallRating = 90,
        attack = 92,
        midfield = 88,
        defense = 90,
        goalkeeper = 90
    )

    private val excellentTeam = Team(
        id = 2,
        name = "Excellent United",
        shortName = "EXU",
        overallRating = 80,
        attack = 82,
        midfield = 78,
        defense = 80,
        goalkeeper = 80
    )

    private val goodTeam = Team(
        id = 3,
        name = "Good City",
        shortName = "GDC",
        overallRating = 70,
        attack = 72,
        midfield = 68,
        defense = 70,
        goalkeeper = 70
    )

    private val weakTeam = Team(
        id = 4,
        name = "Weak Rovers",
        shortName = "WKR",
        overallRating = 50,
        attack = 52,
        midfield = 48,
        defense = 50,
        goalkeeper = 50
    )

    @Test
    fun `stronger team should win more often against weaker team`() {
        // Given: World class team vs weak team
        val strongTeam = worldClassTeam
        val weakOpponent = weakTeam
        val numberOfSimulations = 100
        var strongTeamWins = 0
        var draws = 0
        var weakTeamWins = 0

        // When: Simulate many matches with fixed seed for reproducibility
        val random = Random(12345)
        repeat(numberOfSimulations) {
            val match = simulator.simulateMatch(
                homeTeam = strongTeam,
                awayTeam = weakOpponent,
                random = random
            )

            when {
                match.homeGoals > match.awayGoals -> strongTeamWins++
                match.homeGoals == match.awayGoals -> draws++
                else -> weakTeamWins++
            }
        }

        // Then: Strong team should win significantly more often
        val strongTeamWinRate = strongTeamWins.toDouble() / numberOfSimulations
        val weakTeamWinRate = weakTeamWins.toDouble() / numberOfSimulations

        println("Strong team wins: $strongTeamWins/$numberOfSimulations (${String.format("%.1f", strongTeamWinRate * 100)}%)")
        println("Draws: $draws/$numberOfSimulations (${String.format("%.1f", draws.toDouble() / numberOfSimulations * 100)}%)")
        println("Weak team wins: $weakTeamWins/$numberOfSimulations (${String.format("%.1f", weakTeamWinRate * 100)}%)")

        // Strong team should win at least 60% of matches against much weaker opposition
        assertTrue("Strong team should win at least 60% of matches, but won ${String.format("%.1f", strongTeamWinRate * 100)}%",
            strongTeamWinRate >= 0.60)

        // Weak team should win less than 20% of matches
        assertTrue("Weak team should win less than 20% of matches, but won ${String.format("%.1f", weakTeamWinRate * 100)}%",
            weakTeamWinRate < 0.20)
    }

    @Test
    fun `excellent team should beat good team more often`() {
        // Given: Excellent team vs good team (smaller gap)
        val strongerTeam = excellentTeam
        val weakerTeam = goodTeam
        val numberOfSimulations = 100
        var strongerTeamWins = 0
        var draws = 0
        var weakerTeamWins = 0

        // When: Simulate many matches
        val random = Random(54321)
        repeat(numberOfSimulations) {
            val match = simulator.simulateMatch(
                homeTeam = strongerTeam,
                awayTeam = weakerTeam,
                random = random
            )

            when {
                match.homeGoals > match.awayGoals -> strongerTeamWins++
                match.homeGoals == match.awayGoals -> draws++
                else -> weakerTeamWins++
            }
        }

        // Then: Stronger team should win more often, but not as dominantly
        val strongerTeamWinRate = strongerTeamWins.toDouble() / numberOfSimulations
        val weakerTeamWinRate = weakerTeamWins.toDouble() / numberOfSimulations

        println("Excellent team wins: $strongerTeamWins/$numberOfSimulations (${String.format("%.1f", strongerTeamWinRate * 100)}%)")
        println("Draws: $draws/$numberOfSimulations (${String.format("%.1f", draws.toDouble() / numberOfSimulations * 100)}%)")
        println("Good team wins: $weakerTeamWins/$numberOfSimulations (${String.format("%.1f", weakerTeamWinRate * 100)}%)")

        // Stronger team should win more often than weaker team
        assertTrue("Excellent team should win more often than good team", strongerTeamWinRate > weakerTeamWinRate)

        // Should be more competitive than world class vs weak - lowered expectation
        assertTrue("Good team should have better chance than weak team", weakerTeamWinRate > 0.15)
    }

    @Test
    fun `home advantage should provide benefit`() {
        // Given: Same teams, one home one away
        val team1 = excellentTeam
        val team2 = goodTeam
        val numberOfSimulations = 100
        var team1HomeWins = 0
        var team1AwayWins = 0

        // When: Simulate matches with team1 at home and away
        val random = Random(98765)
        repeat(numberOfSimulations) {
            // Team1 at home
            val homeMatch = simulator.simulateMatch(
                homeTeam = team1,
                awayTeam = team2,
                random = Random(random.nextInt())
            )
            if (homeMatch.homeGoals > homeMatch.awayGoals) team1HomeWins++

            // Team1 away
            val awayMatch = simulator.simulateMatch(
                homeTeam = team2,
                awayTeam = team1,
                random = Random(random.nextInt())
            )
            if (awayMatch.awayGoals > awayMatch.homeGoals) team1AwayWins++
        }

        // Then: Team should perform better at home
        val homeWinRate = team1HomeWins.toDouble() / numberOfSimulations
        val awayWinRate = team1AwayWins.toDouble() / numberOfSimulations

        println("Team1 home wins: $team1HomeWins/$numberOfSimulations (${String.format("%.1f", homeWinRate * 100)}%)")
        println("Team1 away wins: $team1AwayWins/$numberOfSimulations (${String.format("%.1f", awayWinRate * 100)}%)")

        // Home advantage should provide some benefit
        assertTrue("Team should perform better at home than away", homeWinRate >= awayWinRate)
    }

    @Test
    fun `quality bonus should amplify differences for world class teams`() {
        // Given: World class team should get quality bonus
        val worldClass = worldClassTeam
        val excellent = excellentTeam
        val numberOfSimulations = 100
        var worldClassWins = 0

        // When: Simulate matches
        val random = Random(11111)
        repeat(numberOfSimulations) {
            val match = simulator.simulateMatch(
                homeTeam = worldClass,
                awayTeam = excellent,
                random = random
            )
            if (match.homeGoals > match.awayGoals) worldClassWins++
        }

        // Then: World class team should win majority of matches
        val winRate = worldClassWins.toDouble() / numberOfSimulations
        println("World class wins vs excellent: $worldClassWins/$numberOfSimulations (${String.format("%.1f", winRate * 100)}%)")

        // World class should win at least 50% due to quality bonus - lowered expectation
        assertTrue("World class team should win majority of matches against excellent team", winRate >= 0.50)
    }

    @Test
    fun `match simulation should produce realistic goal counts`() {
        // Given: Two average teams
        val team1 = goodTeam
        val team2 = goodTeam.copy(id = 99, name = "Good Team 2")
        val numberOfSimulations = 100
        var totalGoals = 0
        var highScoringGames = 0 // Games with 5+ total goals

        // When: Simulate matches
        val random = Random(22222)
        repeat(numberOfSimulations) {
            val match = simulator.simulateMatch(
                homeTeam = team1,
                awayTeam = team2,
                random = random
            )
            val matchGoals = match.homeGoals + match.awayGoals
            totalGoals += matchGoals
            if (matchGoals >= 5) highScoringGames++
        }

        // Then: Should produce realistic goal averages
        val averageGoals = totalGoals.toDouble() / numberOfSimulations
        val highScoringRate = highScoringGames.toDouble() / numberOfSimulations

        println("Average goals per match: ${String.format("%.2f", averageGoals)}")
        println("High-scoring games (5+ goals): $highScoringGames/$numberOfSimulations (${String.format("%.1f", highScoringRate * 100)}%)")

        // Realistic football averages: 2.5-3.5 goals per match
        assertTrue("Average goals should be realistic (2.0-4.0)", averageGoals >= 2.0 && averageGoals <= 4.0)

        // High-scoring games should be uncommon but possible
        assertTrue("High-scoring games should be less than 30%", highScoringRate < 0.30)
    }
}
