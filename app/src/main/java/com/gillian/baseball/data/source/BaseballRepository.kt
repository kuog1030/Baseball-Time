package com.gillian.baseball.data.source

import com.gillian.baseball.data.Event

interface BaseballRepository {

    fun temp()

    suspend fun sendEvent(event: Event)

}