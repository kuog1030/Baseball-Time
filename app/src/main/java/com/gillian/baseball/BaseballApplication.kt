package com.gillian.baseball

import android.app.Application
import com.gillian.baseball.data.source.BaseballRepository
import com.gillian.baseball.util.ServiceLocator
import kotlin.properties.Delegates

class BaseballApplication : Application() {

    val repository: BaseballRepository
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: BaseballApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}