package com.mytemizer.leaguesimulator.core.domain.di

import com.mytemizer.leaguesimulator.core.domain.usecase.CreateLeagueUseCase
import com.mytemizer.leaguesimulator.core.domain.usecase.GenerateTeamsUseCase
import com.mytemizer.leaguesimulator.core.domain.usecase.GetLeagueTableUseCase
import org.koin.dsl.module

/**
 * Koin module for domain layer dependencies
 */
val domainModule = module {
    
    // Use Cases
    factory { CreateLeagueUseCase(get(), get()) }
    factory { GenerateTeamsUseCase(get()) }
    factory { GetLeagueTableUseCase(get()) }
    
}
