package com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref

import android.content.Context

object UserSession {
    private val pref: SharedPreferenceBuilder by lazy {
        SharedPreferenceBuilder("syanpsis-tag", Context.MODE_PRIVATE)
    }

    private const val KEY_USER_NAME = "key_user_name"
    private const val KEY_USER_ID = "key_user_id"

    var userName: String
        get() = pref.getStringPref(KEY_USER_NAME)
        set(value) = pref.setStringPref(KEY_USER_NAME, value)

    var userId: Int
        get() = pref.getIntPref(KEY_USER_ID)
        set(value) = pref.setIntPref(KEY_USER_ID, value)

}