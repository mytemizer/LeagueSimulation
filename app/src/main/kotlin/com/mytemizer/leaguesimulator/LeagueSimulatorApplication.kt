package com.mytemizer.leaguesimulator

import android.app.Application
import com.mytemizer.leaguesimulator.core.data.di.dataModule
import com.mytemizer.leaguesimulator.core.domain.di.domainModule
import com.mytemizer.leaguesimulator.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application class for League Simulator
 * Initializes Koin dependency injection
 */
class LeagueSimulatorApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            // Koin Android logger
            androidLogger(Level.DEBUG)

            // Android context
            androidContext(this@LeagueSimulatorApplication)

            // Modules
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
