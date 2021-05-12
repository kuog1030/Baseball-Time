package com.gillian.baseball

import android.app.Application
import kotlin.properties.Delegates

class BaseballApplication : Application() {

    companion object {
        var instance: BaseballApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}