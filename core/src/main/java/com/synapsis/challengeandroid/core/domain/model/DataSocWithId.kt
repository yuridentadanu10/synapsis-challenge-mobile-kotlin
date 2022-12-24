package com.synapsis.challengeandroid.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSocWithId(
    val id: Int,
    val textInput: String,
    val date: String,
    val createdBy: String
): Parcelable