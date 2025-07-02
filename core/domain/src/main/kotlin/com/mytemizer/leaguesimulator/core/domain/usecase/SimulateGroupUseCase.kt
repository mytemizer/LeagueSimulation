package com.mytemizer.leaguesimulator.core.domain.usecase

import com.mytemizer.leaguesimulator.core.domain.model.*
import com.mytemizer.leaguesimulator.core.domain.simulation.GroupTableCalculator
import com.mytemizer.leaguesimulator.core.domain.simulation.MatchSimulator

/**
 * Use case for simulating a group stage with 4 teams
 */
class SimulateGroupUseCase(
    private val matchSimulator: MatchSimulator = MatchSimulator(),
    private val groupTableCalculator: GroupTableCalculator = GroupTableCalculator()
) {
    
    /**
     * Simulate a complete group stage
     */
    operator fun invoke(params: SimulateGroupParams): Result<GroupSimulationResult> {
        return try {
            require(params.teams.size == 4) { "Group must have exactly 4 teams" }
            
            // Simulate all matches
            val matches = matchSimulator.simulateGroup(params.teams, params.groupId)
            
            // Calculate group table
            val groupTable = groupTableCalculator.calculateGroupTable(params.teams, matches, params.groupId)
            
            // Get qualification status
            val qualificationStatus = groupTableCalculator.getQualificationStatus(groupTable)
            
            // Create result
            val result = GroupSimulationResult(
                groupTable = groupTable,
                qualificationStatus = qualificationStatus,
                simulationStats = calculateSimulationStats(groupTable)
            )
            
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateSimulationStats(groupTable: GroupTable): SimulationStats {
        val totalMatches = groupTable.matches.size
        val playedMatches = groupTable.matches.count { it.isPlayed }
        val totalGoals = groupTable.totalGoals
        
        val wins = groupTable.matches.count { it.isPlayed && !it.isDraw() }
        val draws = groupTable.matches.count { it.isDraw() }
        
        return SimulationStats(
            totalMatches = totalMatches,
            playedMatches = playedMatches,
            totalGoals = totalGoals,
            averageGoalsPerMatch = groupTable.averageGoalsPerMatch,
            wins = wins,
            draws = draws,
            winPercentage = if (playedMatches > 0) (wins.toDouble() / playedMatches) * 100 else 0.0,
            drawPercentage = if (playedMatches > 0) (draws.toDouble() / playedMatches) * 100 else 0.0
        )
    }
    
    data class SimulateGroupParams(
        val teams: List<Team>,
        val groupId: Long = 0
    )
}

/**
 * Result of a single group simulation
 */
data class GroupSimulationResult(
    val groupTable: GroupTable,
    val qualificationStatus: Map<Team, QualificationStatus>,
    val simulationStats: SimulationStats
)

/**
 * Statistics from group simulation
 */
data class SimulationStats(
    val totalMatches: Int,
    val playedMatches: Int,
    val totalGoals: Int,
    val averageGoalsPerMatch: Double,
    val wins: Int,
    val draws: Int,
    val winPercentage: Double,
    val drawPercentage: Double
)
