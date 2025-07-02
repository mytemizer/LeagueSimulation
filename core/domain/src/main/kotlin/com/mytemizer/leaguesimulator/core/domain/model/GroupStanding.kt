package com.mytemizer.leaguesimulator.core.domain.model

/**
 * Represents a team's standing in a group
 */
data class GroupStanding(
    val team: Team,
    val position: Int = 0,
    val played: Int = 0,
    val won: Int = 0,
    val drawn: Int = 0,
    val lost: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val points: Int = 0
) {
    /**
     * Calculate goal difference
     */
    val goalDifference: Int get() = goalsFor - goalsAgainst

    /**
     * Create updated standing with new match result
     */
    fun withMatchResult(
        result: MatchResult,
        goalsScored: Int,
        goalsConceded: Int
    ): GroupStanding {
        return when (result) {
            MatchResult.WIN -> copy(
                played = played + 1,
                won = won + 1,
                goalsFor = goalsFor + goalsScored,
                goalsAgainst = goalsAgainst + goalsConceded,
                points = points + 3
            )
            MatchResult.DRAW -> copy(
                played = played + 1,
                drawn = drawn + 1,
                goalsFor = goalsFor + goalsScored,
                goalsAgainst = goalsAgainst + goalsConceded,
                points = points + 1
            )
            MatchResult.LOSS -> copy(
                played = played + 1,
                lost = lost + 1,
                goalsFor = goalsFor + goalsScored,
                goalsAgainst = goalsAgainst + goalsConceded
            )
        }
    }
}

/**
 * Represents the complete group table
 */
data class GroupTable(
    val groupId: Long,
    val standings: List<GroupStanding>,
    val matches: List<GroupMatch>,
    val isComplete: Boolean = false
) {
    /**
     * Get teams that qualify for knockout stage
     */
    val qualifiedTeams: List<Team> get() = standings.take(2).map { it.team }

    /**
     * Get teams that are eliminated
     */
    val eliminatedTeams: List<Team> get() = standings.drop(2).map { it.team }

    /**
     * Get total goals scored in the group
     */
    val totalGoals: Int get() = matches.filter { it.isPlayed }.sumOf { it.homeGoals + it.awayGoals }

    /**
     * Get average goals per match
     */
    val averageGoalsPerMatch: Double get() {
        val playedMatches = matches.count { it.isPlayed }
        return if (playedMatches > 0) totalGoals.toDouble() / playedMatches else 0.0
    }

    /**
     * Get head-to-head record between two teams
     */
    fun getHeadToHeadRecord(team1: Team, team2: Team): HeadToHeadRecord {
        val h2hMatches = matches.filter { match ->
            (match.homeTeam.name == team1.name && match.awayTeam.name == team2.name) ||
            (match.homeTeam.name == team2.name && match.awayTeam.name == team1.name)
        }

        var team1Goals = 0
        var team2Goals = 0
        var team1Points = 0
        var team2Points = 0

        h2hMatches.forEach { match ->
            if (match.isPlayed) {
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
        }

        return HeadToHeadRecord(
            team1 = team1,
            team2 = team2,
            team1Goals = team1Goals,
            team2Goals = team2Goals,
            team1Points = team1Points,
            team2Points = team2Points,
            matches = h2hMatches
        )
    }
}

/**
 * Head-to-head record between two teams
 */
data class HeadToHeadRecord(
    val team1: Team,
    val team2: Team,
    val team1Goals: Int,
    val team2Goals: Int,
    val team1Points: Int,
    val team2Points: Int,
    val matches: List<GroupMatch>
) {
    val team1GoalDifference: Int get() = team1Goals - team2Goals
    val team2GoalDifference: Int get() = team2Goals - team1Goals

    /**
     * Get winner of head-to-head record
     */
    fun getWinner(): Team? = when {
        team1Points > team2Points -> team1
        team2Points > team1Points -> team2
        team1GoalDifference > team2GoalDifference -> team1
        team2GoalDifference > team1GoalDifference -> team2
        team1Goals > team2Goals -> team1
        team2Goals > team1Goals -> team2
        else -> null // Tied
    }
}

enum class MatchResult {
    WIN,
    DRAW,
    LOSS
}
