package com.synapsis.challengeandroid.core.data.synapsis.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soc_data")
data class DataSocEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "text_input") val textInput: String,

    @ColumnInfo(name = "date") val date: String,

    @ColumnInfo(name = "createdBy") val createdBy: String
)
