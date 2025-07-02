package com.mytemizer.leaguesimulator.core.domain.simulation

import com.mytemizer.leaguesimulator.core.domain.model.*

/**
 * Calculates group standings and handles sorting according to FIFA rules
 */
class GroupTableCalculator {

    /**
     * Calculate group table from matches
     */
    fun calculateGroupTable(teams: List<Team>, matches: List<GroupMatch>, groupId: Long = 0): GroupTable {
        // Initialize standings for all teams
        val standings = teams.map { team ->
            GroupStanding(team = team)
        }.toMutableList()

        // Process each match to update standings
        matches.filter { it.isPlayed }.forEach { match ->
            updateStandingsWithMatch(standings, match)
        }

        // Sort standings according to FIFA rules
        val sortedStandings = sortStandings(standings, matches)

        // Assign positions
        val finalStandings = sortedStandings.mapIndexed { index, standing ->
            standing.copy(position = index + 1)
        }

        return GroupTable(
            groupId = groupId,
            standings = finalStandings,
            matches = matches,
            isComplete = matches.all { it.isPlayed }
        )
    }

    /**
     * Update standings with a single match result
     */
    private fun updateStandingsWithMatch(standings: MutableList<GroupStanding>, match: GroupMatch) {
        // Update home team - use team name for matching since IDs might be 0
        val homeTeamIndex = standings.indexOfFirst { it.team.name == match.homeTeam.name }
        if (homeTeamIndex != -1) {
            val homeResult = when {
                match.homeGoals > match.awayGoals -> MatchResult.WIN
                match.homeGoals < match.awayGoals -> MatchResult.LOSS
                else -> MatchResult.DRAW
            }
            standings[homeTeamIndex] = standings[homeTeamIndex].withMatchResult(
                result = homeResult,
                goalsScored = match.homeGoals,
                goalsConceded = match.awayGoals
            )
        }

        // Update away team - use team name for matching since IDs might be 0
        val awayTeamIndex = standings.indexOfFirst { it.team.name == match.awayTeam.name }
        if (awayTeamIndex != -1) {
            val awayResult = when {
                match.awayGoals > match.homeGoals -> MatchResult.WIN
                match.awayGoals < match.homeGoals -> MatchResult.LOSS
                else -> MatchResult.DRAW
            }
            standings[awayTeamIndex] = standings[awayTeamIndex].withMatchResult(
                result = awayResult,
                goalsScored = match.awayGoals,
                goalsConceded = match.homeGoals
            )
        }
    }

    /**
     * Sort standings according to FIFA rules:
     * 1. Points
     * 2. Goal difference
     * 3. Goals for
     * 4. Goals against (fewer is better)
     * 5. Head-to-head result
     */
    private fun sortStandings(standings: List<GroupStanding>, matches: List<GroupMatch>): List<GroupStanding> {
        return standings.sortedWith { standing1, standing2 ->
            // 1. Points (descending)
            val pointsComparison = standing2.points.compareTo(standing1.points)
            if (pointsComparison != 0) return@sortedWith pointsComparison

            // 2. Goal difference (descending)
            val goalDiffComparison = standing2.goalDifference.compareTo(standing1.goalDifference)
            if (goalDiffComparison != 0) return@sortedWith goalDiffComparison

            // 3. Goals for (descending)
            val goalsForComparison = standing2.goalsFor.compareTo(standing1.goalsFor)
            if (goalsForComparison != 0) return@sortedWith goalsForComparison

            // 4. Goals against (ascending - fewer is better)
            val goalsAgainstComparison = standing1.goalsAgainst.compareTo(standing2.goalsAgainst)
            if (goalsAgainstComparison != 0) return@sortedWith goalsAgainstComparison

            // 5. Head-to-head result
            val h2hComparison = compareHeadToHead(standing1.team, standing2.team, matches)
            if (h2hComparison != 0) return@sortedWith h2hComparison

            // 6. If still tied, use team strength as tiebreaker
            standing2.team.overallRating.compareTo(standing1.team.overallRating)
        }
    }

    /**
     * Compare two teams based on head-to-head record
     */
    private fun compareHeadToHead(team1: Team, team2: Team, matches: List<GroupMatch>): Int {
        val h2hMatches = matches.filter { match ->
            match.isPlayed && (
                (match.homeTeam.name == team1.name && match.awayTeam.name == team2.name) ||
                (match.homeTeam.name == team2.name && match.awayTeam.name == team1.name)
            )
        }

        if (h2hMatches.isEmpty()) return 0

        var team1Points = 0
        var team2Points = 0
        var team1Goals = 0
        var team2Goals = 0

        h2hMatches.forEach { match ->
            when {
                match.homeTeam.name == team1.name -> {
                    team1Goals += match.homeGoals
                    team2Goals += match.awayGoals
                    team1Points += match.getHomeTeamPoints()
                    team2Points += match.getAwayTeamPoints()
                }
                match.homeTeam.name == team2.name -> {
                    team2Goals += match.homeGoals
                    team1Goals += match.awayGoals
                    team2Points += match.getHomeTeamPoints()
                    team1Points += match.getAwayTeamPoints()
                }
            }
        }

        // Compare head-to-head points
        val h2hPointsComparison = team2Points.compareTo(team1Points)
        if (h2hPointsComparison != 0) return h2hPointsComparison

        // Compare head-to-head goal difference
        val team1H2HGoalDiff = team1Goals - team2Goals
        val team2H2HGoalDiff = team2Goals - team1Goals
        val h2hGoalDiffComparison = team2H2HGoalDiff.compareTo(team1H2HGoalDiff)
        if (h2hGoalDiffComparison != 0) return h2hGoalDiffComparison

        // Compare head-to-head goals for
        return team2Goals.compareTo(team1Goals)
    }

    /**
     * Get qualification status for teams
     */
    fun getQualificationStatus(groupTable: GroupTable): Map<Team, QualificationStatus> {
        return groupTable.standings.associate { standing ->
            val status = when (standing.position) {
                1 -> QualificationStatus.QUALIFIED_FIRST
                2 -> QualificationStatus.QUALIFIED_SECOND
                3 -> QualificationStatus.ELIMINATED_THIRD
                4 -> QualificationStatus.ELIMINATED_FOURTH
                else -> QualificationStatus.ELIMINATED_FOURTH
            }
            standing.team to status
        }
    }
}


