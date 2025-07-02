package com.mytemizer.leaguesimulator.core.data.repository

import com.mytemizer.leaguesimulator.core.domain.model.Team
import com.mytemizer.leaguesimulator.core.domain.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicLong

/**
 * In-memory implementation of TeamRepository for testing and demo purposes
 * In a real app, this would use Room database
 */
class TeamRepositoryImpl : TeamRepository {
    
    private val teams = MutableStateFlow<List<Team>>(emptyList())
    private val idGenerator = AtomicLong(1)
    
    override suspend fun insertTeam(team: Team): Long {
        val newId = idGenerator.getAndIncrement()
        val teamWithId = team.copy(id = newId)

        val currentTeams = teams.value.toMutableList()
        currentTeams.add(teamWithId)
        teams.value = currentTeams
        
        return newId
    }

    override suspend fun resetTeams() {
        teams.value = emptyList()
    }
}
