package com.mytemizer.leaguesimulator.core.domain.usecase

import com.mytemizer.leaguesimulator.core.domain.model.LeagueTable
import com.mytemizer.leaguesimulator.core.domain.repository.LeagueRepository

/**
 * Use case for getting league table with standings
 */
class GetLeagueTableUseCase(
    private val leagueRepository: LeagueRepository
) {
    
    suspend operator fun invoke(params: GetLeagueTableParams): Result<LeagueTable?> {
        return try {
            val leagueTable = leagueRepository.getLeagueTable(
                leagueId = params.leagueId,
                season = params.season
            )
            Result.success(leagueTable)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    data class GetLeagueTableParams(
        val leagueId: Long,
        val season: Int
    )
}
