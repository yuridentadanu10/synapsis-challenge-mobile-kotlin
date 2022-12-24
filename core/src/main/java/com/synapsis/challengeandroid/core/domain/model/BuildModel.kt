package com.synapsis.challengeandroid.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BuildModel(
    val type: String,
    val name: String
) : Parcelable
