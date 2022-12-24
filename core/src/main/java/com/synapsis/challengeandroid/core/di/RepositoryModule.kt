package com.synapsis.challengeandroid.core.di

import com.synapsis.challengeandroid.core.data.synapsis.DataRepository
import com.synapsis.challengeandroid.core.data.synapsis.local.LocalDataSource
import org.koin.dsl.module

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { DataRepository(get()) }
}