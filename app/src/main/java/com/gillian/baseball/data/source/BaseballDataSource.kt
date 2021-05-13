package com.gillian.baseball.data.source

import com.gillian.baseball.data.Event

interface BaseballDataSource {

    fun temp()

    suspend fun sendEvent(event: Event)

}