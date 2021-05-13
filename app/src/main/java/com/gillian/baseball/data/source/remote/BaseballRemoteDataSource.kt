package com.gillian.baseball.data.source.remote

import android.util.Log
import com.gillian.baseball.data.Event
import com.gillian.baseball.data.source.BaseballDataSource

object BaseballRemoteDataSource : BaseballDataSource{

    override fun temp() {
        TODO("Not yet implemented")
    }

    override suspend fun sendEvent(event: Event) {
        Log.i("gillian", "todo")
    }
}