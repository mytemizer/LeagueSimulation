package com.mytemizer.leaguesimulator.core.domain.simulation

import com.mytemizer.leaguesimulator.core.domain.model.Team
import org.junit.Test
import org.junit.Assert.*

class HomeAwayBalanceTest {

    private val simulator = MatchSimulator()

    @Test
    fun `demonstrate balanced home away distribution`() {
        println("\n=== BALANCED HOME/AWAY DISTRIBUTION DEMO ===")
        analyzeDistribution()
    }

    private fun analyzeDistribution() {
        // Create test teams
        val teams = listOf(
            Team(id = 1, name = "Team A", shortName = "TEA", overallRating = 80),
            Team(id = 2, name = "Team B", shortName = "TEB", overallRating = 80),
            Team(id = 3, name = "Team C", shortName = "TEC", overallRating = 80),
            Team(id = 4, name = "Team D", shortName = "TED", overallRating = 80)
        )

        // Generate matches
        val matchPairs = simulator.generateGroupMatches(teams)

        // Count home and away games for each team
        val homeGames = mutableMapOf<String, Int>()
        val awayGames = mutableMapOf<String, Int>()

        teams.forEach { team ->
            homeGames[team.name] = 0
            awayGames[team.name] = 0
        }

        matchPairs.forEach { (homeTeam, awayTeam) ->
            homeGames[homeTeam.name] = homeGames[homeTeam.name]!! + 1
            awayGames[awayTeam.name] = awayGames[awayTeam.name]!! + 1
        }

        // Print current distribution to understand the problem
        println("=== CURRENT DISTRIBUTION ===")
        teams.forEach { team ->
            val home = homeGames[team.name]!!
            val away = awayGames[team.name]!!
            println("${team.name}: $home home, $away away")
        }

        // Check if distribution is balanced (for 4 teams, each should have 1-2 home and 1-2 away)
        teams.forEach { team ->
            val home = homeGames[team.name]!!
            val away = awayGames[team.name]!!
            val homeAwayDiff = kotlin.math.abs(home - away)

            // For 4 teams: each plays 3 games total, so should be 1-2 or 2-1 split
            assertTrue("${team.name} should have balanced home/away games, but has $home home and $away away",
                homeAwayDiff <= 1)
        }

        // Verify total matches is correct
        val expectedMatches = teams.size * (teams.size - 1) / 2
        assertEquals("Should have $expectedMatches total matches", expectedMatches, matchPairs.size)

        // Verify round structure - no team should play twice in the same round
        verifyRoundStructure(teams, matchPairs)
    }

    private fun verifyRoundStructure(teams: List<Team>, matchPairs: List<Pair<Team, Team>>) {
        val matchesPerRound = teams.size / 2
        val totalRounds = teams.size - 1

        for (round in 0 until totalRounds) {
            val roundStart = round * matchesPerRound
            val roundEnd = roundStart + matchesPerRound
            val roundMatches = matchPairs.subList(roundStart, roundEnd.coerceAtMost(matchPairs.size))

            // Collect all teams playing in this round
            val teamsInRound = mutableSetOf<Team>()
            roundMatches.forEach { (home, away) ->
                teamsInRound.add(home)
                teamsInRound.add(away)
            }

            // Each team should appear exactly once in each round
            assertEquals("Round ${round + 1} should have ${teams.size} teams playing", teams.size, teamsInRound.size)

            // Verify no team appears twice
            teams.forEach { team ->
                val appearances = roundMatches.count { (home, away) -> home == team || away == team }
                assertEquals("Team ${team.name} should appear exactly once in round ${round + 1}", 1, appearances)
            }
        }
    }

    @Test
    fun `test with 6 teams`() {
        val teams = listOf(
            Team(id = 1, name = "Team A", shortName = "TEA", overallRating = 80),
            Team(id = 2, name = "Team B", shortName = "TEB", overallRating = 80),
            Team(id = 3, name = "Team C", shortName = "TEC", overallRating = 80),
            Team(id = 4, name = "Team D", shortName = "TED", overallRating = 80),
            Team(id = 5, name = "Team E", shortName = "TEE", overallRating = 80),
            Team(id = 6, name = "Team F", shortName = "TEF", overallRating = 80)
        )

        val matchPairs = simulator.generateGroupMatches(teams)

        val homeGames = mutableMapOf<String, Int>()
        val awayGames = mutableMapOf<String, Int>()

        teams.forEach { team ->
            homeGames[team.name] = 0
            awayGames[team.name] = 0
        }

        matchPairs.forEach { (homeTeam, awayTeam) ->
            homeGames[homeTeam.name] = homeGames[homeTeam.name]!! + 1
            awayGames[awayTeam.name] = awayGames[awayTeam.name]!! + 1
        }

        println("\n=== 6 TEAMS HOME/AWAY DISTRIBUTION ===")
        teams.forEach { team ->
            val home = homeGames[team.name]!!
            val away = awayGames[team.name]!!
            println("${team.name}: $home home, $away away")
        }

        // For 6 teams: each plays 5 games total, so should be 2-3 or 3-2 split
        teams.forEach { team ->
            val home = homeGames[team.name]!!
            val away = awayGames[team.name]!!
            val homeAwayDiff = kotlin.math.abs(home - away)

            assertTrue("${team.name} should have balanced home/away games, but has $home home and $away away",
                homeAwayDiff <= 1)
        }

        // Verify total matches is correct
        val expectedMatches = teams.size * (teams.size - 1) / 2
        assertEquals("Should have $expectedMatches total matches", expectedMatches, matchPairs.size)

        // Verify round structure
        verifyRoundStructure(teams, matchPairs)
    }

    @Test
    fun `test with 8 teams`() {
        val teams = (1..8).map {
            Team(id = it.toLong(), name = "Team $it", shortName = "T$it", overallRating = 80)
        }

        val matchPairs = simulator.generateGroupMatches(teams)

        val homeGames = mutableMapOf<String, Int>()
        val awayGames = mutableMapOf<String, Int>()

        teams.forEach { team ->
            homeGames[team.name] = 0
            awayGames[team.name] = 0
        }

        matchPairs.forEach { (homeTeam, awayTeam) ->
            homeGames[homeTeam.name] = homeGames[homeTeam.name]!! + 1
            awayGames[awayTeam.name] = awayGames[awayTeam.name]!! + 1
        }

        println("\n=== 8 TEAMS HOME/AWAY DISTRIBUTION ===")
        teams.forEach { team ->
            val home = homeGames[team.name]!!
            val away = awayGames[team.name]!!
            println("${team.name}: $home home, $away away")
        }

        // For 8 teams: each plays 7 games total, so should be 3-4 or 4-3 split
        teams.forEach { team ->
            val home = homeGames[team.name]!!
            val away = awayGames[team.name]!!
            val homeAwayDiff = kotlin.math.abs(home - away)

            assertTrue("${team.name} should have balanced home/away games, but has $home home and $away away",
                homeAwayDiff <= 1)
        }

        // Verify total matches is correct
        val expectedMatches = teams.size * (teams.size - 1) / 2
        assertEquals("Should have $expectedMatches total matches", expectedMatches, matchPairs.size)

        // Verify round structure
        verifyRoundStructure(teams, matchPairs)
    }
}
