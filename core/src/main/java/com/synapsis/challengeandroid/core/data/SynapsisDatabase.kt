package com.synapsis.challengeandroid.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.synapsis.challengeandroid.core.data.synapsis.local.room.DataSocEntity
import com.synapsis.challengeandroid.core.data.synapsis.local.room.DataDao
import com.synapsis.challengeandroid.core.data.synapsis.local.room.UserEntity

@Database(
    entities = [DataSocEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SynapsisDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao

}