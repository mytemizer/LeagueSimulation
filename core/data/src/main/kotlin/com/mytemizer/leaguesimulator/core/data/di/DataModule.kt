package com.mytemizer.leaguesimulator.core.data.di

import com.mytemizer.leaguesimulator.core.data.repository.TeamRepositoryImpl
import com.mytemizer.leaguesimulator.core.data.repository.TournamentRepositoryImpl
import com.mytemizer.leaguesimulator.core.domain.repository.TeamRepository
import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import org.koin.dsl.module

/**
 * Koin module for data layer dependencies
 */
val dataModule = module {

    // Repositories
    single<TeamRepository> { TeamRepositoryImpl() }
    single<TournamentRepository> { TournamentRepositoryImpl(get(), get()) }
}
