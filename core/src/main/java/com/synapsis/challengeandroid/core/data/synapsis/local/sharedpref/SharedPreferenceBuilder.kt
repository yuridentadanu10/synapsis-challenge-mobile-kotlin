package com.synapsis.challengeandroid.core.data.synapsis.local.sharedpref

import android.content.SharedPreferences
import androidx.core.content.edit
import com.synapsis.challengeandroid.core.ApplicationProvider

class SharedPreferenceBuilder(tag: String, mode: Int) {
    private val context = ApplicationProvider.context
    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(tag, mode)
    }
    
    fun getBooleanPref(key: String, default: Boolean = false): Boolean {
        return pref.getBoolean(key, default)
    }

    fun setBooleanPref(key: String, value: Boolean) {
        pref.edit { putBoolean(key, value) }
    }

    fun getStringPref(key: String, default: String = ""): String {
        return pref.getString(key, default).orEmpty()
    }

    fun setStringPref(key: String, value: String) {
        pref.edit { putString(key, value) }
    }

    fun getIntPref(key: String, default: Int = 0): Int {
        return pref.getInt(key, default)
    }

    fun setIntPref(key: String, value: Int) {
        pref.edit { putInt(key, value) }
    }

    fun getLongPref(key: String, default: Long = 0L): Long {
        return pref.getLong(key, default)
    }

    fun setLongPref(key: String, value: Long) {
        pref.edit { putLong(key, value) }
    }
}