package com.mytemizer.leaguesimulator.core.domain.usecase

import com.mytemizer.leaguesimulator.core.domain.mock.MockTeamGenerator
import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.core.domain.repository.TeamRepository
import com.mytemizer.leaguesimulator.core.common.util.Constants

/**
 * Use case for generating teams for a league
 */
class GenerateTeamsUseCase(
    private val teamRepository: TeamRepository
) {

    suspend operator fun invoke(params: GenerateTeamsParams): Result<List<Team>> {
        return try {
            val teams = when (params.generationType) {
                GenerationType.MIXED -> {
                    MockTeamGenerator.generateTeams(
                        count = params.teamCount
                    )
                }
                GenerationType.WORLD_CLASS -> {
                    // Generate teams with high individual ratings that result in 85+ overall
                    (1..params.teamCount).map {
                        MockTeamGenerator.generateWorldClassTeam()
                    }
                }
                GenerationType.EXCELLENT -> {
                    // Generate teams with good individual ratings that result in 75-84 overall
                    (1..params.teamCount).map {
                        MockTeamGenerator.generateExcellentTeam()
                    }
                }
                GenerationType.GOOD -> {
                    // Generate teams with decent individual ratings that result in 65-74 overall
                    (1..params.teamCount).map {
                        MockTeamGenerator.generateGoodTeam()
                    }
                }
            }

            // Insert teams into repository
            val insertedTeams = mutableListOf<Team>()
            teams.forEach { team ->
                val teamId = teamRepository.insertTeam(team)
                insertedTeams.add(team.copy(id = teamId))
            }

            Result.success(insertedTeams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    data class GenerateTeamsParams(
        val teamCount: Int,
        val generationType: GenerationType,
    ) {
        init {
            require(teamCount >= Constants.MIN_TEAMS_IN_LEAGUE) { "Team count must be at least ${Constants.MIN_TEAMS_IN_LEAGUE}" }
            require(teamCount <= Constants.MAX_TEAMS_IN_LEAGUE) { "Team count cannot exceed ${Constants.MAX_TEAMS_IN_LEAGUE}" }
            require(teamCount % 2 == 0) { "Team count must be even for proper league scheduling" }
        }
    }

    enum class GenerationType {
        MIXED,
        WORLD_CLASS,
        EXCELLENT,
        GOOD
    }
}
