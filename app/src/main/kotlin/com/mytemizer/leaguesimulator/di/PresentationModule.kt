package com.mytemizer.leaguesimulator.di

import com.mytemizer.leaguesimulator.core.domain.repository.TournamentRepository
import com.mytemizer.leaguesimulator.ui.nextmatch.NextMatchViewModel
import com.mytemizer.leaguesimulator.ui.standings.StandingsViewModel
import com.mytemizer.leaguesimulator.ui.teamcreation.TeamCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for presentation layer dependencies
 */
val presentationModule = module {

    // ViewModels
    viewModel { TeamCreationViewModel(get(), get()) }
    viewModel { NextMatchViewModel(get(), get()) }
    viewModel { StandingsViewModel(get(), get()) }

}
