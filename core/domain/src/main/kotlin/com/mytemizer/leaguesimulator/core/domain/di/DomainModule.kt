package com.mytemizer.leaguesimulator.core.domain.di

import com.mytemizer.leaguesimulator.core.domain.simulation.GroupTableCalculator
import com.mytemizer.leaguesimulator.core.domain.simulation.MatchSimulator
import com.mytemizer.leaguesimulator.core.domain.usecase.GenerateTeamsUseCase
import com.mytemizer.leaguesimulator.core.domain.usecase.ResetTournamentUseCase
import com.mytemizer.leaguesimulator.core.domain.usecase.SimulateGroupUseCase
import org.koin.dsl.module

/**
 * Koin module for domain layer dependencies
 */
val domainModule = module {

    // Simulation engines
    factory { MatchSimulator() }
    factory { GroupTableCalculator() }

    // Use Cases
    factory { GenerateTeamsUseCase(get()) }
    factory { SimulateGroupUseCase(get(), get()) }
    factory { ResetTournamentUseCase(get(), get()) }

}
