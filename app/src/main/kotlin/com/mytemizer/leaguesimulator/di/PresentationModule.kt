package com.mytemizer.leaguesimulator.di

import com.mytemizer.leaguesimulator.ui.TeamsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for presentation layer dependencies
 */
val presentationModule = module {
    
    // ViewModels
    viewModel { TeamsViewModel(get()) }
    
}
