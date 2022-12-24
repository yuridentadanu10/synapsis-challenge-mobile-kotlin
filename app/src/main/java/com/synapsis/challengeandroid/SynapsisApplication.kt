package com.synapsis.challengeandroid

import android.app.Application
import com.synapsis.challengeandroid.core.ApplicationProvider
import com.synapsis.challengeandroid.core.di.dbModule
import com.synapsis.challengeandroid.core.di.repositoryModule
import com.synapsis.challengeandroid.di.useCaseModule
import com.synapsis.challengeandroid.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.NONE
import timber.log.Timber

class SynapsisApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        ApplicationProvider.init(this, BuildConfig.DEBUG)
        startKoin {
            androidLogger(NONE)
            androidContext(this@SynapsisApplication)
            modules(
                listOf(
                    dbModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }

}