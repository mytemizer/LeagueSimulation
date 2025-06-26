package com.mytemizer.leaguesimulator.core.domain.repository

import com.mytemizer.leaguesimulator.core.domain.model.Team
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for team-related operations
 */
interface TeamRepository {
    
    /**
     * Get all teams
     */
    fun getAllTeams(): Flow<List<Team>>
    
    /**
     * Get teams by league ID
     */
    fun getTeamsByLeague(leagueId: Long): Flow<List<Team>>
    
    /**
     * Get team by ID
     */
    suspend fun getTeamById(teamId: Long): Team?
    
    /**
     * Insert a new team
     */
    suspend fun insertTeam(team: Team): Long
    
    /**
     * Update an existing team
     */
    suspend fun updateTeam(team: Team)
    
    /**
     * Delete a team
     */
    suspend fun deleteTeam(teamId: Long)
    
    /**
     * Search teams by name
     */
    fun searchTeams(query: String): Flow<List<Team>>
    
    /**
     * Get user's favorite teams
     */
    fun getUserTeams(): Flow<List<Team>>
    
    /**
     * Generate random teams for a league
     */
    suspend fun generateRandomTeams(count: Int, leagueId: Long): List<Team>
}
