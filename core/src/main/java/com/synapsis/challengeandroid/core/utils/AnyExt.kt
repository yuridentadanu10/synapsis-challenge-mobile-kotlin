package com.synapsis.challengeandroid.core.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toStringDate(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun getCurrentDateAndTimeString(): String {
    return getCurrentDateTime().toStringDate("yyyy/MM/dd HH:mm:ss")
}