package com.synapsis.challengeandroid.core.di

import android.app.Application
import androidx.room.Room
import com.synapsis.challengeandroid.core.data.SynapsisDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

const val DB_NAME: String = "synapsis-database"

val dbModule = module {

    factory { get<SynapsisDatabase>().dataDao() }

    fun provideDatabase(application: Application): SynapsisDatabase {
        return Room.databaseBuilder(application, SynapsisDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single { provideDatabase(androidApplication()) }

}