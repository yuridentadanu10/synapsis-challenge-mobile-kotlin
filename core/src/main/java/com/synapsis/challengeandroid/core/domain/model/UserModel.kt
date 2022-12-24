package com.synapsis.challengeandroid.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int = 0,
    val userName: String,
    val password: String,
    val fingerPrint: String = "",
    val nfcInfo: String = ""
): Parcelable

