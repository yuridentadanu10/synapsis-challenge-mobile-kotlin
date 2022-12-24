package com.synapsis.challengeandroid.core

import android.content.Context

object ApplicationProvider {

    lateinit var context: Context
        private set

    var isDebugMode: Boolean = true
        private set

    fun init(context: Context, isDebugMode: Boolean) {
        this.context = context
        this.isDebugMode = isDebugMode
    }

}