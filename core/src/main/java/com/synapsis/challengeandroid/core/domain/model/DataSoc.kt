package com.synapsis.challengeandroid.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSoc(
    val textInput: String,
    val date: String,
    val createdBy: String
): Parcelable
