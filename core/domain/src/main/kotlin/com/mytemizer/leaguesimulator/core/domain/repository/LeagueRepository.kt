package com.mytemizer.leaguesimulator.core.domain.repository

import com.mytemizer.leaguesimulator.core.domain.model.League
import com.mytemizer.leaguesimulator.core.domain.model.LeagueTable
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for league-related operations
 */
interface LeagueRepository {
    
    /**
     * Get all leagues
     */
    fun getAllLeagues(): Flow<List<League>>
    
    /**
     * Get active leagues
     */
    fun getActiveLeagues(): Flow<List<League>>
    
    /**
     * Get league by ID
     */
    suspend fun getLeagueById(leagueId: Long): League?
    
    /**
     * Insert a new league
     */
    suspend fun insertLeague(league: League): Long
    
    /**
     * Update an existing league
     */
    suspend fun updateLeague(league: League)
    
    /**
     * Delete a league
     */
    suspend fun deleteLeague(leagueId: Long)
    
    /**
     * Get league table for a specific season
     */
    suspend fun getLeagueTable(leagueId: Long, season: Int): LeagueTable?
    
    /**
     * Update league table
     */
    suspend fun updateLeagueTable(leagueTable: LeagueTable)
    
    /**
     * Search leagues by name or country
     */
    fun searchLeagues(query: String): Flow<List<League>>
}
