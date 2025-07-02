package com.mytemizer.leaguesimulator.core.domain.repository

import com.mytemizer.leaguesimulator.core.domain.model.Team
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for team-related operations
 */
interface TeamRepository {
    /**
     * Insert a new team
     */
    suspend fun insertTeam(team: Team): Long


    suspend fun resetTeams()
}
