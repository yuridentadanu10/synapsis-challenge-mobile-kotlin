package com.synapsis.challengeandroid.core.data.synapsis.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "username") val username: String,

    @ColumnInfo(name = "password") val password: String,

    @ColumnInfo(name = "fingerPrint") val fingerPrintInfo: String = "",

    @ColumnInfo(name = "nfcInfo") val nfcNumber: String = ""
)
