package com.gillian.baseball.data.source.local

import android.content.Context
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.source.BaseballDataSource

class BaseballLocalDataSource(val context: Context) : BaseballDataSource {

    override fun temp() {
        TODO("Not yet implemented")
    }

    override suspend fun sendEvent(event: Event) {
        TODO("Not yet implemented")
    }
}