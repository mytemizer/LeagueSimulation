package com.mytemizer.leaguesimulator.core.domain.usecase

import com.mytemizer.leaguesimulator.core.domain.model.League
import com.mytemizer.leaguesimulator.core.domain.repository.LeagueRepository
import com.mytemizer.leaguesimulator.core.domain.repository.TeamRepository

/**
 * Use case for creating a new league with teams
 */
class CreateLeagueUseCase(
    private val leagueRepository: LeagueRepository,
    private val teamRepository: TeamRepository
) {
    
    suspend operator fun invoke(params: CreateLeagueParams): Result<Long> {
        return try {
            // Validate league configuration
            if (!params.league.isValidConfiguration()) {
                return Result.failure(IllegalArgumentException("Invalid league configuration"))
            }
            
            // Create the league
            val leagueId = leagueRepository.insertLeague(params.league)
            
            // Generate teams if requested
            if (params.generateRandomTeams) {
                val teams = teamRepository.generateRandomTeams(
                    count = params.league.numberOfTeams,
                    leagueId = leagueId
                )
                
                teams.forEach { team ->
                    teamRepository.insertTeam(team)
                }
            }
            
            Result.success(leagueId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    data class CreateLeagueParams(
        val league: League,
        val generateRandomTeams: Boolean = true
    )
}
