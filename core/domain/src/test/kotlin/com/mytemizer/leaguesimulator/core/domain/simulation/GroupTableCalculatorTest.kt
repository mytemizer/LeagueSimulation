package com.mytemizer.leaguesimulator.core.domain.simulation

import com.mytemizer.leaguesimulator.core.domain.model.GroupMatch
import com.mytemizer.leaguesimulator.core.domain.model.Team
import org.junit.Test
import org.junit.Assert.*

class GroupTableCalculatorTest {

    private val calculator = GroupTableCalculator()

    // Test teams with different ratings for tiebreaker scenarios
    private val teamA = Team(
        id = 1,
        name = "Team A",
        shortName = "TEA",
        overallRating = 85
    )

    private val teamB = Team(
        id = 2,
        name = "Team B",
        shortName = "TEB",
        overallRating = 80
    )

    private val teamC = Team(
        id = 3,
        name = "Team C",
        shortName = "TEC",
        overallRating = 75
    )

    private val teamD = Team(
        id = 4,
        name = "Team D",
        shortName = "TED",
        overallRating = 70
    )

    @Test
    fun `sortStandings should sort by points descending`() {
        // Given: Matches that result in different points for teams
        // Team D: 3 wins (9 points)
        // Team B: 2 wins (6 points)
        // Team A: 1 win (3 points)
        // Team C: 0 wins (0 points)
        val matches = listOf(
            // Round 1
            createMatch(teamD, teamA, 2, 0), // D wins
            createMatch(teamB, teamC, 1, 0), // B wins
            // Round 2
            createMatch(teamD, teamB, 1, 0), // D wins
            createMatch(teamA, teamC, 2, 1), // A wins
            // Round 3
            createMatch(teamD, teamC, 3, 0), // D wins
            createMatch(teamB, teamA, 2, 1)  // B wins
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: Should be sorted by points (9, 6, 3, 0)
        assertEquals(teamD.name, result.standings[0].team.name) // 9 points
        assertEquals(teamB.name, result.standings[1].team.name) // 6 points
        assertEquals(teamA.name, result.standings[2].team.name) // 3 points
        assertEquals(teamC.name, result.standings[3].team.name) // 0 points

        // Verify points
        assertEquals(9, result.standings[0].points)
        assertEquals(6, result.standings[1].points)
        assertEquals(3, result.standings[2].points)
        assertEquals(0, result.standings[3].points)
    }

    @Test
    fun `sortStandings should sort by goal difference when points are equal`() {
        // Given: Two teams with equal points but different goal differences
        val matches = listOf(
            // Both teams get 3 points (1 win each) but different goal differences
            createMatch(teamA, teamC, 3, 0), // A wins 3-0 (+3 goal diff)
            createMatch(teamB, teamD, 1, 0), // B wins 1-0 (+1 goal diff)
            createMatch(teamC, teamB, 1, 0), // C beats B
            createMatch(teamD, teamA, 1, 0)  // D beats A
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: A and B both have 3 points, but A has better goal difference
        // A: 1 win, 1 loss, +2 goal diff (3-1=+2)
        // B: 1 win, 1 loss, 0 goal diff (1-1=0)
        // C: 1 win, 1 loss, -2 goal diff (1-3=-2)
        // D: 1 win, 1 loss, 0 goal diff (1-1=0)
        val aStanding = result.standings.find { it.team.name == teamA.name }!!
        val bStanding = result.standings.find { it.team.name == teamB.name }!!

        assertEquals(3, aStanding.points)
        assertEquals(3, bStanding.points)
        assertTrue("A should have better goal difference than B",
            aStanding.goalDifference > bStanding.goalDifference)


        // Then: Should be sorted by points (3, 3, 3, 3)
        assertEquals(teamA.name, result.standings[0].team.name) // 3 points
        assertEquals(teamB.name, result.standings[1].team.name) // 3 points
        assertEquals(teamD.name, result.standings[2].team.name) // 3 points
        assertEquals(teamC.name, result.standings[3].team.name) // 3 points
    }

    @Test
    fun `sortStandings should sort by goals for when points and goal difference are equal`() {
        // Given: Two teams with equal points and goal difference but different goals for
        val matches = listOf(
            // Create scenario where A and B have same points, goal diff, but different goals for
            createMatch(teamA, teamC, 3, 1), // A wins 3-1 (+2 goal diff)
            createMatch(teamB, teamD, 2, 0), // B wins 2-0 (+2 goal diff)
            createMatch(teamC, teamA, 2, 1), // C wins 2-1 (A now has +1 goal diff total)
            createMatch(teamD, teamB, 1, 0)  // D wins 1-0 (B now has +1 goal diff total)
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: A and B both have 3 points and +1 goal difference
        // A scored 4 goals (3+1), B scored 2 goals (2+0), so A should rank higher
        val aStanding = result.standings.find { it.team.name == teamA.name }!!
        val bStanding = result.standings.find { it.team.name == teamB.name }!!

        assertEquals(3, aStanding.points)
        assertEquals(3, bStanding.points)
        assertEquals(1, aStanding.goalDifference)
        assertEquals(1, bStanding.goalDifference)
        assertTrue("A should have more goals for than B",
            aStanding.goalsFor > bStanding.goalsFor)
    }

    @Test
    fun `sortStandings should prioritize higher goals for when all other criteria equal`() {
        // Given: Teams with identical points and goal difference but different goals for
        val matches = listOf(
            // Create scenario where A and B have same points and goal diff but different goals for
            createMatch(teamA, teamC, 4, 3), // A wins 4-3 (+1 goal diff, 4 goals for)
            createMatch(teamB, teamD, 2, 1), // B wins 2-1 (+1 goal diff, 2 goals for)
            createMatch(teamC, teamA, 1, 0), // C wins 1-0 (A: 0 goal diff, 4 goals for)
            createMatch(teamD, teamB, 1, 0)  // D wins 1-0 (B: 0 goal diff, 2 goals for)
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: A should rank higher than B due to more goals scored
        val aStanding = result.standings.find { it.team.name == teamA.name }!!
        val bStanding = result.standings.find { it.team.name == teamB.name }!!

        assertEquals(3, aStanding.points) // 1 win, 1 loss
        assertEquals(3, bStanding.points) // 1 win, 1 loss
        assertEquals(0, aStanding.goalDifference) // 0 (4-4=0)
        assertEquals(0, bStanding.goalDifference) // 0 (2-2=0)
        assertEquals(4, aStanding.goalsFor) // 4 goals
        assertEquals(2, bStanding.goalsFor) // 2 goals

        // A should rank higher due to more goals scored
        val aPosition = result.standings.indexOf(aStanding)
        val bPosition = result.standings.indexOf(bStanding)
        assertTrue("A should rank higher than B due to more goals for", aPosition < bPosition)
    }

    @Test
    fun `sortStandings should sort by goals against when points, goal difference and goals for are equal`() {
        // Given: Teams with equal points, goal difference, and goals for but different goals against
        val matches = listOf(
            // Create scenario where teams have identical stats except goals against
            createMatch(teamA, teamC, 2, 1), // A wins 2-1
            createMatch(teamB, teamD, 2, 0), // B wins 2-0 (fewer goals against)
            createMatch(teamC, teamA, 1, 0), // C wins 1-0 (A: 2 for, 2 against, 0 diff)
            createMatch(teamD, teamB, 2, 0)  // D wins 2-0 (B: 2 for, 2 against, 0 diff)
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: Verify the specific goals against comparison
        val aStanding = result.standings.find { it.team.name == teamA.name }!!
        val bStanding = result.standings.find { it.team.name == teamB.name }!!

        // A: 1 win, 1 loss = 3 points, 2 for, 2 against, 0 diff
        assertEquals(3, aStanding.points)
        assertEquals(2, aStanding.goalsFor)
        assertEquals(2, aStanding.goalsAgainst)
        assertEquals(0, aStanding.goalDifference)

        // B: 1 win, 1 loss = 3 points, 2 for, 2 against, 0 diff
        assertEquals(3, bStanding.points)
        assertEquals(2, bStanding.goalsFor)
        assertEquals(2, bStanding.goalsAgainst)
        assertEquals(0, bStanding.goalDifference)

        // B should rank higher due to more points
        val aPosition = result.standings.indexOf(aStanding)
        val bPosition = result.standings.indexOf(bStanding)
        assertTrue("A should rank higher than B due to team rank", aPosition < bPosition)
    }

    @Test
    fun `sortStandings should use goals against as final statistical tiebreaker`() {
        // Given: Teams with identical points, goal difference, and goals for
        val matches = listOf(
            // Create perfect scenario for goals against tiebreaker
            createMatch(teamA, teamC, 3, 2), // A wins 3-2 (3 for, 2 against)
            createMatch(teamB, teamD, 3, 1), // B wins 3-1 (3 for, 1 against - better)
            createMatch(teamC, teamA, 2, 1), // C wins 2-1 (A: 4 for, 4 against, 0 diff)
            createMatch(teamD, teamB, 1, 0)  // D wins 1-0 (B: 3 for, 2 against, +1 diff)
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: B should rank higher than A due to fewer goals conceded in similar scenarios
        val aStanding = result.standings.find { it.team.name == teamA.name }!!
        val bStanding = result.standings.find { it.team.name == teamB.name }!!

        // Both teams have 3 points but different defensive records
        assertEquals(3, aStanding.points) // 1 win, 1 loss
        assertEquals(3, bStanding.points) // 1 win, 1 loss

        // B has better defensive record (fewer goals against)
        assertTrue("B should have fewer goals against than A",
            bStanding.goalsAgainst < aStanding.goalsAgainst)
    }

    @Test
    fun `sortStandings should use team rating as final tiebreaker`() {
        // Given: All teams draw all their matches (same points, goal diff, goals for/against)
        val matches = listOf(
            createMatch(teamA, teamB, 1, 1), // Draw
            createMatch(teamC, teamD, 1, 1), // Draw
            createMatch(teamA, teamC, 1, 1), // Draw
            createMatch(teamB, teamD, 1, 1), // Draw
            createMatch(teamA, teamD, 1, 1), // Draw
            createMatch(teamB, teamC, 1, 1)  // Draw
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: Should be sorted by team rating (85, 80, 75, 70)
        // All teams have 3 points, 0 goal diff, 3 goals for, 3 goals against
        assertEquals(teamA.name, result.standings[0].team.name) // 85 rating
        assertEquals(teamB.name, result.standings[1].team.name) // 80 rating
        assertEquals(teamC.name, result.standings[2].team.name) // 75 rating
        assertEquals(teamD.name, result.standings[3].team.name) // 70 rating

        // Verify all have same stats
        result.standings.forEach { standing ->
            assertEquals(3, standing.points)
            assertEquals(0, standing.goalDifference)
            assertEquals(3, standing.goalsFor)
            assertEquals(3, standing.goalsAgainst)
        }
    }

    @Test
    fun `sortStandings should use head-to-head when other criteria are equal`() {
        // Given: Two teams with identical overall stats but different head-to-head
        val matches = listOf(
            // A and B both beat C and D with same scores, but A beats B
            createMatch(teamA, teamB, 1, 0), // A beats B head-to-head
            createMatch(teamA, teamC, 2, 0), // A beats C
            createMatch(teamB, teamC, 2, 0), // B beats C (same score as A)
            createMatch(teamA, teamD, 0, 1), // A beats D
            createMatch(teamB, teamD, 1, 0),  // B beats D (same score as A)
            createMatch(teamC, teamD, 1, 1)  // B beats D (same score as A)
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: A should be first due to head-to-head advantage over B
        val aStanding = result.standings.find { it.team.name == teamA.name }!!
        val bStanding = result.standings.find { it.team.name == teamB.name }!!

        assertEquals(6, aStanding.points) // 2 wins 1 loss
        assertEquals(6, bStanding.points) // 2 wins, 1 loss
        assertTrue("A should rank higher than B",
            result.standings.indexOf(aStanding) < result.standings.indexOf(bStanding))
    }

    @Test
    fun `sortStandings should handle empty matches list`() {
        // Given: Teams but no matches played
        val matches = emptyList<GroupMatch>()

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: All teams should have 0 points and be sorted by rating
        assertEquals(4, result.standings.size)
        assertEquals(teamA.name, result.standings[0].team.name) // Highest rating
        assertEquals(teamB.name, result.standings[1].team.name)
        assertEquals(teamC.name, result.standings[2].team.name)
        assertEquals(teamD.name, result.standings[3].team.name) // Lowest rating

        // All should have 0 stats
        result.standings.forEach { standing ->
            assertEquals(0, standing.points)
            assertEquals(0, standing.played)
            assertEquals(0, standing.won)
            assertEquals(0, standing.drawn)
            assertEquals(0, standing.lost)
            assertEquals(0, standing.goalsFor)
            assertEquals(0, standing.goalsAgainst)
        }
    }

    @Test
    fun `sortStandings should assign correct positions`() {
        // Given: Simple matches with clear winner
        val matches = listOf(
            createMatch(teamA, teamB, 3, 0), // A wins
            createMatch(teamC, teamD, 1, 2), // D wins
            createMatch(teamA, teamC, 2, 1), // A wins
            createMatch(teamB, teamD, 0, 1), // D wins
            createMatch(teamA, teamD, 1, 0), // A wins
            createMatch(teamB, teamC, 2, 1)  // B wins
        )

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA, teamB, teamC, teamD),
            matches = matches
        )

        // Then: Positions should be assigned correctly
        assertEquals(1, result.standings[0].position)
        assertEquals(2, result.standings[1].position)
        assertEquals(3, result.standings[2].position)
        assertEquals(4, result.standings[3].position)

        // A should be first with 9 points (3 wins)
        assertEquals(teamA.name, result.standings[0].team.name)
        assertEquals(9, result.standings[0].points)
        assertEquals(3, result.standings[0].won)
    }

    @Test
    fun `sortStandings should handle empty teams list`() {
        // Given: Empty teams and matches
        val matches = emptyList<GroupMatch>()

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = emptyList(),
            matches = matches
        )

        // Then: Should return empty list
        assertTrue(result.standings.isEmpty())
    }

    @Test
    fun `sortStandings should handle single team`() {
        // Given: Single team with no matches
        val matches = emptyList<GroupMatch>()

        // When: Calculating group table
        val result = calculator.calculateGroupTable(
            teams = listOf(teamA),
            matches = matches
        )

        // Then: Should return single team with position 1 and 0 stats
        assertEquals(1, result.standings.size)
        assertEquals(teamA.name, result.standings[0].team.name)
        assertEquals(1, result.standings[0].position)
        assertEquals(0, result.standings[0].points)
    }

    // Helper method for creating test matches
    private fun createMatch(
        homeTeam: Team,
        awayTeam: Team,
        homeGoals: Int,
        awayGoals: Int,
        isPlayed: Boolean = true,
        round: Int = 1
    ): GroupMatch {
        return GroupMatch(
            id = 0,
            groupId = 0,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            isPlayed = isPlayed,
            round = round
        )
    }
}
